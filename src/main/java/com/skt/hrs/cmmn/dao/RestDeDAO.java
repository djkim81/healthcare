package com.skt.hrs.cmmn.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("restDeDAO")
public class RestDeDAO {

	private static final Logger logger = LoggerFactory.getLogger(RestDeDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	

	
	/**
	 * 
	 * @설명 : 휴일 리스트 조회 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	
	  public List selectRestDeList(DataEntity param) { 
		  return sqlSession.selectList("restdeMapper.selectRestDeList", param);
	  }
	 
	
	
	/**
	 * 
	 * @설명 : 휴일중복체크
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	
	  public Map selectRestCheck(DataEntity param) { 
		  return sqlSession.selectOne("restdeMapper.selectRestCheck", param); 
	  }
	 
	/**
	 * 
	 * @설명 :휴일테이블 등록
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	
	  public boolean insertRestDe(DataEntity param) { 
		  int result = sqlSession.insert("restdeMapper.insertRestDe", param); 
		  if(result == 1) { 
			  return true; 
		  } 
		  return false; 
	}
	  
  /**
	 * 
	 * @설명 : 휴일테이블  수정 삭제 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	  public boolean updateRestDe(DataEntity param) { 
		  int result = sqlSession.update("restdeMapper.updateRestDe", param); 
		  if(result == 1) { 
			  return true; 
		  } 
		  return false; 
	}
	  /**
		 * 
		 * @설명 : 휴일테이블   삭제 
		 * @작성일 : 2019.10.11
		 * @작성자 : P150113
		 * @param param
		 * @return
		 * @변경이력 : 
		 *  */
	 public boolean deleteRestDe(DataEntity param) { 
		  int result = sqlSession.update("restdeMapper.deleteRestDe", param); 
		  if(result == 1) { 
			  return true; 
		  } 
		  return false; 
	}
	 /**
		 * 
		 * @설명 : 예약 STTUS/HIST 테이블 데이터  삭제 
		 * @작성일 : 2019.10.11
		 * @작성자 : P150113
		 * @param param
		 * @return
		 * @변경이력 :
		 */
	 
	 public boolean deleteResve(DataEntity param) { 
		  int result = sqlSession.delete("restdeMapper.deleteResve", param); 
		  if(result == 1) { 
			  return true; 
		  } 
		  return false; 
	}

	 
	 /**
		 * 
		 * @설명 : 등록된 휴일 조회  
		 * @작성일 : 2019.10.11
		 * @작성자 : P150113
		 * @param param
		 * @return
		 * @변경이력 :
		 */
	public Map selectRestDeItem(DataEntity param) {
		return sqlSession.selectOne("restdeMapper.selectRestDeItem", param);
	}
	
	
	/**
	 * 
	 * @설명 : 등록될 휴일 스케줄  조회 
	 * @작성일 : 2019.10.11
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	
	  public List selectRestReveList(DataEntity param) { 
		  return sqlSession.selectList("restdeMapper.selectRestReveList", param);
	  }
	
}
