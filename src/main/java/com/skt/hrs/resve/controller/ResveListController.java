package com.skt.hrs.resve.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.resve.service.ResveListService;



/**
 * 
 * @FileName  : ResveListController.java
 * @프로그램 설명   : 헬스케어 예약 시스템 컨트롤러
 * @Date      : 2019. 8. 29. 
 * @작성자    : LEE.J.H
 * @변경이력  :
 */
@Controller
@RequestMapping(value = "/resve")
public class ResveListController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResveListController.class);
	
	@Autowired
	ResveListService resveListService;
	
	
	/**
	 * 
	 * @설명 : 예약현황리스트 view 호출
	 * @작성일 : 2019.08.29
	 * @작성자 : LEE.J.H
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/list")
	public ModelAndView resveStatueView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("resve/resveList");
		return mav;
	}
	
	/**
	 * 
	 * @설명 : 예약 상세현황 조회 
	 * @작성일 : 2019.09.18
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/detailHist")
	public String detailHistPopupView(HttpServletRequest req, HttpServletResponse res, HttpSession sess, Model model) throws Exception {	
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());
		ResponseResult result = resveListService.selectResveDetailList(param);
		
		model.addAttribute("list", result.getList());
		return "popup/popDetailHist";
	}
	
	
	/**
	 * 
	 * @설명 : 예약현황리스트 조회
	 * @작성일 : 2019.08.29
	 * @작성자 : LEE.J.H
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectResveList")
	public @ResponseBody ResponseResult selectResveList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empNo", loginVo.getEmpno());
		//param.put("empNo", "P149080"); //테스트를 위해 사번 하드코딩

		int rowPerPage = param.getInt("rowPerPage");
		int startRow = param.getInt("startRow");
		
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);
		
		ResponseResult result = new ResponseResult();
		result = resveListService.selectResveList(param);

		int totalCount = resveListService.selectResveListTotalCount(param);
		result.addCustoms("totalCount", totalCount);

		return result;
	}
	

}
