package com.skt.hrs.cmmn.service;


import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.pub.core.util.JsonUtils;
import com.skt.hrs.cmmn.contants.ResveStatusConst;
import com.skt.hrs.cmmn.dao.CodeDAO;
import com.skt.hrs.cmmn.exception.HrsException;





@Service("codeService")
public class CodeService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	
	/**
	 * @설명 : 공통코드 조회
	 * @작성일 : 2019.08.27
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectUserMenuList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		//int totalCnt = portalGnbDao.selectUserMenuListTotalCnt(param);
		
		result.setItemList(codeDAO.selectCodeList(param));
		
		return result;
	}
	
	/**
	 * @설명 : 공통코드 전체 조회
	 * @작성일 : 2019.08.30
	 * @작성자 : LEE.J.H.
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectAllCodeList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(codeDAO.selectAllCodeList(param));
		
		return result;
	}

	/**
	 * @설명 : 공통코드 종류 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectCodeKindList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		
		result.setItemList(codeDAO.selectCodeKindList(param));
		
		return result;
	}
	
	/**
	 * @설명 : 공통코드 리스트 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectCodeManageList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(codeDAO.selectCodeManageList(param));
		return result;
	}
	
	/**
	 * @설명 : 공통코드 리스트 TOTAL COUNT 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectCodeManageListTotalCount(DataEntity param) {
		int result = codeDAO.selectCodeManageListTotalCount(param);
		return result;
	}

	/**
	 * @설명 : 공통코드 등록
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult insertCodeManage(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(codeDAO.insertCodeManage(param));
		return result;
	}

	/**
	 * @설명 : 공통코드 목록 논리 삭제 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult updateDelYCodeManageList(DataEntity param) {
		ResponseResult result = new ResponseResult(); 
		DataEntity paramsMap = new DataEntity();
		ArrayList<HashMap<String, String>> getListItems = (ArrayList<HashMap<String, String>>)JsonUtils.stringToJsonClass(param.getString("params"), ArrayList.class);
		boolean updateResult = false;
		for (int j = 0; j < getListItems.size(); j++) {
			paramsMap.putAll(getListItems.get(j));
			paramsMap.put("regEmpNo", param.getString("regEmpNo"));
			
			updateResult = codeDAO.updateDelYCodeManageList(paramsMap);
			
			if(!(updateResult)) { 
				throw new HrsException("error.processFailure", true);
			}
		} 
		result.setItemOne(updateResult);
		// data적용 성공여부
		return result;
	}
	
	/**
	 * @설명 : 공통코드 수정
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult updateCodeManageModify(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(codeDAO.updateCodeManageModify(param));
		return result;
	}
	
	/**
	 * @설명 : 공통코드 중복 COUNT 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectCodeManageDuplicateCount(DataEntity param) {
		int result = codeDAO.selectCodeManageDuplicateCount(param);
		return result;
	}
	
}