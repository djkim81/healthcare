package com.skt.hrs.resve.service;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.core.entity.ResponseResult;
import com.skt.hrs.resve.dao.ResveBlacklistDAO;





@Service("resveBlacklistService")
public class ResveBlacklistService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="resveBlacklistDAO")
	private ResveBlacklistDAO resveBlacklistDAO;
	
	/**
	 * 
	 * @설명 :	블랙리스트 등록 
	 * @작성일 : 2019.09.09
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	@Transactional
	public ResponseResult insertBlacklist(int weeks) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(resveBlacklistDAO.insertBlacklist(weeks));
		return result;
	}
	
}