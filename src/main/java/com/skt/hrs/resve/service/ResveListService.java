package com.skt.hrs.resve.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.contants.ResveStatusConst;
import com.skt.hrs.resve.dao.ResveListDAO;





@Service("resveListService")
public class ResveListService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="resveListDAO")
	private ResveListDAO resveListDAO;

	
	/**
	 * 
	 * @설명 : 예약현황리스트 조회
	 * @작성일 : 2019.08.29
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectResveList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(resveListDAO.selectResveList(param));
		return result;
	}
	
	
	
	/**
	 * 
	 * @설명 : 예약현황리스트 TOTAL COUNT 조회
	 * @작성일 : 2019.08.30
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectResveListTotalCount(DataEntity param) {
		int result = resveListDAO.selectResveListTotalCount(param);
		return result;
	}
	
	/**
	 * 
	 * @설명 : 예약 상세현황 조회
	 * @작성일 : 2019.09.18
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseResult selectResveDetailList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		List<Map> list = resveListDAO.selectResveDetailList(param);
		List<Map> retList = new ArrayList<Map>();
		for(Map item : list) {
			// 시스템이 예약완료 한 경우 -> (대기취소 + 예약완료) = 승계완료
			if("SYSTEM".equals(item.get("REG_EMPNO"))) {
				
				if(ResveStatusConst.DBSTATUS.RESVE_COMPT.toString().equals(item.get("STTUS_CODE"))) {		// 예약완료 => 승계완료
					item.put("STTUS_CODE_NM", "승계완료");
				}else if(ResveStatusConst.DBSTATUS.WAIT_CANCL.toString().equals(item.get("STTUS_CODE"))){	// 시스템에 의한 대기취소는 무시
					continue;
				}
				item.remove("REG_EMPNO");
				//item.remove("STTUS_CODE");
			}
			retList.add(item);
		}
		
		result.setItemList(retList);
		return result;
	}
	
}