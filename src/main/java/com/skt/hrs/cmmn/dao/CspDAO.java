package com.skt.hrs.cmmn.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.skt.hrs.cmmn.vo.CspVo;


@Repository("cspDAO")
public class CspDAO {

	private static final Logger logger = LoggerFactory.getLogger(CspDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	

	/**
	 * 
	 * @설명 : SMS, EMAIL 발신대상 insert 
	 * @작성일 : 2019.09.06
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean insertCspSend(CspVo vo) {
		int result = sqlSession.insert("cmmnCspMapper.insertCspSend", vo);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @설명 : SMS, EMAIL 발신대상 select 
	 * @작성일 : 2019.09.07
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectCspSendList() {
		return sqlSession.selectList("cmmnCspMapper.selectCspSendList");
	}
	
	
	public boolean updateCspSendLog(CspVo vo) {
		int result = sqlSession.update("cmmnCspMapper.updateCspSendLog", vo);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	
	
	
	
}
