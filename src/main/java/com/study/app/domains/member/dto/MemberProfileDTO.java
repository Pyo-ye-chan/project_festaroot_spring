package com.study.app.domains.member.dto;

import java.util.List;
import com.study.app.domains.activity.dto.UserActivityLogDTO;

import java.util.Map;

public class MemberProfileDTO {
    private MemberDTO member;
    private List<InterestRegionDTO> interestRegions;
    private List<InterestThemeDTO> interestThemes;
    private List<UserActivityLogDTO> recentLogs;
    private List<Map<String, Object>> likedFestivals;
    
    // 성장 관련 추가 정보
    private Integer level;
    private String titleName;
    private Long currentExp;
    private Long nextLevelExp;

    public MemberProfileDTO() {}

    public MemberProfileDTO(MemberDTO member, List<InterestRegionDTO> interestRegions, List<InterestThemeDTO> interestThemes, 
                            List<UserActivityLogDTO> recentLogs, List<Map<String, Object>> likedFestivals) {
        this.member = member;
        this.interestRegions = interestRegions;
        this.interestThemes = interestThemes;
        this.recentLogs = recentLogs;
        this.likedFestivals = likedFestivals;
    }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getTitleName() { return titleName; }
    public void setTitleName(String titleName) { this.titleName = titleName; }
    public Long getCurrentExp() { return currentExp; }
    public void setCurrentExp(Long currentExp) { this.currentExp = currentExp; }
    public Long getNextLevelExp() { return nextLevelExp; }
    public void setNextLevelExp(Long nextLevelExp) { this.nextLevelExp = nextLevelExp; }

    public List<Map<String, Object>> getLikedFestivals() {
        return likedFestivals;
    }

    public void setLikedFestivals(List<Map<String, Object>> likedFestivals) {
        this.likedFestivals = likedFestivals;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public List<InterestRegionDTO> getInterestRegions() {
        return interestRegions;
    }

    public void setInterestRegions(List<InterestRegionDTO> interestRegions) {
        this.interestRegions = interestRegions;
    }

    public List<InterestThemeDTO> getInterestThemes() {
        return interestThemes;
    }

    public void setInterestThemes(List<InterestThemeDTO> interestThemes) {
        this.interestThemes = interestThemes;
    }

    public List<UserActivityLogDTO> getRecentLogs() {
        return recentLogs;
    }

    public void setRecentLogs(List<UserActivityLogDTO> recentLogs) {
        this.recentLogs = recentLogs;
    }
}
