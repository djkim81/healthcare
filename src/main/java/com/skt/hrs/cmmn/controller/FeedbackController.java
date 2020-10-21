package com.skt.hrs.cmmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.cmmn.contants.CspContents;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.cmmn.service.CspService;
import com.skt.hrs.cmmn.vo.CspVo;
import com.skt.hrs.cmmn.vo.LoginVo;
import com.skt.hrs.utils.StringUtil;

/**
 * 
 * @FileName  : FeedbackController.java
 * @프로그램 설명   : 문의/신고하기 컨트롤러
 * @Date      : 2019. 9. 26. 
 * @작성자      : 
 * @변경이력    :
 */
@Controller
@RequestMapping(value = "/")
public class FeedbackController {

	@Autowired
	CspService cspService;
	
	@Value("#{prop['FEEDBACK.RCVEMPNO']}")
	private String feecbackRcvEmpnos;
	
	@RequestMapping(value = "/pop/feedback")
	public String feedBackPopupView(HttpServletRequest req, HttpSession sess, Model model) throws Exception {		
		return "popup/popFeedback";
	}
	
	@RequestMapping(value = "feedback", method = RequestMethod.POST)
	public  @ResponseBody ResponseResult registFeedback(HttpServletRequest req, HttpSession sess) {
		
		
		DataEntity param = HttpUtil.getServletRequestParam(req);
		LoginVo loginVo = (LoginVo) sess.getAttribute("LoginVo");
		
		String[] rcvEmpnos = feecbackRcvEmpnos.split(",");
		
		int sendCount = 0;
		for(String empno : rcvEmpnos) {
			if(param.get("contents") == null) {
				throw new HrsException("error.invalidRequest", true);
			}
			String content = param.get("contents").toString();
			content = StringUtil.cleanXSS(content);
			content = StringUtil.escapeNewlineToBr(content);
			
			CspVo vo = new CspVo();
			vo.setSendEmpno(loginVo.getEmpno());
			vo.setCspType(CspContents.MAIL.toString());

			vo.setRcvEmpno(empno);	// properties에 등록된 3명 (여러명은 어떻게?)
			//FIXME: 메세지 처리
			vo.setMssgHead("[헬스케어예약시스템] 문의/오류 접수 메일");
			vo.setMssgBody(content);
			
			if((boolean) cspService.insertCspMail(vo).getItem()) {
				sendCount++;
			}
		}
		ResponseResult result = new ResponseResult();
		result.setItemOne(sendCount);
		
		return result;
	}
}
