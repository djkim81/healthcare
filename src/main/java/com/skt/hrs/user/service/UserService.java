package com.skt.hrs.user.service;


import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;
import com.skt.hrs.user.dao.UserDAO;





@Service("userService")
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="userDAO")
	private UserDAO userDAO;

	
	/**
	 * 
	 * @설명 : 사용자 정보 조회
	 * @작성일 : 2019.09.03
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectUserInfo(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(userDAO.selectUserInfo(param));
		return result;
	}
	
	public ResponseResult updateAgree(DataEntity param) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(userDAO.updateAgree(param));
		return result;
	}
	
}