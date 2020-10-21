package com.skt.hrs.mssr.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("healthkeperDAO")
public class HealthkeperDAO {

	private static final Logger logger = LoggerFactory.getLogger(HealthkeperDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	
	/**
	 * 
	 * @설명 : 헬스키퍼 리스트 조회 
	 * @Date : 2019. 9. 20. 
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectHealthkeperList(DataEntity param) {
		return sqlSession.selectList("healthkeperMapper.selectHealthkeperList", param);
	}
	
	
	
	/**
	 * 
	 * @설명 : 헬스키퍼 리스트 TOTAL COUNT 조회 
	 * @Date : 2019. 9. 20. 
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectHealthkeperListTotalCount(DataEntity param) {
		return sqlSession.selectOne("healthkeperMapper.selectHealthkeperListTotalCount", param);
	}


	/**
	 * 
	 * @설명 : 헬스키퍼 등록 
	 * @Date : 2019. 9. 20. 
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean insertHealthkeper(DataEntity param) {
		int result = sqlSession.insert("healthkeperMapper.insertHealthkeper", param);
		if(result == 1) {
			return true;
		}
		return false;
	}


	/**
	 * 
	 * @설명 : 헬스키퍼 사번 COUNT 
	 * @Date : 2019. 9. 30. 
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectHealthkeperEmpnoTotalCount(DataEntity param) {
		return sqlSession.selectOne("healthkeperMapper.selectHealthkeperEmpnoTotalCount", param);
	}


	/**
	 * 
	 * @설명 : 헬스키퍼 수정등록 
	 * @Date : 2019. 10. 01. 
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean updateHealthkeper(DataEntity param) {
		int result = sqlSession.update("healthkeperMapper.updateHealthkeper", param);
		if(result == 1) {
			return true;
		}
		return false;
	}

	
}
