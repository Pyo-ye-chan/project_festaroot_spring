package com.study.app.domains.achievement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.app.domains.achievement.dto.AchievementDTO;
import com.study.app.domains.achievement.dto.LevelSystemDTO;
import com.study.app.domains.achievement.dto.UserAchProgressDTO;

@Repository
public class AchievementDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    /**
     * 특정 타입의 미달성 업적 목록을 조회합니다.
     */
    public List<AchievementDTO> getUnachievedAchievementsByType(String memberId, String achType) {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("achType", achType);
        return mybatis.selectList("Achievement.getUnachievedAchievementsByType", params);
    }

    /**
     * 업적 진행도를 업데이트합니다. (없으면 생성, 있으면 증가)
     */
    public void upsertProgress(String memberId, Long achievementId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("achievementId", achievementId);
        mybatis.update("Achievement.upsertProgress", params);
    }

    /**
     * 현재 진행도를 조회합니다.
     */
    public UserAchProgressDTO getProgress(String memberId, Long achievementId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("achievementId", achievementId);
        return mybatis.selectOne("Achievement.getProgress", params);
    }

    /**
     * 업적 달성 처리 (상태 변경 및 이력 저장)
     */
    public void completeAchievement(String memberId, Long achievementId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("achievementId", achievementId);
        
        mybatis.update("Achievement.updateAchievedStatus", params);
        mybatis.insert("Achievement.insertAchievementHistory", params);
    }

    /**
     * 유저 경험치 증가
     */
    public void addMemberExp(String memberId, Integer expAmount) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("expAmount", expAmount);
        mybatis.update("Achievement.addMemberExp", params);
    }

    /**
     * 유저의 현재 경험치 및 레벨 정보 조회
     */
    public Map<String, Object> getMemberExpAndLevel(String memberId) {
        return mybatis.selectOne("Achievement.getMemberExpAndLevel", memberId);
    }

    /**
     * 다음 레벨 정보 조회
     */
    public LevelSystemDTO getNextLevelInfo(Integer currentLevel) {
        return mybatis.selectOne("Achievement.getNextLevelInfo", currentLevel);
    }

    /**
     * 유저 레벨 및 칭호 업데이트
     */
    public void updateMemberLevelAndTitle(String memberId, Integer newLevel, Long titleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("newLevel", newLevel);
        params.put("titleId", titleId);
        mybatis.update("Achievement.updateMemberLevelAndTitle", params);
    }

    /**
     * 달성한 업적 총 개수 조회 (ALL_CLEAR 체크용)
     */
    public int getAchievedCount(String memberId) {
        return mybatis.selectOne("Achievement.getAchievedCount", memberId);
    }

    /**
     * 특정 업적 정보 조회
     */
    public AchievementDTO getAchievementById(Long achievementId) {
        return mybatis.selectOne("Achievement.getAchievementById", achievementId);
    }

    /**
     * 유저의 모든 업적 현황 조회
     */
    public List<Map<String, Object>> getAllAchievementsWithProgress(String memberId) {
        return mybatis.selectList("Achievement.getAllAchievementsWithProgress", memberId);
    }
}
