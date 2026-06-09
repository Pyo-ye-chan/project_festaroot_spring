package com.study.app.domains.achievement.dto;

public class AchievementDTO {
    private Long achievement_id;
    private String ach_title;
    private String ach_desc;
    private String ach_type;
    private Integer condition_count;
    private Integer exp_reward;

    public AchievementDTO() {}

    public Long getAchievement_id() { return achievement_id; }
    public void setAchievement_id(Long achievement_id) { this.achievement_id = achievement_id; }
    public String getAch_title() { return ach_title; }
    public void setAch_title(String ach_title) { this.ach_title = ach_title; }
    public String getAch_desc() { return ach_desc; }
    public void setAch_desc(String ach_desc) { this.ach_desc = ach_desc; }
    public String getAch_type() { return ach_type; }
    public void setAch_type(String ach_type) { this.ach_type = ach_type; }
    public Integer getCondition_count() { return condition_count; }
    public void setCondition_count(Integer condition_count) { this.condition_count = condition_count; }
    public Integer getExp_reward() { return exp_reward; }
    public void setExp_reward(Integer exp_reward) { this.exp_reward = exp_reward; }
}
