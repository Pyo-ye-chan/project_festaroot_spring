package com.study.app.domains.achievement.dto;

import java.util.List;

/**
 * 업적 달성 시 프론트엔드에 전달할 결과 요약 DTO
 */
public class AchievementResultDTO {
    private String ach_title;
    private String ach_desc;
    private Integer exp_reward;
    private boolean leveled_up;
    private Integer new_level;
    private String new_title;

    public AchievementResultDTO() {}

    public AchievementResultDTO(String ach_title, String ach_desc, Integer exp_reward) {
        this.ach_title = ach_title;
        this.ach_desc = ach_desc;
        this.exp_reward = exp_reward;
    }

    public String getAch_title() { return ach_title; }
    public void setAch_title(String ach_title) { this.ach_title = ach_title; }
    public String getAch_desc() { return ach_desc; }
    public void setAch_desc(String ach_desc) { this.ach_desc = ach_desc; }
    public Integer getExp_reward() { return exp_reward; }
    public void setExp_reward(Integer exp_reward) { this.exp_reward = exp_reward; }
    public boolean isLeveled_up() { return leveled_up; }
    public void setLeveled_up(boolean leveled_up) { this.leveled_up = leveled_up; }
    public Integer getNew_level() { return new_level; }
    public void setNew_level(Integer new_level) { this.new_level = new_level; }
    public String getNew_title() { return new_title; }
    public void setNew_title(String new_title) { this.new_title = new_title; }
}
