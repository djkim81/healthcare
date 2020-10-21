package com.skt.hrs.mssr.service;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.mssr.dao.HealthkeperDAO;





@Service("healthkeperService")
public class HealthkeperService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="healthkeperDAO")
	private HealthkeperDAO healthkeperDAO;

	
	/**
	 * 
	 * @설명 : 헬스키퍼 리스트 조회
	 * @Date      : 2019. 9. 20. 
	 * @작성자    : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectHealthkeperList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(healthkeperDAO.selectHealthkeperList(param));
		return result;
	}
	
	
	/**
	 * 
	 * @설명 : 헬스키퍼 리스트 TOTAL COUNT 조회
	* @Date      : 2019. 9. 20. 
	 * @작성자    : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectHealthkeperListTotalCount(DataEntity param) {
		int result = healthkeperDAO.selectHealthkeperListTotalCount(param);
		return result;
	}


	/**
	 * 
	 * @설명 : 헬스키퍼 스케쥴 등록
	 * @Date      : 2019. 9. 30. 
	 * @작성자    : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult insertHealthkeper(DataEntity param) {
		ResponseResult result = new ResponseResult();
		boolean  insertResult = false;
		int cntEmpno = 0;
		String Empno="M";
		String zeroEmpno;
		
		cntEmpno = healthkeperDAO.selectHealthkeperEmpnoTotalCount(param);
		cntEmpno +=1;
		
		zeroEmpno = String.format("%07d", cntEmpno);
		Empno = Empno + zeroEmpno;
		
		param.put("Empno", Empno);
		
		insertResult = healthkeperDAO.insertHealthkeper(param); 
		
		if(!insertResult) {
			throw new HrsException("error.processFailure", true);
		}
		
		result.setItemOne(insertResult);
		return result;
	}

	@Transactional
	public ResponseResult healthkeperModify(DataEntity param) {
		ResponseResult result = new ResponseResult();
		boolean  updateResult = false;
		
		updateResult = healthkeperDAO.updateHealthkeper(param);
		
		if(!updateResult) {
			throw new HrsException("error.processFailure", true);
		}
		
		result.setItemOne(updateResult);
		return result;
	}
  
			
}