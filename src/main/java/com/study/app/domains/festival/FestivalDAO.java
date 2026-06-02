package com.study.app.domains.festival;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    // 테마가 없는 축제 목록 조회
    public List<FestivalDTO> getFestivalsWithoutTheme() {
        return mybatis.selectList("Festival.getFestivalsWithoutTheme");
    }

    // 매핑 데이터 저장
    public void insertFestivalThemeMapping(Long contentId, String themeCode) {
        mybatis.insert("Festival.insertFestivalThemeMapping", 
            java.util.Map.of("content_id", contentId, "theme_code", themeCode));
    }

    // 인덱싱 대상 데이터 조회
    public List<Map<String, Object>> getFestivalsToIndex() {
        return mybatis.selectList("Festival.getFestivalsToIndex");
    }

    // 인덱싱 완료 후 상태 기록
    public void updateIndexedModifiedTime(Long contentId, String modifiedTime) {
        mybatis.update("Festival.updateIndexedModifiedTime", 
            java.util.Map.of("content_id", contentId, "modified_time", modifiedTime));
    }

}
