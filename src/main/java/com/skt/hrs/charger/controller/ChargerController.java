package com.skt.hrs.charger.controller;

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
/*import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.pub.core.util.JsonUtils;
import com.skt.hrs.charger.service.ChargerService;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.utils.StringUtil;
/*import com.skt.hrs.utils.ExcelExport;*/


/**
 * 
 * @FileName  : ChargerController.java
 * @프로그램 설명   : 헬스케어 예약 시스템 - 매니저 charger(charger) 컨트롤러
 * @Date      : 2019. 10. 03. 
 * @작성자    : P150113
 * @변경이력  :
 */
@Controller
@RequestMapping(value = "/charger")
public class ChargerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChargerController.class);
	
	@Autowired
	ChargerService chargerService;
	
	/**
	 * 
	 * @설명 : 담당자 스케쥴 목록 view 호출
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param response
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/chargerList")
	public ModelAndView ChargerView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("charger/chargerList");
		return mav;
	}
	
	
	/**
	 * 
	 * @설명 : 담당자  리스트 조회 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	@RequestMapping(value = "/selectChargerList")
	public @ResponseBody ResponseResult selectChargerList(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		ResponseResult result = new ResponseResult();
		 LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		int rowPerPage = param.getInt("rowPerPage");
		
		int startRow = param.getInt("startRow");
		param.put("rowPerPage", rowPerPage);
		param.put("startRow", startRow);	

		result = chargerService.selectChargerList(param);

		return result;
	}
	
	

	/**
	 * 
	 * @설명 : 담당자  상세 조회 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param request
	 * @return
	 * @throws Exception
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/selectChargerItem")
	public @ResponseBody ResponseResult selectChargerItem(HttpServletRequest request, HttpSession sess) throws Exception {
		DataEntity param = HttpUtil.getServletRequestParam(request);
		ResponseResult result = new ResponseResult();
		result = chargerService.selectChargerItem(param);
		return result;
	}
	
    
	/**
	 * 
	 * @설명 : 담당자   등록 팝업
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param : request
	 * @return: response
	 * @throws Exception
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/pop/ChargerRegister") 
	public String ChargerRegistPopupView(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception { 
		DataEntity param =  HttpUtil.getServletRequestParam(req);
		model.addAttribute("item", JsonUtils.objectToString(param));
		return"popup/popChargerRegister"; 
	}
	
	/**
	 * 
	 * @설명 : 담당자   중복 체크  
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param request
	 * @param Exception
	 * @return
	 * @변경이력 :
	 */
	
	@RequestMapping(value = "/chargerEmpNoCheck")
	public @ResponseBody ResponseResult selectEmpNoCheck(HttpServletRequest req, HttpSession res){
	  DataEntity param = HttpUtil.getServletRequestParam(req); 
	  
	  return chargerService.selectEmpNoCheck(param);
	}
	 
	
	/**
	 * 
	 * @설명 : 담당자   등록 /수정 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/chargerSave", method = RequestMethod.POST)
	public @ResponseBody ResponseResult chargerSave(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return  chargerService.chargerSave(param);
	}
	
	/**
	 * 
	 * @설명 : 담당자   리스트 선택 삭제 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param req
	 * @param sess
	 * @return
	 * @변경이력 :
	 */
	@RequestMapping(value = "/chargerDelete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult deleteCharger(HttpServletRequest req, HttpSession sess) {
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		param.put("regEmpNo", loginVo.getEmpno()); //등록자사번
				
		return  chargerService.deleteCharger(param);
	}
	

}
