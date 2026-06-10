package com.study.app.domains.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.app.domains.achievement.dto.AchievementDTO;
import com.study.app.domains.achievement.dto.AchievementResultDTO;
import com.study.app.domains.achievement.dto.LevelSystemDTO;
import com.study.app.domains.achievement.dto.UserAchProgressDTO;

@Service
public class AchievementService {

    private static final Logger log = LoggerFactory.getLogger(AchievementService.class);

    @Autowired
    private AchievementDAO achievementDAO;

    /**
     * 마이페이지용 유저 업적 및 성장 통합 정보를 조회합니다.
     */
    public Map<String, Object> getUserAchievementData(String memberId) {
        Map<String, Object> data = new HashMap<>();
        
        // 1. 유저의 현재 성장 정보 (레벨, 경험치, 칭호)
        Map<String, Object> userGrowth = achievementDAO.getMemberExpAndLevel(memberId);
        data.put("userGrowth", userGrowth);
        
        // 2. 전체 업적 리스트 및 진행 현황
        List<Map<String, Object>> achievements = achievementDAO.getAllAchievementsWithProgress(memberId);
        data.put("achievements", achievements);
        
        // 3. 통계 요약 (전체 개수, 달성 개수)
        int totalCount = achievements.size();
        int achievedCount = (int) achievements.stream()
                .filter(a -> "Y".equals(a.get("IS_ACHIEVED")))
                .count();
        
        data.put("summary", Map.of(
            "totalCount", totalCount,
            "achievedCount", achievedCount,
            "progressRate", totalCount > 0 ? (int)((double)achievedCount / totalCount * 100) : 0
        ));
        
        return data;
    }

    /**
     * 활동 유형별 기본 경험치 정의
     */
    public enum ActivityType {
        ATTENDANCE(10),
        POST(20),
        COMMENT(5),
        FESTIVAL_LIKE(2),
        FESTIVAL_REVIEW(30),
        AI_PLAN(20),
        RANDOM_PICK(5);

        private final int exp;
        ActivityType(int exp) { this.exp = exp; }
        public int getExp() { return exp; }
    }

    /**
     * 일상적인 활동에 따른 경험치를 지급하고 레벨업을 체크합니다.
     * 다른 서비스에서 활동 성공 시 호출하면 됩니다.
     */
    @Transactional
    public AchievementResultDTO addActivityExp(String memberId, ActivityType type) {
        log.info("활동 경험치 지급 - 유저: {}, 활동: {}, 점수: {}", memberId, type, type.getExp());
        
        AchievementResultDTO result = new AchievementResultDTO("활동 보상", type.name() + " 활동 완료", type.getExp());
        
        // 경험치 지급 및 레벨업 체크 로직 실행
        addExpAndCheckLevelUp(memberId, type.getExp(), result);
        
        // 업적 진행도도 함께 업데이트 (타입명이 일치하므로 재사용 가능)
        updateProgress(memberId, type.name());
        
        return result;
    }

    /**
     * 출석 체크를 처리합니다. (하루 한 번만 실행되도록 호출부에서 제어 필요)
     */
    @Transactional
    public List<AchievementResultDTO> processAttendance(String memberId) {
        log.info("출석 체크 처리 - 유저: {}", memberId);
        
        List<AchievementResultDTO> results = new ArrayList<>();
        
        // 1. 기본 활동 경험치 지급 (addActivityExp 내부에서 updateProgress("ATTENDANCE")를 호출함)
        AchievementResultDTO activityResult = addActivityExp(memberId, ActivityType.ATTENDANCE);
        results.add(activityResult);
        
        // 2. 추가적인 업적 체크가 필요하다면 여기서 수행할 수 있지만, 
        // 이미 updateProgress가 호출되었으므로 바로 반환하면 됩니다.
        
        return results;
    }

    /**
     * 유저의 특정 활동에 따른 업적 진행도를 업데이트하고 달성 여부를 확인합니다.
     */
    @Transactional
    public List<AchievementResultDTO> updateProgress(String memberId, String achType) {
        List<AchievementResultDTO> results = new ArrayList<>();
        
        // 1. 해당 타입의 아직 달성하지 않은 업적 목록 조회
        List<AchievementDTO> targets = achievementDAO.getUnachievedAchievementsByType(memberId, achType);
        
        for (AchievementDTO ach : targets) {
            // 2. 진행도 업데이트 (카운트 +1)
            achievementDAO.upsertProgress(memberId, ach.getAchievement_id());
            
            // 3. 현재 수치 확인
            UserAchProgressDTO progress = achievementDAO.getProgress(memberId, ach.getAchievement_id());
            
            // 4. 달성 조건 충족 시
            if (progress.getCurrent_count() >= ach.getCondition_count()) {
                completeAndReward(memberId, ach, results);
                
                // 5. ALL_CLEAR 업적 체크 (일반 업적 달성 시에만 체크)
                if (!"ALL_CLEAR".equals(achType)) {
                    checkAllClear(memberId, results);
                }
            }
        }
        
        return results;
    }

    /**
     * 업적 달성 처리 및 보상 지급
     */
    private void completeAndReward(String memberId, AchievementDTO ach, List<AchievementResultDTO> results) {
        log.info("업적 달성! 유저: {}, 업적: {}", memberId, ach.getAch_title());
        
        // 업적 완료 상태로 변경 및 이력 저장
        achievementDAO.completeAchievement(memberId, ach.getAchievement_id());
        
        // 보상 결과 객체 생성
        AchievementResultDTO result = new AchievementResultDTO(ach.getAch_title(), ach.getAch_desc(), ach.getExp_reward());
        
        // 경험치 지급 및 레벨업 체크
        addExpAndCheckLevelUp(memberId, ach.getExp_reward(), result);
        
        results.add(result);
    }

    /**
     * 경험치 추가 및 레벨업 로직
     */
    private void addExpAndCheckLevelUp(String memberId, Integer expReward, AchievementResultDTO result) {
        // 경험치 추가
        achievementDAO.addMemberExp(memberId, expReward);
        
        // 현재 정보 조회
        Map<String, Object> currentStatus = achievementDAO.getMemberExpAndLevel(memberId);
        if (currentStatus == null) return;

        Long currentExp = ((Number) currentStatus.get("EXP_POINT")).longValue();
        Integer currentLv = ((Number) currentStatus.get("CURRENT_LV")).intValue();
        
        // 다음 레벨 정보 확인
        LevelSystemDTO nextLvInfo = achievementDAO.getNextLevelInfo(currentLv);
        
        if (nextLvInfo != null && currentExp >= nextLvInfo.getRequired_exp()) {
            // 레벨업 조건 충족!
            achievementDAO.updateMemberLevelAndTitle(memberId, nextLvInfo.getCurrent_lv(), nextLvInfo.getTitle_id());
            
            // 결과에 레벨업 정보 기록
            result.setLeveled_up(true);
            result.setNew_level(nextLvInfo.getCurrent_lv());
            
            // 새 칭호 정보 조회 (필요 시)
            // result.setNew_title(...);
            log.info("레벨업! 유저: {}, 새 레벨: {}", memberId, nextLvInfo.getCurrent_lv());
        }
    }

    /**
     * 모든 업적 클리어 여부 확인 (메타 업적)
     * 일반 업적이 하나 달성될 때마다 호출되어 999번 업적의 진행도를 올립니다.
     */
    private void checkAllClear(String memberId, List<AchievementResultDTO> results) {
        // 1. 999번 업적의 진행도를 1 올림 (일반 업적 하나가 달성되었으므로)
        achievementDAO.upsertProgress(memberId, 999L);

        // 2. 현재 총 달성 개수 확인 (999번은 아직 'Y'가 아니므로 순수 일반 업적 개수)
        int achievedCount = achievementDAO.getAchievedCount(memberId);
        
        // 주신 리스트 기준, ALL_CLEAR 제외 총 24개의 업적이 있음
        if (achievedCount == 24) {
            AchievementDTO allClearAch = achievementDAO.getAchievementById(999L);
            if (allClearAch != null) {
                // 이미 달성했는지 한 번 더 체크 (방어 로직)
                List<AchievementDTO> check = achievementDAO.getUnachievedAchievementsByType(memberId, "ALL_CLEAR");
                if (!check.isEmpty()) {
                    completeAndReward(memberId, allClearAch, results);
                }
            }
        }
    }
}
