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
import org.springframework.web.servlet.View;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.resve.service.ResveMgmtService;
import com.skt.hrs.resve.view.ResveMgmtExcelView;
import com.skt.hrs.utils.StringUtil;



/**
 * 
 * @FileName  	: ResveMgmtController.java
 * @프로그램 설명	: (관리자) 예약관리 화면
 * @Date		: 2019. 9. 23. 
 * @작성자		: P149365
 * @변경이력		:
 */
@Controller
@RequestMapping(value = "/resveMgmt")
public class ResveMgmtController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResveMgmtController.class);
	
	@Autowired
	ResveMgmtService resveMgmtService;
	
	/**
	 * 
	 * @설명 : 관리자 예약 정보 조회 및 변경 페이지 호출 
	 * @작성일 : 2019.09.23
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/list")
	public String resveMgmtView(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		return "resve/resveMgmt";
	}
	
	@RequestMapping(value = "/excel")
	public View resveMgmtExcelView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {	
		DataEntity param = HttpUtil.getServletRequestParam(req);
		ResponseResult result = new ResponseResult();
		result = resveMgmtService.selectResveMgmtListExcel(param);
        model.addAttribute("list", result.getList());
 
        return new ResveMgmtExcelView();

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
		ResponseResult result = resveMgmtService.selectResveMgmtDetailList(param);
		
		model.addAttribute("list", result.getList());
		return "popup/popDetailHist";
	}
	
	/**
	 * 
	 * @설명 : 예약/대기 수정 팝업 
	 * @작성일 : 2019.10.14
	 * @작성자 : P149365
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/modify")
	public String resveCancelPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);		
		model.addAttribute("item", resveMgmtService.selectResveMgmtItem(param).getItem());
		
		return "popup/popResveModify";
	}
	
	
	
	/**
	 * 
	 * @설명 : 관리자 예약 정보 조회
	 * @작성일 : 2019.09.23
	 * @작성자 : P149365
	 * @param request
	 * @param sess
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectResveMgmtList")
	public @ResponseBody ResponseResult selectResveMgmtList(HttpServletRequest request) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
				
		int rowPerPage = param.getInt("rowPerPage");
		int startRow = param.getInt("startRow");
		
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);
		
		ResponseResult result = new ResponseResult();
		result = resveMgmtService.selectResveMgmtList(param);

		int totalCount = resveMgmtService.selectResveMgmtListTotalCount(param);
		result.addCustoms("totalCount", totalCount);

		return result;
	}
	
	/**
	 * 
	 * @설명 : 예약/대기 상태 변경
	 * @작성일 : 2019.10.15
	 * @작성자 : P149365
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public @ResponseBody ResponseResult modifyResveStatus(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
						
		if(param.get("type") == null || StringUtil.isEmpty(param.getString("type"))) {
			throw new HrsException("error.invalidRequest", true);
		}
		
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("empno", loginVo.getEmpno());			//수정인사번
		param.put("auth", loginVo.getAuth());			//관리자가 취소(당일은 지난시간도 취소가능)
		
		logger.info("[MODIFY]  => " + param.toString());
		
		if("resve".equals(param.getString("type"))) {	// 예약변경				
			return resveMgmtService.modifyResveStatus(param);
		}else if("wait".equals(param.getString("type"))) {	// 대기변경
			return resveMgmtService.modifyWaitStatus(param);
		}else {
			throw new HrsException("error.invalidRequest", true);
		}
		
	}
	

}
