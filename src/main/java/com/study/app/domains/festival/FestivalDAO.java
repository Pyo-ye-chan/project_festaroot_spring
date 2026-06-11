package com.study.app.domains.festival;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.app.domains.festival.dto.FestDetailDTO;
import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.festival.dto.FestivalSearchDTO;

@Repository
public class FestivalDAO {

	@Autowired
	private SqlSessionTemplate mybatis;
	
	public List<FestivalDTO> getAllFestival(){
		return mybatis.selectList("Festival.getAll");
	}

	// 축제 찾기 > 조건에 맞는 축제 목록 가져오기
	public List<FestivalDTO> getSearchFestivals(FestivalSearchDTO searchDTO) {
		return mybatis.selectList("Festival.selectByOptions", searchDTO);
	}

	// 축제 찾기 > 네비게이터 카운트
	public int getSearchFestivalCount(FestivalSearchDTO searchDTO) {
		return mybatis.selectOne("Festival.getSearchFestivalCount", searchDTO);
	}

	// 축제 찾기 > 축제 목록별 조회수
	public void increaseViewCount(String contentId) {
		mybatis.insert("Festival.increaseViewCount", contentId);
	}
	
	// 홈 > 지역별 인기 축제 목록 top3
	public List<FestivalDTO> getTop3ByRegion(String regionName) {
		return mybatis.selectList("Festival.getTop3ByRegion", regionName);
	}
	
	// 홈 > 종료 임박 축제 목록
	public List<FestivalDTO> getClosingSoonFestivals() {
	    return mybatis.selectList("Festival.getClosingSoonFestivals");
	}
	
	// 홈 > 랜덤 축제 추천
	public FestivalDTO getRandomFestival() {
		return mybatis.selectOne("Festival.getRandomFestival");
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

	public FestivalDTO selectDeatilByContentId(String contentId) {
		return mybatis.selectOne("Festival.selectDetailByContentId", contentId);
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

	// 로그인 아이디 기준 찜 누른 축제 목록
	public List<Long> getMyFestivalLikedIds(String memberId) {
		return mybatis.selectList("Festival.getMyFestivalLikedIds", memberId);
	}

	public List<Map<String, Object>> getMyFestivalLikedDetails(String memberId) {
		return mybatis.selectList("Festival.getMyFestivalLikedDetails", memberId);
	}

	// 찜 존재 여부 확인 (Count)
	public int checkLikeExists(Map<String, Object> toggle) {
		return mybatis.selectOne("Festival.checkLikeExists", toggle);
	}

	// 찜 테이블에 추가 (Insert)
	public int insertLike(Map<String, Object> toggle) {
		return mybatis.insert("Festival.insertLike", toggle);
	}

	// 찜 테이블에서 삭제 (Delete)
	public int deleteLike(Map<String, Object> toggle) {
		return mybatis.delete("Festival.deleteLike", toggle);
	}

	// 축제 메인 테이블 총 찜수 증가 (+1)
	public int incrementLikeCount(Long contentId) {
		return mybatis.update("Festival.incrementLikeCount", contentId);
	}

	// 축제 메인 테이블 총 찜수 감소 (-1)
	public int decrementLikeCount(Long contentId) {
		return mybatis.update("Festival.decrementLikeCount", contentId);
	}

	// 총 찜 개수 조회
	public int getFestivalLikeCount(Long contentId) {
		return mybatis.selectOne("Festival.getFestivalLikeCount", contentId);
	}

	// 지역별 축제 조회 (추천 후보용)
	public List<Map<String, Object>> getFestivalsByRegion(String region) {
		return mybatis.selectList("Festival.selectByRegion", region);
	}

	// 추천용 축제 상세 조회
	public Map<String, Object> getFestivalDetail(Long contentId) {
		return mybatis.selectOne("Festival.selectDetailForRecommendation", contentId);
	}

	// 관심 테마 기반 축제 조회
	public List<Map<String, Object>> getFestivalsByThemes(List<String> themeCodes) {
		return mybatis.selectList("Festival.selectByInterestThemes", themeCodes);
	}

	// 인기 축제 조회 (Fallback)
	public List<Map<String, Object>> getPopularFestivals() {
		return mybatis.selectList("Festival.selectPopularFestivals");
	}

}
