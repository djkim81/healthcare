package com.skt.hrs.cmmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.pub.core.util.JsonUtils;

import com.skt.hrs.cmmn.service.RestDeService;
import com.skt.hrs.cmmn.vo.LoginVo;



/**
 * 
 * @FileName  : CodeController.java
 * @프로그램 설명   : 공통코드 컨트롤러
 * @Date      : 2019. 8. 27. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/cmmn")
public class RestDeController {
	
	private static final Logger logger = LoggerFactory.getLogger(RestDeController.class);
	
	@Autowired
	RestDeService restDeService;
	
	/**
	 * 
	 * @설명 :  휴일 목록 view 호출
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param response
	 * @return: response
	 * @변경이력 :
	 */
	@RequestMapping(value = "/restDeList")
	public ModelAndView mssrScheduleView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("cmmn/restDeList");
		return mav;
	}
	
	/**
	 * 
	 * @설명 : 휴일 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param request
	 * @return: response
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectRestDeList")
	public @ResponseBody ResponseResult selectrestDeList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		ResponseResult result = new ResponseResult();
		 LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		int rowPerPage = param.getInt("rowPerPage");
		
		int startRow = param.getInt("startRow");
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);	

		result = restDeService.selectRestDeList(param);

		return result;
	}
	
	/**
	 * 
	 * @설명 : 휴일  상세 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/selectRestDeItem")
	public @ResponseBody ResponseResult selectrestDeItem(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		ResponseResult result = new ResponseResult();
		result = restDeService.selectRestDeItem(param);
		return result;
	}
	
    
	/**
	 * 
	 * @설명 : 휴일  등록 팝업
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @변경이력 :
	 */
	
	
	@RequestMapping(value = "/pop/RestDeRegister") 
	public String restDeRegistPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception { 
		DataEntity param =  HttpUtil.getServletRequestParam(req);
		model.addAttribute("item", JsonUtils.objectToString(param));
		return"popup/popRestDeRegister"; 
	}
	
	/**
	 * 
	 * @설명 : 휴일   중복 체크  
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/restDeCheck")
	public @ResponseBody ResponseResult selectRestDeItem(HttpServletRequest req, HttpSession res){
	   DataEntity param = HttpUtil.getServletRequestParam(req); 
	    ResponseResult result = new ResponseResult();
		result = restDeService.selectRestDeCheck(param);
		return result;
	}
	 
	/**
	 * 
	 * @설명 : 휴일   등록 /수정 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @변경이력 :
	 */
	@RequestMapping(value = "/restDeSave", method = RequestMethod.POST)
	public @ResponseBody ResponseResult restDeSave(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
		return  restDeService.saveRestDe(param);
	}
	
	/**
	 * 
	 * @설명 : 휴일   삭제 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @변경이력 :
	 */
	@RequestMapping(value = "/restDeDelete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult deleteRestDe(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return  restDeService.deleteRestDe(param);
	}
	
	/**
	 * 
	 * @설명 : 휴일  스케줄조회 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/selectRestReveList")
	public @ResponseBody ResponseResult selectRestReveList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		ResponseResult result = new ResponseResult();
		 LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		result = restDeService.selectRestReveList(param);
		return result;
	}

}
