package com.skt.hrs.cmmn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.user.service.UserService;
import com.skt.hrs.utils.StringUtil;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserService userService;
	
	
	@Override	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.info("AuthInterceptor URI :" + request.getRequestURI());
		HttpSession session = request.getSession();
		
		if(session == null || session.getAttribute("LoginVo") == null) {			
			return false;
		}else {
			LoginVo loginVo = (LoginVo) session.getAttribute("LoginVo");
			if(StringUtil.isEmpty(loginVo.getAuth())){
				response.sendRedirect("/error/403");
				return false;
			}
		}
		return true;
	}
		
}
