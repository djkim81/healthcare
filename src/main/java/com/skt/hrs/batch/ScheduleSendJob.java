package com.skt.hrs.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.service.ScheduleService;
import com.skt.hrs.cmmn.vo.ScheduleVo;
import com.skt.hrs.utils.HttpClientUtils;
import com.skt.hrs.utils.JsonUtils;
import com.skt.hrs.utils.StringUtil;


public class ScheduleSendJob {
	
	Logger logger = LoggerFactory.getLogger("scheduleSendJobLogger");
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Value("#{prop['OUTLOOK.SCHEDULE.URL']}")
	private String outlookUrl;
	
	protected void execute() {
		String opt = System.getProperty("hrs.batch");
		//String opt = "true";
		if(opt != null && opt.equals("true")) {
			try {
				int undeliveredOutlookSendCnt = 0;  //일정 등록 건수
				int undeliveredOullookCancelCnt = 0;//일정 취소 건수
				
				// 아웃룩 일정 등록 처리(예약완료건)
				List<Map<String,String>> undeliveredOutlookSendList = scheduleService.selectScheduleSendList().getList();
				undeliveredOutlookSendCnt = undeliveredOutlookSendList.size();
				for(Map<String,String> outlook : undeliveredOutlookSendList ) {
					
					ResponseResult result = new ResponseResult();	
					outlook.put("type", "N");
					
					Map<String,String> param = new HashMap<>();
					param.put("paramType", "xml");
					param.put("paramXml", makeXmlData(outlook));
					
					result = HttpClientUtils.httpPostDataString(outlookUrl+"?type=N", param);
					
					//http post 요청이 성공한 경우
					if(result.getMessageCode() =="success" && !"".equals(result.getItem())) {
						
						String returnJsonStr = JsonUtils.xmlStringToJsonString(result.getItem());
						
						Map<String,Object> jsonMap = JsonUtils.stringToJsonMap(returnJsonStr);
						
						Map<String,Object> resultMap = (Map<String, Object>)jsonMap.get("result");
						Map<String,Object> itemMap = (Map<String, Object>)resultMap.get("item");
						
						ScheduleVo scheduleVo = new ScheduleVo();
						
						//정상 등록된 경우
						//if(  itemMap.get("response").toString() =="true") {
						if("true".equals(String.valueOf(itemMap.get("response")))) {
							scheduleVo.setNo(Integer.parseInt(String.valueOf(outlook.get("NO"))));
							scheduleVo.setSendYn("Y");
							scheduleVo.setoId(itemMap.get("Id").toString());
							scheduleVo.setoChangekey(itemMap.get("changeKey").toString());
							scheduleVo.setSendRslt(itemMap.get("response").toString());
							
							//로그 저장
							scheduleService.updateScheduleSendLog(scheduleVo);
							
						}
						//등록 시 실패난 경우
						else {
							scheduleVo.setNo(Integer.parseInt(String.valueOf(outlook.get("NO"))));
							scheduleVo.setSendRslt("false");
							
							//로그 저장
							scheduleService.updateScheduleSendLog(scheduleVo);
						}
					}
					//http post 요청이 실패난 경우
					else {
						ScheduleVo scheduleVo = new ScheduleVo();
						scheduleVo.setNo(Integer.parseInt(String.valueOf(outlook.get("NO"))));
						scheduleVo.setSendRslt("false");
						
						//로그 저장
						scheduleService.updateScheduleSendLog(scheduleVo);
					}
					
				}
				
				// 아웃룩 일정 취소 처리(예약취소건)
				List<Map<String,String>> undeliveredOutlookCancelList = scheduleService.selectScheduleCancelList().getList();
				undeliveredOullookCancelCnt = undeliveredOutlookCancelList.size();
				for(Map<String,String> outlook : undeliveredOutlookCancelList ) {
					
					ResponseResult result = new ResponseResult();	
					outlook.put("type", "C");
					
					Map<String,String> param = new HashMap<>();
					param.put("paramType", "xml");
					param.put("paramXml", makeXmlData(outlook));
					
					result = HttpClientUtils.httpPostDataString(outlookUrl+"?type=C", param);
					
					//http post 요청이 성공한 경우
					if(result.getMessageCode() =="success" && !"".equals(result.getItem())) {
						
						String returnJsonStr = JsonUtils.xmlStringToJsonString(result.getItem());
						
						Map<String,Object> jsonMap = JsonUtils.stringToJsonMap(returnJsonStr);
						
						Map<String,Object> resultMap = (Map<String, Object>)jsonMap.get("result");
						Map<String,Object> itemMap = (Map<String, Object>)resultMap.get("item");
						
						ScheduleVo scheduleVo = new ScheduleVo();
						
						//정상 등록된 경우
						if("true".equals(String.valueOf(itemMap.get("response")))) {
							scheduleVo.setNo(Integer.parseInt(String.valueOf(outlook.get("NO"))));
							scheduleVo.setCanclYn("Y");
							scheduleVo.setCanclRslt(itemMap.get("response").toString());
							
							//로그 저장
							scheduleService.updateScheduleCancelLog(scheduleVo);
							
						}
						//등록 시 실패난 경우
						else {
							scheduleVo.setNo(Integer.parseInt(String.valueOf(outlook.get("NO"))));
							scheduleVo.setCanclRslt("false");
							
							//로그 저장
							scheduleService.updateScheduleCancelLog(scheduleVo);
						}
					}
					//http post 요청이 실패난 경우
					else {
						ScheduleVo scheduleVo = new ScheduleVo();
						scheduleVo.setNo(Integer.parseInt(String.valueOf(outlook.get("NO"))));
						scheduleVo.setCanclRslt("false");
						
						//로그 저장
						scheduleService.updateScheduleCancelLog(scheduleVo);
					}
					
				}
				

				logger.info(  "{} OUTLOOK {} : 전체: {}, 일정등록: {}, 일정취소: {} ",
						ScheduleSendJob.class.getSimpleName(), 
						"일정 등록 결과", 
						(undeliveredOutlookSendCnt+undeliveredOullookCancelCnt),
						undeliveredOutlookSendCnt, 
						undeliveredOullookCancelCnt);
				
				logger.info(":::::{} ScheduleSendJob job {} ", 
						ScheduleSendJob.class.getSimpleName(),
						"실행에 성공하였습니다" + " ::::: ");
				
			}catch(Exception e) {
				logger.error("{} ScheduleSendJob Error Message : {} ", ScheduleSendJob.class.getSimpleName(), e.getMessage());
			}
		}
		
		
	}
	
	//요청 xml 파라미터 생성
	public String makeXmlData(Map<String,String> map) {
		String returnXml;
		if("N".equals(map.get("type"))){
			String newXml;
			newXml =  "<?xml version='1.0' ?>";
			newXml += "<request>";
			newXml += "<meeting>";
			newXml += "<sender><![CDATA["+map.get("RESVE_EMPNO").toString()+"]]></sender>";
			newXml += "<sequence><![CDATA["+String.valueOf(map.get("RESVE_NO"))+"]]></sequence>";
			newXml += "<subject><![CDATA[헬스케어 예약]]></subject>";
			newXml += "<requiredAttendees><![CDATA["+map.get("EMAIL").toString()+"]]></requiredAttendees>";
			newXml += "<optionalAttendees><![CDATA[]]></optionalAttendees>";
			newXml += "<startTime><![CDATA["+StringUtil.getStartTime(map.get("RESVE_DE").toString(),map.get("TIME").toString())+"]]></startTime>";
			newXml += "<endTime><![CDATA["+StringUtil.getEndTime(map.get("RESVE_DE").toString(),map.get("TIME").toString())+"]]></endTime>";
			newXml += "<location><![CDATA["+map.get("LOCATION").toString()+"]]></location>";
			newXml += "<content><![CDATA[]]></content>";
			newXml += "</meeting>";
			newXml += "</request>";
			
			returnXml = newXml;
		}else {
			String cancelXml;
			cancelXml =  "<?xml version='1.0'?>";
			cancelXml += "<request>";
			cancelXml += "<meeting>";
			cancelXml += "<sender><![CDATA["+map.get("RESVE_EMPNO").toString()+"]]></sender>";
			cancelXml += "<sequence><![CDATA["+String.valueOf(map.get("RESVE_NO"))+"]]></sequence>";
			cancelXml += "<id><![CDATA["+map.get("O_ID").toString()+"]]></id>";
			cancelXml += "<changeKey><![CDATA["+map.get("O_CHANGEKEY").toString()+"]]></changeKey>";
			cancelXml += "</meeting>";
			cancelXml += "</request>";
			
			returnXml = cancelXml;
		}
		return returnXml;
	}
}
