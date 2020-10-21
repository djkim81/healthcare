package com.skt.hrs.charger.service;


import java.text.SimpleDateFormat;

import javax.annotation.PreDestroy;
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
import com.skt.hrs.charger.dao.ChargerDAO;


import com.skt.hrs.utils.DateUtil;
import com.skt.hrs.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.pub.core.util.JsonUtils;





@Service("chargerService")
public class ChargerService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="chargerDAO")
	private ChargerDAO chargerDAO;


	/**
	 * 
	 * @설명 : 담당자  조회
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return: response
	 * @변경이력 :
	 */
	public ResponseResult selectChargerList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(chargerDAO.selectChargerList(param));
		return result;
	}
	
	/**
	 * 
	 * @설명 : 담당자 상세 조회
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return: response
	 * @변경이력 :
	 */
	public ResponseResult selectChargerItem(DataEntity param) {
		ResponseResult result = new ResponseResult();
		 result.setItemOne(chargerDAO.selectChargerItem(param));
		return result;
	}
	
	/**
	 * 
	 * @설명 : 담당자 사번 중복 체크 
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param
	 * @return: response
	 * @변경이력 :
	 */
	public ResponseResult selectEmpNoCheck(DataEntity param) {	
		ResponseResult result = new ResponseResult();
		boolean  checkResult =false;
		int empCheckCnt = chargerDAO.selectEmpNoCheck(param);
		if(empCheckCnt==1) {
			checkResult = true;
		}
		result.setItemOne(checkResult);
		return result;
	}
	/**
	 * 
	 * @설명 : 담당자 동록/수정
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param  -> saveStat C:INSERT ,U:UPDATE
	 * @return: response
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult chargerSave(DataEntity param) {
		ResponseResult result = new ResponseResult();
		boolean  saveResult = false;
		String saveStat = param.getString("saveStat");		
		if (saveStat.equals("C")) {
			saveResult = chargerDAO.insertCharger(param); 
		}else if (saveStat.equals("U")) {
			saveResult = chargerDAO.updateCharger(param);
		}
		if(!(saveResult)) { 
			throw new HrsException("error.processFailure", true);
		 } 	
   	
		return result;
	}
	
	/**
	 * 
	 * @설명 : 담당자 리스트 선택 삭제 
	 * @작성일 : 2019.10.07
	 * @작성자 : P150113
	 * @param param 
	 * @return: response
	 * @변경이력 :
	 */
	
	@Transactional
	public  ResponseResult deleteCharger(DataEntity param) { // 1,2 ... SPLIT 배열 생성 
		  ResponseResult result = new ResponseResult(); 
		  String[] deleteEmpNo = param.split("deleteEmpNo",",");
	      boolean updateResult = false;	 
		    for (int i = 0; i < deleteEmpNo.length; i++) {
		    	param.put("chargerEmpno",deleteEmpNo[i]);
		    	updateResult = chargerDAO.deleteCharger(param); 
                 
				if(!(updateResult)) { 
					throw new HrsException("error.processFailure", true);
				 }		
           } 
		result.setItemOne(updateResult);
		// data적용 성공여부
		return result;
	}
	
}