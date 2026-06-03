package com.study.app.domains.festival;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.festival.dto.FestivalSearchDTO;

@Repository
public class FestivalDAO {

	@Autowired
	private SqlSessionTemplate mybatis;

	// 축제 찾기 > 조건에 맞는 축제 목록 가져오기
	public List<FestivalDTO> getSearchFestivals(FestivalSearchDTO searchDTO) {
		return mybatis.selectList("Festival.selectByOptions", searchDTO);
	}

	// 축제 찾기 > 네비게이터 카운트
	public int getSearchFestivalCount(FestivalSearchDTO searchDTO) {
		return mybatis.selectOne("Festival.getSearchFestivalCount", searchDTO);
	}

	public FestivalDTO selectByContentId(String contentId) {
		return mybatis.selectOne("Festival.selectByContentId", contentId);
	}

	// 축제 정보 업데이트 또는 추가하는 메서드
	public int upsertFestival(FestivalDTO dto) {
		return mybatis.update("Festival.upsertFestival", dto);
	}

}
