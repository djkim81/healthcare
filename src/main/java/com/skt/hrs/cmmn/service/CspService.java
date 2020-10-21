package com.skt.hrs.cmmn.service;


import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.contants.CspContents;
import com.skt.hrs.cmmn.dao.CspDAO;
import com.skt.hrs.cmmn.vo.CspVo;
import com.skt.hrs.utils.DateUtil;



@Service("cspService")
public class CspService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="cspDAO")
	private CspDAO cspDAO;

	@Resource(name="messageSource")
	MessageSource messageSource;
	
	
	/**
	 * 
	 * @설명 : MAIL 입력
	 * @작성일 : 2019.09.26
	 * @작성자 : P149365
	 * @param cspVo
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult insertCspMail(CspVo cspVo) {
		ResponseResult result = new ResponseResult();
		
		result.setItemOne(cspDAO.insertCspSend(cspVo));
		
		return result;
	}
	
	/**
	 * 
	 * @설명 : SMS입력
	 * @작성일 : 2019.09.06
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult insertCspSMS(Map param, String messageCode, Locale locale) {
		ResponseResult result = new ResponseResult();
		
		CspVo cspVo = new CspVo();
		String mssg = "";
		
		// 케어 30분전 알림
		if(messageCode.equals("csp.sms.resveNotify")) {
			//헬스케어 {0} {1}베드 시작 30분 전입니다.도착 후 본인 확인 바랍니다
			mssg =  messageSource.getMessage("csp.sms.resveNotify", new String[] {				
					param.get("BLD_NM").toString(),
					param.get("BED_NM").toString()
			}, locale);
			//mssg = messageSource.getMessage(mssg, null, locale);
	    	
		}else if (messageCode.equals("csp.sms.adminResveCancel")) {
			String resveDay = DateUtil.yyyymmdd2HumanReadable(param.get("RESVE_DE").toString());			
			mssg = messageSource.getMessage(messageCode, new String[] {
				resveDay
			}, locale);
		
		}else if (messageCode.equals("csp.sms.adminWaitCancel")) { 
			String resveDay = DateUtil.yyyymmdd2HumanReadable(param.get("RESVE_DE").toString());
			mssg = messageSource.getMessage(messageCode, new String[] {
				resveDay
			}, locale);
		}else{
			String resveDay = DateUtil.yyyymmdd2HumanReadable(param.get("RESVE_DE").toString());						
			mssg = messageSource.getMessage(messageCode, new String[] {
				resveDay,
				param.get("RESVE_TM_STR").toString()
			}, locale);
		}
		
		cspVo.setRcvEmpno(param.get("targetEmpno").toString());
		cspVo.setCspType(CspContents.SMS.toString());	//SMS
		cspVo.setResveNo(Integer.parseInt(param.get("RESVE_NO").toString()));		
		cspVo.setMssgBody(mssg);
		
		result.setItemOne(cspDAO.insertCspSend(cspVo));
		
		return result;
	}
	
	
	
	
	/**
	  * 
	 * @설명 : CSP 발송 리스트
	 * @작성일 : 2019.09.07
	 * @작성자 : djkim
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectCspSendList(){
		ResponseResult result = new ResponseResult();
		result.setItemList(cspDAO.selectCspSendList());
		return result;
	}
	
	public ResponseResult updateCspLog(String no, String sendRslt) {
		ResponseResult result = new ResponseResult();
		CspVo cspVo = new CspVo();
		cspVo.setNo(Integer.parseInt(no));
		cspVo.setSendRslt(sendRslt);
		
		result.setItemOne(cspDAO.updateCspSendLog(cspVo));
		
		return result;
	}
}