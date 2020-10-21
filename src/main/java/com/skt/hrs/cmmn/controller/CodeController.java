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
import com.skt.hrs.cmmn.service.CodeService;
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
public class CodeController {
	
	private static final Logger logger = LoggerFactory.getLogger(CodeController.class);
	
	@Autowired
	CodeService codeService;
	
	
	/**
	 * @설명 : 공통코드 목록 조회
	 * @작성일 : 2019.08.28
	 * @작성자 : P149365
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/codeList")
	public @ResponseBody ResponseResult codeList(HttpServletRequest req) {
		DataEntity param = HttpUtil.getServletRequestParam(req);		
		return codeService.selectUserMenuList(param);
	}
	
	
	/**
	 * @설명 : 공통코드 전체 목록 조회
	 * @작성일 : 2019.08.30
	 * @작성자 : LEE.J.H
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/allCodeList")
	public @ResponseBody ResponseResult selectAllCodeList(HttpServletRequest req) {
		DataEntity param = HttpUtil.getServletRequestParam(req);		
		return codeService.selectAllCodeList(param);
	}
	
	/**
	 * @설명 : 공통코드관리 view 호출
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param request
	 * @param response
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/codeManage")
	public ModelAndView codeManageView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("cmmn/codeManage");
		return mav;
	}

	/**
	 * @설명 : 공통코드 종류 목록 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/codeKindList")
	public @ResponseBody ResponseResult codeKindList(HttpServletRequest req) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		return codeService.selectCodeKindList(param);
	}
	
	/**
	 * @설명 : 공통코드 리스트 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param request
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectCodeManageList")
	public @ResponseBody ResponseResult selectCodeManageList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);

		int rowPerPage = param.getInt("rowPerPage");
		int startRow = param.getInt("startRow");
		
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);
		
		ResponseResult result = new ResponseResult();
		result = codeService.selectCodeManageList(param);

		int totalCount = codeService.selectCodeManageListTotalCount(param);
		result.addCustoms("totalCount", totalCount);

		return result;
	}
    
	/**
	 * @설명 : 공통코드 등록 팝업
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param request
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/codeManageCreate")
	public String codeManageCreatePopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAllAttributes(param);
		return "popup/popCodeManageCreate";
	}
	
	/**
	 * @설명 : 공통코드 등록
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/insertCodeManage", method = RequestMethod.POST)
	public @ResponseBody ResponseResult insertCodeManage(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno());
		
		return codeService.insertCodeManage(param);
	}

	/**
	 * @설명 : 공통코드 목록 논리 삭제
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/updateDelYCodeManageList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult updateDelYCodeManageList(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno());
		
		return codeService.updateDelYCodeManageList(param);
	}
    
	/**
	 * @설명 : 공통코드 수정 팝업
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param request
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/pop/codeManageModify")
	public String codeManageModifyPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		model.addAttribute("item", JsonUtils.objectToString(param));
//		System.out.println("@@@@@@@@@@ \n" + JsonUtils.objectToString(param));
		return "popup/popCodeManageModify";
	}

	/**
	 * @설명 : 공통코드 목록 논리 삭제
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/updateCodeManageModify", method = RequestMethod.POST)
	public @ResponseBody ResponseResult updateCodeManageModify(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno());
		
		return codeService.updateCodeManageModify(param);
	}
	
	/**
	 * @설명 : 공통코드 목록 논리 삭제
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param req
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectCodeManageDuplicateCount", method = RequestMethod.POST)
	public @ResponseBody ResponseResult selectCodeManageDuplicateCount(HttpServletRequest req, HttpServletResponse res) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		ResponseResult result = new ResponseResult();
		System.out.println("@@@@ param :"+ param);
		int duplCnt = codeService.selectCodeManageDuplicateCount(param);
		result.addCustoms("duplCnt", duplCnt);
		System.out.println("@@@@duplCnt : "+duplCnt);
		return result;
	}

}
