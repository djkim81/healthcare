package com.skt.hrs.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.user.service.UserService;



/**
 * 
 * @FileName  : UserController.java
 * @프로그램 설명   : 헬스케어 예약 시스템 사용자 화면  컨트롤러
 * @Date      : 2019. 8. 27. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/agree", method={RequestMethod.POST, RequestMethod.PUT})
	public @ResponseBody ResponseResult updateUserAgree(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());
		ResponseResult result = userService.updateAgree(param);
		if((boolean)result.getItem()) {
			loginVo.setHrsAgree("Y");
			sess.setAttribute("LoginVo", loginVo);
		}
		return result;
	}
	

}
