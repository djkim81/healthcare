package com.skt.hrs.mssr.controller;

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
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.mssr.service.HealthkeperService;



/**
 * 
 * @FileName  :  HealthkeperController.java
 * @프로그램 설명   : 헬스케어 예약 시스템 - 헬스키퍼 컨트롤러
 * @Date      : 2019. 9. 20. 
 * @작성자    : YANG.H.R
 * @변경이력  :
 */
@Controller
@RequestMapping(value = "/mssr")
@SuppressWarnings("unchecked")
public class HealthkeperController {
	
	private static final Logger logger = LoggerFactory.getLogger(HealthkeperController.class);
	
	@Autowired
	HealthkeperService  healthkeperService;
	
	
	
	/**
	 * 
	 * @설명 : 헬스키퍼 목록 view 호출
	 * @Date      : 2019. 9. 20. 
	 * @작성자    : YANG.H.R
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/healthkeper")
	public ModelAndView mssrHealthkeperView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("mssr/healthkeper");
		return mav;
	}	
	
	/**
	 * 
	 * @설명 : 헬스키퍼 등록
	 * @Date      : 2019. 9. 30. 
	 * @작성자    : YANG.H.R
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/healthkeperCreate", method = RequestMethod.POST)
	public @ResponseBody ResponseResult healthkeperCreate(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return healthkeperService.insertHealthkeper(param);
	}
	
	
	/**
	 * 
	 * @설명 : 헬스키퍼 리스트 조회
	 * @Date      : 2019. 9. 20. 
	 * @작성자    : YANG.H.R
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectHealthkeperList")
	public @ResponseBody ResponseResult selectHealthkeperList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);

		int rowPerPage = param.getInt("rowPerPage");
		int startRow = param.getInt("startRow");
		String bldCode = param.getString("bldCode");
		String hffc_yn = param.getString("hffc_yn");
		
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);
		param.put("bldCode", bldCode);
		param.put("hffc_yn", hffc_yn);
		
		ResponseResult result = new ResponseResult();
		result = healthkeperService.selectHealthkeperList(param);

		int totalCount = healthkeperService.selectHealthkeperListTotalCount(param);
		result.addCustoms("totalCount", totalCount);

		return result;
	}
    
	
	/**
	 * 
	 * @설명 : 헬스키퍼 등록 팝업
	 * @Date      : 2019. 9. 26. 
	 * @작성자    : YANG.H.R
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/pop/healthkeperCreate")
	public String healthkeperRegistPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
		return "popup/popHealthkeperCreate";
	}
	
	/**
	 * 
	 * @설명 : 헬스키퍼 수정
	 * @Date      : 2019. 10. 01. 
	 * @작성자    : YANG.H.R
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/pop/healthkeperModify")
	public String healthkeperConfirmPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {	
		DataEntity param = HttpUtil.getServletRequestParam(req);	
		
		model.addAttribute("item", JsonUtils.objectToString(param));
		return "popup/popHealthkeperModify";
	}
	
	/**
	 * 
	 * @설명 : 헬스키퍼 수정등록
	 * @Date      : 2019. 10. 01. 
	 * @작성자    : YANG.H.R
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/healthkeperModify", method = RequestMethod.POST)
	public @ResponseBody ResponseResult healthkeperModify(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return healthkeperService.healthkeperModify(param);
	}

}
