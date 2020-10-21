package com.skt.hrs.cmmn.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pub.core.constans.ResultConst;
import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.JsonUtils;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.user.service.UserService;

/**
 * 
 * @FileName  : CodeController.java
 * @프로그램 설명   : 공통코드 컨트롤러
 * @Date      : 2019. 8. 27. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	/**
	 * 
	 * @설명 : 개발중 테스트로그인 페이지 view 
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/testLogin")
	public String resveStatusView(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		return "testSSOLogin";
	}
	
	/**
	 * 
	 * @설명 : (테스트) SSO 로그인 처리. testLogin에서 호출됨.
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @return
	 * @변경이력 :
	 */	
	@RequestMapping(value = "/testSSOLogin")
	public String ssoLogin(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
			
		//String paramUserid = getUserId(req);
		String paramUserid = req.getHeader("TEST_SM_USER");
		
		logger.debug("**************** USER *************** => " + paramUserid);
		
		DataEntity param = new DataEntity();
		param.put("empno", paramUserid);

		ResponseResult result = userService.selectUserInfo(param);
		Map user = result.getItem();

		if(user != null) {
			LoginVo loginVo = new LoginVo();
			loginVo.setEmpno(user.get("EMPNO").toString());
			loginVo.setHname(user.get("HNAME").toString());
			loginVo.setPlace(user.get("PLACE").toString());
			loginVo.settSex(user.get("T_SEX").toString());
			loginVo.setAuth( user.get("AUTH_CODE") == null ? 
					"" : user.get("AUTH_CODE").toString());	//구성원
			loginVo.setHrsAgree(user.get("HRS_AGREE").toString());
			sess.setAttribute("LoginVo", loginVo);
			
			// forward로 넘어온경우
			//return "redirect:" + (String)req.getAttribute( "javax.servlet.forward.request_uri" );
			return "redirect:/resve/status";
		}else {
			
			String headerInfo = req.getHeader("X-Requested-With");
			
			
			res.setContentType("application/json;charset=UTF-8");
			
			if ("XMLHttpRequest".equals(headerInfo)) {
				res.setContentType("application/json;charset=UTF-8");
				res.setStatus(HttpStatus.OK.value());				
				result = new ResponseResult();
				result.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
				result.setStatus(ResultConst.CODE.FORBIDDEN.toInt());
				result.setMessage("Login first.");
				Writer writer = null;
				try {
					writer = res.getWriter();
					writer.write(JsonUtils.objectToString(result));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						try {writer.close();} catch (IOException e) {}
					};					
				}
				return null;
			}else {				
				return "redirect:/error/403" ;
			}
		}
		
				
	}
	
	
}
