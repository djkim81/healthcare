package com.skt.hrs.mssr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.mssr.service.MssrblacklistService;



/**
 * 
 * @FileName  :  MssrblacklistController.java
 * @프로그램 설명   : No-show(블랙리스트) 관리
 * @Date      : 2019. 10. 11. 
 * @작성자    : YANG.H.R
 * @변경이력  :
 */
@Controller
@RequestMapping(value = "/mssr")
@SuppressWarnings("unchecked")
public class MssrblacklistController {
	
	private static final Logger logger = LoggerFactory.getLogger(MssrblacklistController.class);
	
	@Autowired
	MssrblacklistService  mssrblacklistService;
	
	
	
	/**
	 * 
	 * @설명 : No-show(블랙리스트) 목록 view 호출
	 * @Date      : 2019. 10. 11.
	 * @작성자    : YANG.H.R
	 * @변경이력 :
	 */
	@RequestMapping(value = "/mssrblacklist")
	public ModelAndView mssrMssrblacklistView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("mssr/mssrblacklist");
		return mav;
	}	
	
	/**
	 * 
	 * @설명 : No-show(블랙리스트) 리스트 조회
	 * @Date      : 2019. 10. 11.
	 * @작성자    : YANG.H.R
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectMssrblacklistList")
	public @ResponseBody ResponseResult selectMssrblacklistList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);

		int rowPerPage = param.getInt("rowPerPage");
		int startRow = param.getInt("startRow");
		String bldCode = param.getString("bldCode");
		String userName = param.getString("userName");
		
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);
		param.put("bldCode", bldCode);
		param.put("userName", userName);
		
		ResponseResult result = new ResponseResult();
		result = mssrblacklistService.selectMssrblacklistList(param);
		
		int totalCount = mssrblacklistService.selectMssrblacklistListCount(param);
		result.addCustoms("totalCount", totalCount);
		
		return result;
	}
	
	/**
	 * 
	 * @설명 : No-show(블랙리스트) 케어완료 상태로 변경
	 * @Date      : 2019. 10. 21.
	 * @작성자    : YANG.H.R
	 * @변경이력 :
	 */
	@RequestMapping(value = "/mssrblacklistcareDelete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult mssrblacklistDelete(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
		
		String resveEmpno = param.getString("resveEmpno");
		param.put("resveEmpno", resveEmpno);
		
		String rowData = param.getString("rowData");
		param.put("rowData", rowData);
		
		
		return mssrblacklistService.mssrblacklistcareDelete(param);
	}
	
	/**
	 * 
	 * @설명 : No-show(블랙리스트) No-Show 상태로 2주 패널티만 제외
	 * @Date      : 2019. 10. 21.
	 * @작성자    : YANG.H.R
	 * @변경이력 :
	 */
	@RequestMapping(value = "/mssrblacklistnoshowDelete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult mssrblacklistnoshowDelete(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		
		String resveEmpno = param.getString("resveEmpno");
		param.put("resveEmpno", resveEmpno);
		
		String rowData = param.getString("rowData");
		param.put("rowData", rowData);
		
		return mssrblacklistService.mssrblacklistnoshowDelete(param);
	}

}
