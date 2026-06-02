package com.study.app.domains.festival;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.app.domains.festival.dto.FestDetailDTO;
import com.study.app.domains.festival.dto.FestivalDTO;

@Repository
public class FestivalDAO {

	@Autowired
	private SqlSessionTemplate mybatis;

	public List<FestivalDTO> getAllFestival() {
		return mybatis.selectList("Festival.getAll");

	}

	public FestivalDTO selectByContentId(String contentId) {
		return mybatis.selectOne("Festival.selectByContentId", contentId);
	}

	// 축제 정보 업데이트 또는 추가하는 메서드
	public int upsertFestival(FestivalDTO dto) {
		return mybatis.update("Festival.upsertFestival", dto);
	}
	
	// CLOB 타입 업데이트 분리
	public int updateFestivalDetail(FestivalDTO dto) {
	    return mybatis.update("Festival.updateFestivalDetail", dto);
	}
	
	public FestDetailDTO selectDeatilByContentId(String contentId) {
		return mybatis.selectOne("Festival.selectDetailByContentId", contentId);
	}

}
