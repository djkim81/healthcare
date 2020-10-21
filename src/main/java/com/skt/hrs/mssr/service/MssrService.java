package com.skt.hrs.mssr.service;


import java.text.SimpleDateFormat;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.contants.ResveStatusConst;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.cmmn.service.CspService;
import com.skt.hrs.cmmn.service.ScheduleService;
import com.skt.hrs.mssr.dao.MssrDAO;
import com.skt.hrs.utils.DateUtil;
import com.skt.hrs.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.pub.core.util.JsonUtils;





@Service("mssrService")
public class MssrService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="mssrDAO")
	private MssrDAO mssrDAO;
	
	@Autowired
	private CspService cspService;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	private ScheduleService scheduleService;

	/**
	 * 
	 * @설명 : 관리사 콤보박스  리스트  조회
	 * @작성일 : 2019.09.04
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult getMssrList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(mssrDAO.getMssrList(param));
		return result;
	}
	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 리스트 조회
	 * @작성일 : 2019.09.05
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectScheduleList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(mssrDAO.selectScheduleList(param));
		return result;
	}
	
	
	/**
	 * 
	 * @설명 : 관리사 근무시간 상세조회
	 * @작성일 : 2019.09.05
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectScheduleDetail(DataEntity param) {
		ResponseResult result = new ResponseResult();
		 param.put("resveTime",null);
		result.setItemList(mssrDAO.selectScheduleDetail(param));
		return result;
	}
  
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 리스트 TOTAL COUNT 조회
	 * @작성일 : 2019.09.05
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectScheduleListTotalCount(DataEntity param) {
		int result = mssrDAO.selectScheduleListTotalCount(param);
		return result;
	}


	/**
	 * 
	 * @설명 : 관리사 스케쥴 동록
	 * @작성일 : 2019.09.16
	 * @작성자 : P150113
	 * @paramType: jsonFromatString 
	 * @param :[{startDate:20190901,endDate:20190910,startTime:1,endTime:3 ,bldcode:SK01,bedcode:A,mssrno:M0000001,resveTime:1},{...,resveTime:2},{...,resveTime:3}] 
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult insertSchedule(DataEntity param) {
		ResponseResult result = new ResponseResult();  
		DataEntity paramsMap = new DataEntity();  // 
		DataEntity chkDataMap = new DataEntity(); // 중복 자료 확인을 위한 paramMaP
		//jsonString -> arrayList<Map> 
		ArrayList<HashMap<String, String>> getListItems = (ArrayList<HashMap<String, String>>)JsonUtils.stringToJsonClass(param.getString("params"), ArrayList.class);
		String startDate ="", endDate = "", workDate="";
		long days = 0;
		int chk =0 , timeDup = 0, bedUse = 0;
	    Map restDateMap = null; 
		String [] restDates = null;
	    paramsMap.putAll(getListItems.get(0));
		chkDataMap.put("RESVE_CHECK","");
		chkDataMap.put("START_DATE",paramsMap.getString("startDate"));
		
		boolean  insertResult = false;
        for (int i = 0; i < getListItems.size(); i++) {
        	paramsMap.putAll(getListItems.get(i));
        	startDate = paramsMap.getString("startDate");
        	endDate = paramsMap.getString("endDate");
        	paramsMap.put("regEmpNo",param.getString("regEmpNo"));
        	days = DateUtil.getDateDiff(startDate, endDate ); // 근무생성 일수 계산  
        	chkDataMap.put("startDate", startDate.replaceAll("-", "")); // 기간내 휴일 체크를 위한 시작일 셋
        	chkDataMap.put("endDate", endDate.replaceAll("-", ""));  // 기간내 휴일 체크를 위한 종료일 셋
        	restDateMap = mssrDAO.selectRestCheck(chkDataMap);  // 기간내 휴일 리스트  조회  return : 20191003,20191008       	
        	restDates = restDateMap.get("REST_DATES").toString().split(",");//휴일 일자별 배열 생성         	
        	for (int j = 0; j <= days; j++) {  //근무일 만큼 foa 문 실행 
        		 workDate = DateUtil.getDateAdd(startDate,j); //근무일 가져오기  startDate+j
        		 //근무일과 휴일이 같으면  근무 들록  제외 
        		 if(DateUtil.isWeekend(workDate,"yyyyMMdd")||Arrays.asList(restDates).contains(workDate.replaceAll("-", ""))) {
            		 continue;
            	 };
        		paramsMap.put("sttusCode",ResveStatusConst.DBSTATUS.WORK.toString());
             	paramsMap.put("fromDate",startDate.replaceAll("-", "")); 
             	paramsMap.put("toDate",endDate.replaceAll("-", ""));	
             	paramsMap.put("resveDate",workDate.replaceAll("-", ""));
             	
             	dupChk(paramsMap , chkDataMap); // 증북자료 체크 
             	chk = chkDataMap.getInt("RESVE_CHECK"); //  동일헬스키퍼로 등록된 근무있음 :10 ,다른 헬스키퍼 로 등록된 근무 있읍 :20, 두가지사항 모두 해당 :30 
	            //상황별 중복건수 체크 하기 위한   조건문  
             	if(chk == 10) { 
	            	timeDup= timeDup +1; 
				 }
	             else if(chk==20){
	            	 bedUse= bedUse +1;
				 } 
	             else if(chk==30){
					 bedUse= bedUse +1;
					 timeDup= timeDup +1; 
				 }
        	     //resve_sttus 테이블  근무등록
	            insertResult = mssrDAO.insertSchedule(paramsMap); 
				
				if(!(insertResult)) { 
					throw new HrsException("error.processFailure", true);
				 }
				
				//resve_hist 테이블  근무상태 등록 
				insertResult = mssrDAO.insertResveHist(paramsMap); 
				if(!(insertResult)) { 
					throw new HrsException("error.processFailure", true);
				 } 
          }
        }
    	 // 중복 상황별 오루 메세지 출력을 위한 조건문 
    	 if(timeDup >0) {  // 돌인인 근무 중복 
    		 String message = messageSource.getMessage("error.mssr.duplicateDate", new String[] {
    				 chkDataMap.getString("START_DATE"), //근무시작일 
    				 chkDataMap.getString("END_DATE"), // 근무 종료일 
    				
 			}, Locale.forLanguageTag(param.getString("_ep_locale")));	
    		 throw new HrsException(message);
    		 
    	 }else if(bedUse > 0) { //다른 헬스키퍼 근무 중복 
    		 String message = messageSource.getMessage("error.mssr.duplicateBed", new String[] {
    				 chkDataMap.getString("END_DATE"), //중복 근무일
    				 chkDataMap.getString("MSSR_NCNM"),//이미 근무등록된 헬스키퍼명 
    				 chkDataMap.getString("BED_CODE"), // 베드명 
    				
 			}, Locale.forLanguageTag(param.getString("_ep_locale")));	
    		 throw new HrsException(message); 
    	 }
    	  
		result.setItemOne(insertResult);
		// data적용 성공여부
		return result;
	}
	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 수정
	 * @작성일 : 2019.09.16
	 * @작성자 : P150113
	 * @param :{resveDate:20190901,bldcode:SK01,bedcode:A,mssrno:M0000001, insertTime:1,3, deleteTime: 2,4} 
	 * @return
	 * @변경이력 :
	 * @etc : 헬스키퍼 스케줄 수정일 일단위로 만 수정  가능 
	 */
	@Transactional
	public ResponseResult scheduleModify(DataEntity param) {
		ResponseResult result = new ResponseResult();
	    boolean  insertResult = false;
	    DataEntity chkDataMap = new DataEntity(); // 중복 체크 를 위한 parma Map
	    String[] InsertTime = param.split("insertTime",",");  //신규 근무 시간 배열생성  
		String[] DeleteTime = param.split("deleteTime",",");  //삭제 근무 시간 배열생성
		int chk =0 , timeDup = 0, bedUse = 0;
        //HashMap<String,String > paramsMap = new HashMap<String,String >() ;
        param.remove("insertTime");
        param.remove("deleteTime");
        chkDataMap.put("RESVE_CHECK","");
		chkDataMap.put("START_DATE",param.getString("resveDate"));
		chkDataMap.put("MSSR_EMPNO","");
		chkDataMap.put("BED_CODE","");
		if(InsertTime.length >0) {
			
			param.put("sttusCode",ResveStatusConst.DBSTATUS.WORK.toString()); //근무 생성 코드  셋
			for (int i = 0 ; i < InsertTime.length; i++) { //신규 근무 시간 배열 길이 만클 for 문 실행 
		     param.put("resveTime",InsertTime[i]);
		        dupChk(param , chkDataMap); // 증북자료 체크 
             	chk = chkDataMap.getInt("RESVE_CHECK");
             	 //상황별 중복건수 체크 하기 위한  근무 수정으로 동일 근무 중복은 발생 안함 
	             if(chk==20){
	            	 bedUse= bedUse +1;
				 } 
	             else if(chk==30){
					 bedUse= bedUse +1; 
				 }
				 insertResult = mssrDAO.insertSchedule(param); 
				if(!(insertResult)) { 
					throw new HrsException("error.processFailure", true);
				 } 
				 
				insertResult = mssrDAO.insertResveHist(param); 
				if(!(insertResult)) { 
					throw new HrsException("error.processFailure", true);
				 } 
			}
		}
      
		if(bedUse > 0) { //근무 수정은 베드 예약 중복 만 발생 
   		 String message = messageSource.getMessage("error.mssr.duplicateBed", new String[] {
   				 chkDataMap.getString("END_DATE"),
   				 chkDataMap.getString("MSSR_NCNM"),
   				 chkDataMap.getString("BED_CODE"),
   				
			}, Locale.forLanguageTag(param.getString("_ep_locale")));	
   		 throw new HrsException(message); 
   	 }
	    //삭제
		if (DeleteTime.length>0) { 
			for (int i = 0 ; i < DeleteTime.length; i++) {
				 param.put("resveTime",DeleteTime[i]);
				 
				 Map item = mssrDAO.selectResveItem(param); //삭제 근무 시간 예약 번호 가져오기  RESVE_NO
				 
				 if(!"0".equals(item.get("resveNo").toString())) { //예약 번호  가 있으면 삭제 
					 param.put("RESVE_NO",item.get("resveNo").toString());
					 
					 insertResult = deleteResveItme(param); // 근무시간 근무 취소 처리 
					 
					 if(!(insertResult)) { 
							throw new HrsException("error.processFailure", true);
						 }		
				
					param.remove("RESVE_NO");
				 }else {
					 throw new HrsException("error.processFailure", true);
				 }
			}
		}
			// data적용 성공여부
			result.setItemOne(insertResult);
			return result;
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴  리스트 삭제(FOR 문으로  건별삭제 deleteResveItme 호출 )
	 * @작성일 : 2019.10.18
	 * @작성자 : P150113
	 * @paramType: jsonFromatString 
	 * @param :[{resveDate : 20190930 , mssrCode :M000001, bldCode :SK01 , RESVE_NO:1987},{..., RESVE_NO:2019}{..., RESVE_NO:2003}{..., RESVE_NO:3027}]
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public  ResponseResult deleteResve(DataEntity param) {
		  ResponseResult result = new ResponseResult(); 
		  DataEntity paramsMap = new DataEntity();
		 //jsonString -> arrayList<Map> 
		  ArrayList<HashMap<String, String>> getListItems = (ArrayList<HashMap<String, String>>)JsonUtils.stringToJsonClass(param.getString("params"), ArrayList.class);
	      boolean updateResult = false;	 
		    for (int j = 0; j < getListItems.size(); j++) {
		    	paramsMap.putAll(getListItems.get(j));
		    	paramsMap.put("regEmpNo", param.getString("regEmpNo"));
		    	updateResult = deleteResveItme(paramsMap);
		    	 
				if(!(updateResult)) { 
					throw new HrsException("error.processFailure", true);
				 }	
		
           }   	
		result.setItemOne(updateResult);
		// data적용 성공여부
		return result;
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴  건별 삭제 휴일 등록 스케줄 삭제 에서도 사용 
	 * @작성일 : 2019.10.18
	 * @작성자 : P150113
	 * @param : {resveDate : 20190930 , mssrCode :M000001, bldCode :SK01 , RESVE_NO:1987}
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public  boolean deleteResveItme(DataEntity param) {
	      boolean updateResult = false;	 
		 
		    	param.put("canclYn", "Y");
		    	param.put("sttusCode",ResveStatusConst.DBSTATUS.WORK_CANCL.toString());

		    	updateResult = mssrDAO.deleteResve(param); 
               
				if(!(updateResult)) { 
					throw new HrsException("error.processFailure", true);
				 }	
				
				updateResult = mssrDAO.insertResveHist(param); 
				if(!(updateResult)) { 
					throw new HrsException("error.processFailure", true);
				 }	
				//근무 취소 안내 sms 전송 및  일정취소 아웃룩연동
				sendSms(param);
		// data적용 성공여부
		return updateResult;
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴  취소 SMS 전송  
	 * @작성일 : 2019.09.30
	 * @작성자 : P150113
	 * @param :{RESVE_NO:0000}
	 * @return
	 * @변경이력 :
	 */	
	
  public void sendSms(DataEntity param) {
	 // ResponseResult result = new ResponseResult(); 
	   Map smsItem = mssrDAO.selectSmsInfoGet(param); //예약번호로  SMS 정보를 가져온다 .
	   if(!StringUtil.isEmpty(smsItem.get("COMPT_YN").toString()) &&  "N".equals(smsItem.get("COMPT_YN").toString())) {// 케어 완료건은 SMS 전송 제외  	
		   if(!StringUtil.isEmpty(smsItem.get("RESVE_EMPNO").toString())) {
				smsItem.put("targetEmpno", smsItem.get("RESVE_EMPNO").toString());
		       cspService.insertCspSMS(smsItem, "csp.sms.adminResveCancel", Locale.forLanguageTag(param.getString("_ep_locale")));
		          
		   	   //일정취소 아웃룩연동
			   scheduleService.updateScheduleCancel(smsItem);
			}				
			
			if(!StringUtil.isEmpty(smsItem.get("WAIT_EMPNO").toString())) {
				smsItem.put("targetEmpno", smsItem.get("WAIT_EMPNO"));
				cspService.insertCspSMS(smsItem, "csp.sms.adminWaitCancel", Locale.forLanguageTag(param.getString("_ep_locale")));		
			}
	   }  
  }

/**
 * 
 * @설명 : 관리사 스케줄 등록 중복 자료 체크 
 * @작성일 : 2019.09.30
 * @작성자 : P150113
 * @param param
 * @return
 * @변경이력 :
 */	
	
  public void dupChk(DataEntity param , DataEntity refResult) { //근무일에 동일인,미리예약 된 예약건이 잇는지 확인 후  메세지  정보 RETURN 
	    Map result = mssrDAO.selectResveCheck(param); 
	    refResult.put("RESVE_CHECK" ,result.get("RESVE_CHECK").toString());
	    if (result.get("RESVE_CHECK").toString()!= "0") {
	       refResult.put("END_DATE" , DateUtil.yyyymmdd2HumanReadable(result.get("END_DATE").toString()));		
	       refResult.put("MSSR_NCNM" ,  result.get("MSSR_NCNM").toString());
	       refResult.put("BED_CODE" ,result.get("BED_CODE").toString());
	    }
	    
  }
			
}