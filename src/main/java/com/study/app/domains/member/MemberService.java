package com.study.app.domains.member;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.achievement.AchievementDAO;
import com.study.app.domains.achievement.AchievementService;
import com.study.app.domains.achievement.dto.LevelSystemDTO;
import com.study.app.domains.festival.FestivalDAO;
import com.study.app.domains.member.dto.InterestRegionDTO;
import com.study.app.domains.member.dto.InterestThemeDTO;
import com.study.app.domains.member.dto.MemberDTO;
import com.study.app.domains.member.dto.MemberProfileDTO;
import com.study.app.domains.storage.UploadService;
import com.study.app.utils.JWTUtil;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private com.study.app.domains.activity.UserActivityLogDAO userActivityLogDAO;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private FestivalDAO festivalDAO;
    
    @Autowired
    private AchievementDAO achievementDAO;
    
    @Autowired
    private AchievementService achievementService;

    public MemberProfileDTO getProfile(String member_id) {
        MemberDTO member = memberDAO.selectMemberById(member_id);
        if (member == null) return null;

        List<InterestRegionDTO> regions = memberDAO.selectInterestRegions(member_id);
        List<InterestThemeDTO> themes = memberDAO.selectInterestThemes(member_id);
        List<com.study.app.domains.activity.dto.UserActivityLogDTO> logs = userActivityLogDAO.selectRecentLogs(member_id);
        
        // 성장 정보 조회
        Map<String, Object> expAndLevel = achievementDAO.getMemberExpAndLevel(member_id);
        
        // 찜한 축제 원본 데이터 (CLOB 포함 가능성 있음)
        List<Map<String, Object>> rawLikedFestivals = festivalDAO.getMyFestivalLikedDetails(member_id);
        
        // JSON 변환을 위해 CLOB를 String으로 가공
        List<Map<String, Object>> likedFestivals = new ArrayList<>();
        if (rawLikedFestivals != null) {
            for (Map<String, Object> row : rawLikedFestivals) {
                Map<String, Object> cleanRow = new HashMap<>();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    cleanRow.put(entry.getKey(), convertToString(entry.getValue()));
                }
                likedFestivals.add(cleanRow);
            }
        }

        MemberProfileDTO profile = new MemberProfileDTO(member, regions, themes, logs, likedFestivals);
        
        // 성장 정보 세팅
        if (expAndLevel != null) {
            Integer currentLv = expAndLevel.get("CURRENT_LV") != null ? ((Number) expAndLevel.get("CURRENT_LV")).intValue() : 1;
            profile.setLevel(currentLv);
            profile.setTitleName((String) expAndLevel.get("TITLE_NAME"));
            profile.setCurrentExp(expAndLevel.get("EXP_POINT") != null ? ((Number) expAndLevel.get("EXP_POINT")).longValue() : 0L);
            
            // 다음 레벨 필요 경험치 조회
            LevelSystemDTO nextLv = achievementDAO.getNextLevelInfo(currentLv);
            if (nextLv != null) {
                profile.setNextLevelExp(nextLv.getRequired_exp());
            }
        }

        return profile;
    }

    private String convertToString(Object obj) {
        if (obj == null) return "";
        if (obj instanceof Clob) {
            try {
                Clob clob = (Clob) obj;
                StringBuilder sb = new StringBuilder();
                try (java.io.Reader reader = clob.getCharacterStream();
                     BufferedReader br = new BufferedReader(reader)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                return sb.toString().trim();
            } catch (Exception e) {
                log.error("CLOB 변환 오류: {}", e.getMessage());
                return "";
            }
        }
        return String.valueOf(obj);
    }

    /**
     * FormData 등으로 들어온 지저분한 리스트 데이터를 정제합니다.
     * 예: ["THEME001"] -> THEME001
     */
    private List<String> cleanList(List<String> rawList) {
        if (rawList == null || rawList.isEmpty()) return rawList;
        
        Set<String> cleanedSet = new HashSet<>();
        for (String item : rawList) {
            if (item == null) continue;
            
            // JSON 배열 형태인 경우 ([", "], [ 등 제거)
            String cleaned = item.replaceAll("[\\[\\]\"]", "");
            
            // 쉼표로 구분된 여러 값이 들어온 경우 분리
            String[] parts = cleaned.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    cleanedSet.add(trimmed);
                }
            }
        }
        return new ArrayList<>(cleanedSet);
    }

    @Transactional
    public int signup(MemberDTO memberDTO) {

        memberDTO.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));

        if (memberDTO.getReside_area_code() == null) {
            memberDTO.setReside_area_code("0");
        }

        if (memberDTO.getReside_sigungu_code() == null) {
            memberDTO.setReside_sigungu_code("0");
        }

        if (memberDTO.getSocial_provider() == null) {
            memberDTO.setSocial_provider("LOCAL");
        }

        if (memberDTO.getProfile_image_url() == null) {
            memberDTO.setProfile_image_url("");
        }

        if (memberDTO.getTitle_id() == null) {
            memberDTO.setTitle_id(1L);
        }

        int result = memberDAO.insertMember(memberDTO);

        if (result > 0) {
            List<String> cleanedThemes = cleanList(memberDTO.getThemes());
            if (cleanedThemes != null) {
                for (String themeCode : cleanedThemes) {
                    memberDAO.insertInterestTheme(new InterestThemeDTO(memberDTO.getMember_id(), themeCode));
                }
            }

            List<String> cleanedRegions = cleanList(memberDTO.getRegions());
            if (cleanedRegions != null) {
                for (String regionCode : cleanedRegions) {
                    memberDAO.insertInterestRegion(new InterestRegionDTO(memberDTO.getMember_id(), regionCode, null));
                }
            }
        }
        return result;
    }

    @Transactional
    public Map<String, Object> updateProfile(String member_id, MemberDTO memberDTO, MultipartFile profileImage) {
        Map<String, Object> response = new HashMap<>();
        List<com.study.app.domains.achievement.dto.AchievementResultDTO> achievementResults = new ArrayList<>();
        
        // 이미지 업로드 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String imageUrl = uploadService.upload(profileImage, "profile");
                memberDTO.setProfile_image_url(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        memberDTO.setMember_id(member_id);
        int result = memberDAO.updateMember(memberDTO);

        if (result > 0) {
            // Update Interest Themes
            memberDAO.deleteInterestThemes(member_id);
            List<String> cleanedThemes = cleanList(memberDTO.getThemes());
            if (cleanedThemes != null) {
                for (String themeCode : cleanedThemes) {
                    memberDAO.insertInterestTheme(new InterestThemeDTO(member_id, themeCode));
                }
            }

            // Update Interest Regions
            memberDAO.deleteInterestRegions(member_id);
            List<String> cleanedRegions = cleanList(memberDTO.getRegions());
            if (cleanedRegions != null) {
                for (String regionCode : cleanedRegions) {
                    memberDAO.insertInterestRegion(new InterestRegionDTO(member_id, regionCode, null));
                }
            }
            
            // 업적 체크 및 결과 저장
            if(profileImage != null && !profileImage.isEmpty() && memberDTO.getProfile_image_url() != null) {
                achievementResults = achievementService.updateProgress(member_id, "PROFILE");
            }
        }
        
        response.put("success", result > 0);
        response.put("achievements", achievementResults);
        return response;
    }

    /**
     * 출석 체크를 처리하고 업적 결과를 반환합니다.
     */
    @Transactional
    public Map<String, Object> checkAndProcessAttendance(String memberId) {
        Map<String, Object> response = new HashMap<>();
        List<com.study.app.domains.achievement.dto.AchievementResultDTO> achievements = new ArrayList<>();
        
        // 1. 오늘 이미 출석했는지 확인
        int todayLogCount = userActivityLogDAO.checkTodayAttendance(memberId);
        
        if (todayLogCount == 0) {
            // 2. 오늘 첫 방문이라면 로그 저장
            com.study.app.domains.activity.dto.UserActivityLogDTO log = new com.study.app.domains.activity.dto.UserActivityLogDTO();
            log.setMember_id(memberId);
            log.setAction_type("ATTENDANCE");
            userActivityLogDAO.insertLog(log);
            
            // 3. 업적 서비스 호출 (경험치 지급 및 업적 체크)
            achievements = achievementService.processAttendance(memberId);
            
            response.put("success", true);
            response.put("message", "출석 체크 완료!");
        } else {
            response.put("success", false);
            response.put("message", "이미 오늘 출석 체크를 하셨습니다.");
        }
        
        response.put("achievements", achievements);
        return response;
    }
}