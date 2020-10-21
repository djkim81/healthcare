package com.skt.hrs.cmmn.interceptor;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pub.core.constans.ResultConst;
import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.JsonUtils;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.user.service.UserService;
import com.skt.hrs.utils.StringUtil;

/**
 * SSO 로그인 처리 인터셉터
 * @author P149365
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserService userService;
		
	@Override	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.info("LoginInterceptor URI : " + request.getRequestURI());
		
		HttpSession session = request.getSession();
		
		// 세션이 없다면
		if(session == null || session.getAttribute("LoginVo") == null) {			
			logger.info("SESSION => null");
			
			// SSO id 조회
			String paramUserid = getUserId(request);
			logger.info("SSO ID => " + (StringUtil.isEmpty(paramUserid) ? "null" : paramUserid));
			
			// SSO id가 없으면 거부
			if(StringUtil.isEmpty(paramUserid)) {
				forbidden(request, response);
				return false;
			}					
			
			// SSO id로 사용자 조회하여 로그인처리
			DataEntity param = new DataEntity();
			param.put("empno", paramUserid);

			ResponseResult result = userService.selectUserInfo(param);
			Map user = result.getItem();				
						
			if(user != null) {
				
				String authCode = user.get("AUTH_CODE") == null ? "" : user.get("AUTH_CODE").toString();				
				
				
				// P사번 19사번은 관리자일때만 접속 가능
				if(paramUserid.startsWith("P") || paramUserid.startsWith("19")) {
					if(StringUtil.isEmpty(authCode)) {					
						response.sendRedirect("/error/403");
						return false;
					}
				}
				
				LoginVo loginVo = new LoginVo();
				loginVo.setEmpno(user.get("EMPNO").toString());
				loginVo.setHname(user.get("HNAME").toString());
				loginVo.setPlace(user.get("PLACE").toString());
				loginVo.settSex(user.get("T_SEX").toString());
				loginVo.setAuth(authCode);
				loginVo.setHrsAgree(user.get("HRS_AGREE").toString());
				
				//HttpSession sess = request.getSession();
				session.setAttribute("LoginVo", loginVo);
																
			}else {
				forbidden(request, response);
				return false;
			}
			
		}else {
			// P사번 19사번은 관리자일때만 접속 가능
			LoginVo loginVo = (LoginVo) session.getAttribute("LoginVo");
			if(loginVo.getEmpno().startsWith("P") || loginVo.getEmpno().startsWith("19")) {
				if(StringUtil.isEmpty(loginVo.getAuth())) {					
					response.sendRedirect("/error/403");
					return false;
				}
			}
		}
		logger.info("SESSION => " + ((LoginVo) session.getAttribute("LoginVo")).getEmpno());
		return true;
	}
	
	/**
	 * 
	 * @설명 : 권한없음 처리 
	 * @작성일 : 2019.09.10
	 * @작성자 : P149365
	 * @param request
	 * @param response
	 * @throws Exception
	 * @변경이력 :
	 */
	private void forbidden(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseResult result = new ResponseResult();
		String headerInfo = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(headerInfo)) {
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.OK.value());
			result = new ResponseResult();
			result.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
			result.setStatus(ResultConst.CODE.FORBIDDEN.toInt());
			result.setMessage("Login first.");
			Writer writer = null;
			try {
				writer = response.getWriter();
				writer.write(JsonUtils.objectToString(result));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {writer.close();} catch (IOException e) {}
				};
			}
		} else {

			response.sendRedirect("/error/403");
		}
	}
	
	/**
	 * 
	 * @설명 : sso를 통하여 userId 얻기 
	 * @작성일 : 2019.09.16
	 * @작성자 : P149365
	 * @param request
	 * @return
	 * @변경이력 :
	 */
	private String getUserId(HttpServletRequest request) {

		String paramUserid = "";
		HttpSession session = request.getSession();
		String adminsetUser = (String) session.getAttribute("adminsetuser");

		if (adminsetUser != null && adminsetUser != "") {
			// 로컬이든 운영이든 모든 가장을 통해서 들어오면 그것이 우선
			paramUserid = adminsetUser;
		} else {

			try {
				paramUserid = request.getHeader("SM_USER");
			} catch (Exception e) {

			}

		}
		if (paramUserid == null || "".equals(paramUserid)) {

			// local에서 admin_login을 통해서 들어오지 않았다는 뜻
			// window 로긴 계정을 얻는다
			// paramUserid = "P021090";
			
			/* ---------- window 계정 얻는부분 -----------
			paramUserid = System.getProperty("user.name");

			if (paramUserid.indexOf("\\") != -1) {

				String[] arrParamUserid = paramUserid.split("\\");
				if (arrParamUserid.length > 0) {
					paramUserid = arrParamUserid[1];
				}
			}
			---------------------------------------------*/

		}
		/*
		 * Enumeration<String> headerNameEnum = request.getHeaderNames();
		 * 
		 * String headerName; while(headerNameEnum.hasMoreElements()) { headerName =
		 * headerNameEnum.nextElement(); logger.debug("header key [{}]  : val [{}]" ,
		 * headerName , request.getHeader(headerName)); }
		 */

		logger.debug("header val getUserId \t paramUserid :  " + paramUserid);
		paramUserid = (paramUserid == null ? "" : paramUserid).toUpperCase();
		paramUserid = paramUserid.replace("SKT\\", "");

		paramUserid = paramUserid != null && !"".equals(paramUserid) && !"wpadmin".equals(paramUserid)
				&& !"wpadmin01".equals(paramUserid) && !"wpadmin02".equals(paramUserid) ? paramUserid : "";

		logger.debug("getUserId \t paramUserid :  " + paramUserid);
		return paramUserid;
	}
}
