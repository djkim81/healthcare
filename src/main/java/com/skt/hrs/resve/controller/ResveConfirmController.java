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
import com.pub.core.util.JsonUtils;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.resve.service.ResveConfirmService;
import com.skt.hrs.resve.service.ResveStatusService;
import com.skt.hrs.utils.StringUtil;

/**
 * 
 * @FileName  : ResveController.java
 * @프로그램 설명   : 헬스케어 예약확인 시스템 컨트롤러
 * @Date      : 2019. 9. 10. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/confirm")
public class ResveConfirmController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResveConfirmController.class);
	
	@Autowired
	ResveConfirmService resveConfirmService;
	
	@Autowired
	ResveStatusService resveStatusService;

	/**
	 * 
	 * @설명 : 예약확인 view 
	 * @작성일 : 2019.09.10
	 * @작성자 : P149365
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "")
	public String resveConfirmView(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		return "resve/resveConfirm";
	}
	
	/**
	 * 
	 * @설명 :  케어시작 팝업호출
	 * @작성일 : 2019.09.11
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/start")
	public String resveConfirmPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {	
		DataEntity param = HttpUtil.getServletRequestParam(req);
		if(param.get("resveEmpno") == null && StringUtil.isEmpty(param.get("resveEmpno").toString())) {
			throw new HrsException("error.invalidRequest", true);
		}
		if(param.get("resveDe") == null && StringUtil.isEmpty(param.get("resveDe").toString())) {
			throw new HrsException("error.invalidRequest", true);
		}
		ResponseResult result = resveConfirmService.selectConfirmTarget(param);
		
		model.addAttribute("item", JsonUtils.objectToString(result.getItem()));
		return "popup/popStartConfirm";
	}
	
	/**
	 * 
	 * @설명 : 예약현황 조회
	 * @작성일 : 2019.09.11
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/getResve")
	public @ResponseBody ResponseResult selectResveConfirm(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		
		//공용사번 체크?
		//사옥 체크?
		
		return resveConfirmService.selectWorkList(param);
	}
	
	/**
	 * 
	 * @설명 : 완료처리
	 * @작성일 : 2019.09.11
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public @ResponseBody ResponseResult selectConfirmTarget(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());			//등록자사번		
		
		logger.info("[CONFIRM]  => " + param.toString());
		
		return resveStatusService.completeResveStatus(param);
	}
}
