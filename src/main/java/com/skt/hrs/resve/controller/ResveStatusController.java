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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.cmmn.service.CodeService;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.resve.service.ResveStatusService;



/**
 * 
 * @FileName  : ResveController.java
 * @프로그램 설명   : 헬스케어 예약 시스템 컨트롤러
 * @Date      : 2019. 8. 27. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/resve")
@SuppressWarnings("unchecked")
public class ResveStatusController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResveStatusController.class);
	
	@Autowired
	ResveStatusService resveStatusService;
	
	@Autowired
	CodeService codeService;
	
	
	/**
	 * 
	 * @설명 : 예약현황 view 
	 * @작성일 : 2019.08.28
	 * @작성자 : P149365
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/status")
	public String resveStatusView(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		return "resve/resveStatus";
	}
	
	/**
	 * 
	 * @설명 : 예약등록 팝업 view
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/regist")
	public String resveRegistPopupView(HttpServletRequest req, HttpSession sess, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		
		//기존예약건 조회
		param.put("empno", loginVo.getEmpno());
		resveStatusService.selectDayCount(param);
				
		//블랙리스트 체크
		resveStatusService.checkBlacklist(param.getString("resveDe"), loginVo.getEmpno());
		
		return "popup/popResveRegist";
	}
	
	/**
	 * 
	 * @설명 : 대기등록 팝업 view 
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/wait")
	public String resveWaitPopupView(HttpServletRequest req, HttpSession sess, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");	
		
		//기존예약건 조회
		param.put("empno", loginVo.getEmpno());
		resveStatusService.selectDayCount(param);
		
		//블랙리스트 체크
		resveStatusService.checkBlacklist(param.getString("resveDe"), loginVo.getEmpno());
		
		return "popup/popResveWait";
	}
	
	/**
	 * 
	 * @설명 : 사후완료처리 팝업
	 * @작성일 : 2019.09.05
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/noshowConfirm")
	public String noshowConfirmPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
//		if(!DateUtil.getYYYYYMMDD().equals(param.getString("RESVE_DE"))) {
//			throw new HrsException("사후 완료처리는 당일만 가능합니다.");
//		}
		return "popup/popNoshowConfirm";
	}
	
	
	/**
	 * 
	 * @설명 : 예약/대기 취소 팝업 view 
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/cancel")
	public String resveCancelPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
		return "popup/popResveCancel";
	}
	
	/**
	 * 
	 * @설명 : 사용자의 이번달 예약/대기 건수 조회 
	 * @작성일 : 2019.08.29
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/monthCnt")
	public @ResponseBody ResponseResult selectMonthCount(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());
		return resveStatusService.selectMonthCount(param);
	}
	
	/**
	 * 
	 * @설명 : 사용자의 향후 2주간 예약/대기 건수 조회 
	 * @작성일 : 2019.09.18
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/2WeeksCnt")
	public @ResponseBody ResponseResult select2WeeksCount(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());
		return resveStatusService.select2WeeksCount(param);
	}
	
	/**
	 * 
	 * @설명 : 현황조회
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/getStatus")
	public @ResponseBody ResponseResult selectResveStatus(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		//param.put("empno", loginVo.getEmpno());
		return resveStatusService.selectResveStatus(param, loginVo);
	}
	
	/**
	 * 
	 * @설명 : 예약 등록 
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public @ResponseBody ResponseResult registResveStatus(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		
		logger.info("[REGIST RESVE]  => " + param.toString());
		
		// resveEmpno와 empno가 같지만, 타인의 예약 등을 대비하여 일단 분리한다.		
		param.put("resveEmpno", loginVo.getEmpno());	//예약자사번
		param.put("empno", loginVo.getEmpno());			//등록자사번
		param.put("resveSexdstn", loginVo.gettSex());	//예약자성별
		param.put("hrsAgree", loginVo.getHrsAgree());	//동의여부
		return resveStatusService.registResveStatus(param);
	}
	
	/**
	 * 
	 * @설명 : 대기 등록
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/wait", method = RequestMethod.POST)
	public @ResponseBody ResponseResult waitResveStatus(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		
		logger.info("[REGIST WAIT]  => " + param.toString());
		
		param.put("waitEmpno", loginVo.getEmpno());	//예약자사번
		param.put("empno", loginVo.getEmpno());			//등록자사번
		param.put("waitSexdstn", loginVo.gettSex());	//예약자성별
		param.put("hrsAgree", loginVo.getHrsAgree());	//동의여부
		return resveStatusService.waitResveStatus(param);
	}
	
	/**
	 * 
	 * @설명 : 예약/대기 취소 
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody ResponseResult cancelResveStatus(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());			//등록자사번
		
		logger.info("[CANCEL]  => " + param.toString());
		
		return resveStatusService.cancelResveStatus(param);
	}

	/**
	 * 
	 * @설명 : 완료처리 
	 * @작성일 : 2019.09.05
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult noshowConfirm(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		
		logger.info("[CONFIRM]  => " + param.toString());
		
		param.put("empno", loginVo.getEmpno());			//등록자사번
		return resveStatusService.completeResveStatus(param);
	}
}
