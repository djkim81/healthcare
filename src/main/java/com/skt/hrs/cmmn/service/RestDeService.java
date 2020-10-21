package com.skt.hrs.cmmn.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.JsonUtils;
import com.pub.core.util.StringUtil;
import com.skt.hrs.cmmn.contants.ResveStatusConst;
import com.skt.hrs.cmmn.dao.RestDeDAO;
import com.skt.hrs.mssr.service.MssrService;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.utils.DateUtil;





@Service("restDeService")
public class RestDeService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="restDeDAO")
	private RestDeDAO restDeDAO;
    
	@Autowired
	 MssrService mssrService;
  	
	/**
	 * 
	 * @설명 : 휴일 리스트 조회
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectRestDeList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(restDeDAO.selectRestDeList(param));
		return result;
	}
	
	/**
	 * 
	 * @설명 : 휴일 상세 조회
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectRestDeItem(DataEntity param) {
		ResponseResult result = new ResponseResult();
		 result.setItemOne(restDeDAO.selectRestDeItem(param));
		return result;
	}
	

	/**
	 * 
	 * @설명 : 휴일 수정 /동록/삭제 
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult saveRestDe(DataEntity param) {
		ResponseResult result = new ResponseResult();
		boolean  saveResult = false;
		String saveStat = param.getString("saveStat"); // C: 신규  U:수정		 
		int days = 0 ; 
		String startDate =param.getString("startDate"), endDate =  param.getString("endDate");
		String workDate = "";
		DataEntity  delParam = new DataEntity();
		if (saveStat.equals("C")) {
        	days = DateUtil.getDateDiff(startDate , endDate); // 등록할 일수 
			
        	for (int i = 0; i<= days; i++) { // 일수 만큰 for 문 실행   
        		workDate = DateUtil.getDateAdd(startDate,i); // 시작일 + i 일자   로 신규 자료 생성 
        		param.put("restDeDate",workDate.replaceAll("-", ""));
        		saveResult = restDeDAO.insertRestDe(param); 
			 	if(!(saveResult)) { 
					throw new HrsException("error.processFailure", true);
				 } 
			 	
          }
        	// 휴일 등록시   등록 기간에  헬스 키퍼 스케줄이 포함 된 경우   건별로 삭제 및 근무 취소 로직 
  
        	param.put("startDate",param.getString("startDate").replaceAll("-", ""));
        	param.put("endDate", param.getString("endDate").replaceAll("-", ""));      
            List<HashMap<String,String>> list = restDeDAO.selectRestReveList(param);  // 휴일 기간에 포함되는  근무/예약  리스트 가져오기 (RESVE_NO,STTUS_CODE:STS00 ~ STS04 자료만 조회)
    		for(HashMap<String,String> item : list) {		
    		     	delParam.putAll(item);
    		     	delParam.put("regEmpNo",param.getString("regEmpNo"));
    		          	
    				if(ResveStatusConst.DBSTATUS.WORK.toString().equals(item.get("STTUS_CODE"))) {//최종 상택값이 근무인 경우 STS00
    					delParam.put("delType", "sttus"); // RESVE_STTUS ROW 삭제 
    					saveResult = restDeDAO.deleteResve(delParam); 
    					delParam.put("delType", "hist");// RESVE_HIST ROW 삭제
    					saveResult = restDeDAO.deleteResve(delParam); 
    					if(!(saveResult)) { 
    						throw new HrsException("error.processFailure", true);
    					 } 	
    				}else  { // 예약 상태가 예약 완료/대기완료/예약취소/대기취소  인 경우  근무취소 처리 
    					saveResult = mssrService.deleteResveItme(delParam);	//스케줄관리 근무취소 호출  (취소SMS /아웃룩 스케줄 취소)
    					if(!(saveResult)) { 
    						throw new HrsException("error.processFailure", true);
    					 } 	
    				}	
    		}
    		
			
		}else if (saveStat.equals("U")) { // 수정   현재  휴일 명만 수정 가능 
			saveResult = restDeDAO.updateRestDe(param);
		}
		if(!(saveResult)) { 
			throw new HrsException("error.processFailure", true);
		 } 	
   	
		return result;
	}
	
	/**
	 * 
	 * @설명 : 휴일 리스트 선택 삭제  
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public  ResponseResult deleteRestDe(DataEntity param) {
		  ResponseResult result = new ResponseResult(); 
		  String[] deleteEmpNo = param.split("deleteRestNo",","); // 20190901,20190902 ... SPLIT 배열 생성 
	      boolean updateResult = false;	 
		    for (int i = 0; i < deleteEmpNo.length; i++) {
		    	param.put("restDeNo",deleteEmpNo[i]);
		    	param.put("usedYn","N");    // USED_YN  UPDATE  처리  Y:휴일 사용   N:휴일 미사용 (삭제)
		    	updateResult = restDeDAO.deleteRestDe(param);  
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
	 * @설명 : 휴일 중복 체크 
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectRestDeCheck(DataEntity param) { // 신규 등록 일자로 사용되고 있는 일자 있는지 확인 VIEW 중복값이 있을시  alertPOPUP();   	
		ResponseResult result = new ResponseResult();   
		boolean  checkResult =false;
		param.put("startDate",param.getString("startDate").replaceAll("-", ""));
    	param.put("endDate", param.getString("endDate").replaceAll("-", ""));
    	result.setItemOne(restDeDAO.selectRestCheck(param));
		return result;
	}
	
	/**
	 * 
	 * @설명 : 휴일에 등록된 스케줄  조회
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectRestReveList(DataEntity param) { //신규 일자 등록시  휴일기간에 포함된  근무/예약  건수 및 confirmPopup 메세지  출력     ;
		ResponseResult result = new ResponseResult();
		param.put("startDate",param.getString("startDate").replaceAll("-", ""));
    	param.put("endDate", param.getString("endDate").replaceAll("-", ""));
		result.setItemList(restDeDAO.selectRestReveList(param));
		return result;
	}
	
	
}