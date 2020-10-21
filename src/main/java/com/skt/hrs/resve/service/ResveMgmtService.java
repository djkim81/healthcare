package com.skt.hrs.resve.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.contants.ResveStatusConst;
import com.skt.hrs.cmmn.exception.HrsException;
import com.skt.hrs.resve.dao.ResveMgmtDAO;
import com.skt.hrs.resve.dao.ResveStatusDAO;



@Service("resveMgmtService")
public class ResveMgmtService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="resveMgmtDAO")
	private ResveMgmtDAO resveMgmtDAO;
	
	@Resource(name="resveStatusDAO")
	private ResveStatusDAO resveStatusDAO;
		
	@Autowired
	private ResveStatusService resveStatusService;
	
	/**
	 * 
	 * @설명 : (관리자) 예약 현황 조회 
	 * @작성일 : 2019.09.23
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectResveMgmtList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(resveMgmtDAO.selectResveMgmtList(param));
		return result;
	}
	
	
	
	/**
	 * 
	 * @설명 : (관리자) 예약 현황 카운트
	 * @작성일 : 2019.09.23
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectResveMgmtListTotalCount(DataEntity param) {
		int result = resveMgmtDAO.selectResveMgmtListTotalCount(param);
		return result;
	}
	
	/**
	 * 
	 * @설명 : 예약 단건 조회 
	 * @작성일 : 2019.10.15
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectResveMgmtItem(DataEntity param) {
		ResponseResult result = new ResponseResult();
		
		Map resveItem = resveMgmtDAO.selectResveMgmtItem(param);
		
		/*****************************
		 *  VALIDATION
		 ****************************/
		if(resveItem == null ) 
			throw new HrsException("error.invalidRequest", true);
		
		if("Y".equals(resveItem.get("CANCL_YN").toString())) {
			throw new HrsException("error.canceledResve", true);	//이미 근무취소 처리되었습니다. 
		}
		
		if("Y".equals(resveItem.get("COMPT_YN").toString())) {
			throw new HrsException("error.completeResve", true);	//이미 완료된 예약입니다.
		}
		
		// 시간체크 (현재시간<= 예약시간-20분)
// 케어완료로는 20분전 이후에도 변경가능		
//		Date resveDt = DateUtil.hrsDtToRealDt(resveItem.get("RESVE_DE").toString(), resveItem.get("RESVE_TM").toString());
//		if(!DateUtil.isPastBeforeMin(resveDt, 20)) {				
//			throw new HrsException("error.over20minModify", true);	//케어시작 20분 전 까지만 변경 가능합니다.
//		}
		
		result.setItemOne(resveItem);
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
	public ResponseResult selectResveMgmtDetailList(DataEntity param) {
		ResponseResult result = new ResponseResult();
		
		
		List<Map> list = resveMgmtDAO.selectResveMgmtDetailList(param);
		List<Map> retList = new ArrayList<Map>();
		for(Map item : list) {
			// 시스템이 예약완료 한 경우 -> (대기취소 + 예약완료) = 승계완료
			if("SYSTEM".equals(item.get("REG_EMPNO"))) {
				
				if(ResveStatusConst.DBSTATUS.RESVE_COMPT.toString().equals(item.get("STTUS_CODE"))) {		// 예약완료 => 승계완료
					item.put("STTUS_CODE_NM", "승계완료");
				}else if(ResveStatusConst.DBSTATUS.WAIT_CANCL.toString().equals(item.get("STTUS_CODE"))){	// 시스템에 의한 대기취소는 무시
					continue;
				}
			}
			retList.add(item);
		}
		
		result.setItemList(retList);
		
		return result;
	}
	
	/**
	 * 
	 * @설명 : 예약 상태 변경 
	 * @작성일 : 2019.10.15
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult modifyResveStatus(DataEntity param) {
		
		if(param.getString("sttusCode").equals(ResveStatusConst.DBSTATUS.COMPT.toString())) {	// 케어완료로 변경
			
			return resveStatusService.completeResveStatus(param);
			
		}else if(param.getString("sttusCode").equals(ResveStatusConst.DBSTATUS.RESVE_CANCL.toString())) {	//예약취소로 변경
			
			param.put("cancelGbn", ResveStatusConst.VIEWSTATUS.RESVE_COMPT.toString());
			return resveStatusService.cancelResveStatus(param);
			
		}else {
			throw new HrsException("error.invalidRequest", true);
		}
	}
	
	
	/**
	 * 
	 * @설명 : 대기 상태 변경 
	 * @작성일 : 2019.10.15
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult modifyWaitStatus(DataEntity param) {
		
		param.put("cancelGbn", ResveStatusConst.VIEWSTATUS.WAIT.toString());
		return resveStatusService.cancelResveStatus(param);
	}
	
	/**
	 * 
	 * @설명 : (관리자) 예약 현황 엑셀 조회 
	 * @작성일 : 2019.10.16
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectResveMgmtListExcel(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemList(resveMgmtDAO.selectResveMgmtListExcel(param));
		return result;
	}
}