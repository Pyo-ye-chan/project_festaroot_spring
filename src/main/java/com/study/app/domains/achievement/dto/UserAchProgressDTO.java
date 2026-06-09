package com.study.app.domains.achievement.dto;

public class UserAchProgressDTO {
    private String member_id;
    private Long achievement_id;
    private Integer current_count;
    private String is_achieved; // 'Y' or 'N'

    public UserAchProgressDTO() {}

    public String getMember_id() { return member_id; }
    public void setMember_id(String member_id) { this.member_id = member_id; }
    public Long getAchievement_id() { return achievement_id; }
    public void setAchievement_id(Long achievement_id) { this.achievement_id = achievement_id; }
    public Integer getCurrent_count() { return current_count; }
    public void setCurrent_count(Integer current_count) { this.current_count = current_count; }
    public String getIs_achieved() { return is_achieved; }
    public void setIs_achieved(String is_achieved) { this.is_achieved = is_achieved; }
}
