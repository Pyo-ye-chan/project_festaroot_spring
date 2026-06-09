package com.study.app.domains.achievement.dto;

public class LevelSystemDTO {
    private Integer current_lv;
    private Long required_exp;
    private Long title_id;

    public LevelSystemDTO() {}

    public Integer getCurrent_lv() { return current_lv; }
    public void setCurrent_lv(Integer current_lv) { this.current_lv = current_lv; }
    public Long getRequired_exp() { return required_exp; }
    public void setRequired_exp(Long required_exp) { this.required_exp = required_exp; }
    public Long getTitle_id() { return title_id; }
    public void setTitle_id(Long title_id) { this.title_id = title_id; }
}
