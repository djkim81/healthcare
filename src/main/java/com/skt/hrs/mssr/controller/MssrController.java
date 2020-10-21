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
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.mssr.service.MssrService;
import com.skt.hrs.utils.StringUtil;



/**
 * 
 * @FileName  : MssrController.java
 * @프로그램 설명   : 헬스케어 예약 시스템 - 관리사 MSSR(Messeur) 컨트롤러
 * @Date      : 2019. 9. 03. 
 * @작성자    : LEE.J.H
 * @변경이력  :
 */
@Controller
@RequestMapping(value = "/mssr")
public class MssrController {
	
	private static final Logger logger = LoggerFactory.getLogger(MssrController.class);
	
	@Autowired
	MssrService mssrService;	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 목록 view 호출
	 * @작성일 : 2019.09.03
	 * @작성자 : LEE.J.H
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/schedule")
	public ModelAndView mssrScheduleView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("mssr/schedule");
		return mav;
	}
	
	
	/**
	 * 
	 * @설명 : 관리사 콤보 리스트 조회
	 * @작성일 : 2019.09.04
	 * @작성자 : LEE.J.H
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/getMssrList")
	public @ResponseBody ResponseResult getMssrList(HttpServletRequest req) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		return mssrService.getMssrList(param);
	}
	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 리스트 조회
	 * @작성일 : 2019.09.05
	 * @작성자 : LEE.J.H
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectScheduleList")
	public @ResponseBody ResponseResult selectScheduleList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);

		int rowPerPage = param.getInt("rowPerPage");
		int startRow = param.getInt("startRow");
		
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);
		
		ResponseResult result = new ResponseResult();
		result = mssrService.selectScheduleList(param);

		int totalCount = mssrService.selectScheduleListTotalCount(param);
		result.addCustoms("totalCount", totalCount);

		return result;
	}
    
	/**
	 * 
	 * @설명 : 관리사 스케쥴  등록 팝업
	 * @작성일 : 2019.09.09
	 * @작성자 : P150113
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/pop/scheduleCreate")
	public String resveRegistPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
		return "popup/popScheduleCreate";
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴  등록
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param 
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/scheduleCreate", method = RequestMethod.POST)
	public @ResponseBody ResponseResult scheduleCreate(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return mssrService.insertSchedule(param);
	}

	/**
	 * 
	 * @설명 : 관리사 스케쥴  리스트 선택 삭제 
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param 
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/scheduleDelete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult scheduleDelete(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return mssrService.deleteResve(param);
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴  수정 팝업
	 * @작성일 : 2019.09.17
	 * @작성자 : P150113
	 * @param req
	 * @param 
	 * @return
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/pop/scheduleModify")
	public String resveConfirmPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {	
		DataEntity param = HttpUtil.getServletRequestParam(req);	
		
		model.addAttribute("item", JsonUtils.objectToString(param));
		return "popup/popScheduleModify";
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 수정 
	 * @작성일 : 2019.09.17
	 * @작성자 : P150113
	 * @param req
	 * @param 
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/scheduleModify", method = RequestMethod.POST)
	public @ResponseBody ResponseResult scheduleModify(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return mssrService.scheduleModify(param);
	}
	/**
	 * 
	 * @설명 : 관리사 스케쥴  상세 조회 
	 * @작성일 : 2019.09.26
	 * @작성자 : P149365
	 * @param req
	 * @param 
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectScheduleDetail")
	public @ResponseBody ResponseResult selectScheduleDetail(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		ResponseResult result = new ResponseResult();
		result = mssrService.selectScheduleDetail(param);

		return result;
	}
	

}
