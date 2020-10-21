package com.skt.hrs.cmmn.exception;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.pub.core.constans.ResultConst;
import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.pub.core.util.JsonUtils;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.utils.StringUtil;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Autowired
	MessageSource messageSource;
	
	/**
	 * 
	 * @설명 : sql exception 처리 
	 * @작성일 : 2019.09.02
	 * @작성자 : P149365
	 * @param ex
	 * @param request
	 * @param response
	 * @변경이력 :
	 */
	@ExceptionHandler(value=SQLException.class)
	public void sqlExceptionHandle(SQLException ex, HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error("sqlExceptionHandle "+ getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ResultConst.CODE.ERROR.toInt());
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result,"sqlex");
		
	}
		
	/**
	 * 
	 * @설명 : runtime exception 처리 
	 * @작성일 : 2019.09.02
	 * @작성자 : P149365
	 * @param ex
	 * @param request
	 * @param response
	 * @변경이력 :
	 */
	@ExceptionHandler(value=RuntimeException.class)
	public void runtimeExceptionHandle(RuntimeException ex, HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error("runtimeExceptionHandle : ", getClass().getName(),ex);
		
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ResultConst.CODE.ERROR.toInt());
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result,"runtimeex");
	}
	
	/**
	 * 	
	 * @설명 : Hrs exception 처리
	 * @작성일 : 2019.09.02
	 * @작성자 : P149365
	 * @param ex
	 * @param request
	 * @param response
	 * @변경이력 :
	 */
	@ExceptionHandler(value=HrsException.class)
	public void hrsExceptionHandle(HrsException ex, HttpServletRequest request ,  HttpServletResponse response, Locale locale){
		
		//logger.error(getClass().getName(),ex);
		logger.error("{} - {}", ex.getMessage(), messageSource.getMessage(ex.getMessage(), null, request.getLocale()).replaceAll("\\\n", " "));	//비즈니스에러 로그는 메세지만 간단히 찍도록
		 
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ex.getErrorCode() > 0 ? ex.getErrorCode() : ResultConst.CODE.ERROR.toInt());		
		
		if(!StringUtil.isEmpty(ex.getMessageCode())) {
			result.setMessageCode(ex.getMessageCode());
			if(StringUtil.isEmpty(ex.getErrorMessage())) {
				
				result.setMessage(messageSource.getMessage(ex.getMessageCode(), ex.getMessageArgs(), locale));
			}else {
				result.setMessage(ex.getErrorMessage());
			}
		}else {
			result.setMessage(ex.getMessage());
		}
		
		exceptionRequestHandle(request, response ,result, "hrsex");
			
	}
	
	@ExceptionHandler(value=Exception.class)
	public void exceptionHandle(HrsException ex, HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ex.getErrorCode() > 0 ? ex.getErrorCode() : ResultConst.CODE.ERROR.toInt());
		result.setMessageCode(ex.getMessageCode());
		result.setMessage(ex.getErrorMessage());
		
		exceptionRequestHandle(request, response ,result,"ex");
	}
	
	private void exceptionRequestHandle(HttpServletRequest request, HttpServletResponse response ,ResponseResult result ) {
		exceptionRequestHandle(request ,response , result  , "connError");
	}
	
	private void exceptionRequestHandle(HttpServletRequest request, HttpServletResponse response ,ResponseResult result, String pageName) {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		LoginVo loginVo = (LoginVo) (request.getSession().getAttribute("LoginVo"));
		logger.error("EXCEPTION LoginVo : {}", loginVo.toString());
		logger.error("EXCEPTION REQUEST PARAMETER : {}", param.toString());
		
		String headerInfo = request.getHeader("X-Requested-With");
		
		if("XMLHttpRequest".equals(headerInfo)){
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.OK.value());
			//response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			result.setResultCode(ResultConst.CODE.ERROR.toInt());
			
			if(StringUtil.isEmpty(result.getMessage())) {
				result.setMessage(messageSource.getMessage(result.getMessageCode(), null, request.getLocale()));
			}
			Writer writer=null;
			try {
				writer = response.getWriter();
				writer.write(JsonUtils.objectToString(result));				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(writer!=null){ try {writer.close();} catch (IOException e) {}};
			}
		}else{
			try {
				response.sendRedirect("/error/500");
			} catch (IOException e1) {
				logger.error("exceptionRequestHandle sendRedirect error ", e1);
			}
		}		
	}
	
}