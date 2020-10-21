package com.skt.hrs.cmmn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @FileName  : ErrorController.java
 * @프로그램 설명   : 에러페이지 컨트롤러
 * @Date      : 2019. 9. 16. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController {

	
	@RequestMapping(value = "/{code}")
	public String resveStatusView(@PathVariable("code") String code) throws Exception {		
		return "error/error"+code;
	}
}
