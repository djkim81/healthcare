package com.skt.hrs.mssr.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("mssrblacklistDAO")
public class MssrblacklistDAO {

	private static final Logger logger = LoggerFactory.getLogger(MssrblacklistDAO.class);


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
	public List selectMssrblacklistList(DataEntity param) {
		return sqlSession.selectList("mssrblacklistMapper.selectMssrblacklistList", param);
	}

	/**
	 * 
	 * @설명 : 헬스키퍼 리스트 카운트 조회 
	 * @작성일 : 2019.10.22
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectMssrblacklistListCount(DataEntity param) {
		return sqlSession.selectOne("mssrblacklistMapper.selectMssrblacklistListCount", param);
	}
	
	/**
	 * 
	 * @설명 : No-show(블랙리스트) No-Show 상태로 2주 패널티만 제외 
	 * @Date : 2019. 10. 21.
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean mssrblacklistnoshowDelete(DataEntity param) {
		int result = sqlSession.delete("mssrblacklistMapper.mssrblacklistnoshowDelete", param);
		if(result == 1) {
			return true;
		}
		return false;
	}


	/**
	 * 
	 * @설명 : No-show(블랙리스트) 케어완료 상태로 변경
	 * @Date : 2019. 10. 21.
	 * @작성자 : YANG.H.R
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean mssrblacklistcomptUpdate(DataEntity param) {
		int result = sqlSession.update("mssrblacklistMapper.mssrblacklistcomptUpdate", param);
		if(result == 1) {
			return true;
		}
		return false;
	}


	public boolean mssrblacklisthistInsert(DataEntity param) {
		int result = sqlSession.insert("mssrblacklistMapper.mssrblacklisthistInsert", param);
		if(result == 1) {
			return true;
		}
		return false;
	}


}
