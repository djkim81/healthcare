package com.skt.hrs.batch;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.skt.hrs.cmmn.contants.CspContents;
import com.skt.hrs.cmmn.csp.CspSender;
import com.skt.hrs.cmmn.csp.vo.ResultVo;
import com.skt.hrs.cmmn.service.CspService;

/**

 */
@Configuration
public class CspSendJob {

	private final String CSP_TYPE = "CSP_TYPE";

	Logger logger = LoggerFactory.getLogger("cspSendJobLogger");

	@Autowired
	private CspService service;
	
	@Value("#{prop['CSP.SMS.CONSUMER_ID']}")
	private String CONSUMER_ID;
	
	@Value("#{prop['CSP.SMS.SENDER_NO']}")
	private String SENDER_NUM;
	
	/**
	 */
	protected void execute() {
		String opt = System.getProperty("hrs.batch");
		if(opt != null && opt.equals("true")) {
			try {
				
				int undeliveredTotalCnt = 0; // 전체 토탈
				int undeliveredSmsCnt = 0;
				int undeliveredMailCnt = 0;
				int undeliveredMemoCnt = 0;
				
				List<Map<String, String>> list = service.selectCspSendList().getList();
				CspSender sender = null;
				undeliveredTotalCnt = list.size(); // 전체 토탈
				
				// 미전송된 SMS 목록
				List<Map<String, String>> undeliveredMessages 	= getUndeliveredDatas(list, CspContents.SMS);
				// 미전송된 메일 목록
				List<Map<String, String>> undeliveredMails 		= getUndeliveredDatas(list, CspContents.MAIL);
				// 미전송된 쪽지 목록
				List<Map<String, String>> undeliveredMemos 		= getUndeliveredDatas(list, CspContents.MEMO);
	
				sender = new CspSender(CspContents.SMS.value());
				//final String CONSUMER_ID = "C00479";
				for (Map<String, String> sms : undeliveredMessages) {
					String[][] paramLt = { 
						 {"CONSUMER_ID"		, CONSUMER_ID } // 서비스 ID (*필수값)
						,{"RPLY_PHON_NUM"	, SENDER_NUM } //발신전화번호 (*필수값)
						,{"TITLE"			, sms.get("MSSG_BODY") } //내용 (*필수값)
						,{"PHONE"			, sms.get("MOVETELNO") } // 수신전화 (*필수값)
					};
					String[] contLt = { "RETURN" };
					sender.setParamLt(paramLt);
					sender.setContLt(contLt);
					ResultVo vo = sender.send();
					service.updateCspLog(String.valueOf(sms.get("NO")), vo.getHEADER().RESULT );
					undeliveredSmsCnt++;
					
					logger.info("==={} : {} " , 
							"CSP SMS 전송후 결과 코드", 
							vo.getHEADER().RESULT_CODE);
					
				}
				
				// 미전송된 메일 목록, 전송 처리
				sender = new CspSender(CspContents.MAIL.value());
				for (Map<String, String> mails : undeliveredMails) {
					String[][] paramLt = { 
						 {"SENDEREMAIL"		, mails.get("SEND_EMAIL") }
						,{"RECEIVEREMAIL"	, mails.get("RCV_EMAIL") }
						,{"SUBJECT"			, mails.get("MSSG_HEAD") }
						,{"CONTENT"			, mails.get("MSSG_BODY") }
					};
					String[] contLt = { "RETURN" };
					sender.setParamLt(paramLt);
					sender.setContLt(contLt);
					ResultVo vo = sender.send();
					service.updateCspLog(String.valueOf(mails.get("NO")), vo.getHEADER().RESULT );
					undeliveredMailCnt++;
					
					
					logger.info("==={} : {} " , 
							"CSP MAIL 전송후 결과 코드", 
							vo.getHEADER().RESULT_CODE);
				}
				
				
				logger.info("==={} {} : {} " ,
						CspSendJob.class.getSimpleName(), 
						"미전송된 총 목록", 
						undeliveredTotalCnt);
	
				logger.info(  "{} SMS {} : {} Transfer: {} Untransfer: {} ",
						CspSendJob.class.getSimpleName(), 
						"미전송된 총 목록", 
						undeliveredMessages.size(),
						undeliveredSmsCnt, 
						(undeliveredMessages.size() - undeliveredSmsCnt) );
				
				logger.info(  "{} MAIL {} : {} Transfer: {} Untransfer: {} ",
						CspSendJob.class.getSimpleName(), 
						"미전송된 총 목록", 
						undeliveredMails.size(),
						undeliveredMailCnt, 
						(undeliveredMails.size() - undeliveredMailCnt));
	
				logger.info(":::::{} SMS/MAIL job {} ", 
						CspSendJob.class.getSimpleName(),
						"실행에 성공하였습니다" + " ::::: ");
				
	
			} catch (Exception e) {
				logger.error("{} MEMO/SMS/MAIL Error Message : {} ", CspSendJob.class.getSimpleName(), e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @설명 : 메일/쪽지/SMS 데이터 반환
	 * @작성일 : 2019.09.07
	 * @작성자 : djkim
	 * @param list DB에서 가져온 전체 미전송 목록
	 * @param enumKey 메일, 쪽지, SMS 구분 키
	 * @return
	 * @변경이력 :
	 */
	public List<Map<String, String>> getUndeliveredDatas(List<Map<String, String>> list,  CspContents enumKey) {
		return list.stream()
				.filter( obj -> obj.get(CSP_TYPE).equals( enumKey.keyStrValue() ) )
				.collect(Collectors.toList());
		
	}

}
