package com.study.app.domains.achievement.dto;

public class TitleDTO {
    private Long title_id;
    private String title_name;
    private String title_desc;

    public TitleDTO() {}

    public Long getTitle_id() { return title_id; }
    public void setTitle_id(Long title_id) { this.title_id = title_id; }
    public String getTitle_name() { return title_name; }
    public void setTitle_name(String title_name) { this.title_name = title_name; }
    public String getTitle_desc() { return title_desc; }
    public void setTitle_desc(String title_desc) { this.title_desc = title_desc; }
}
