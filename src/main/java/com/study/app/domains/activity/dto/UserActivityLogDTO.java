package com.study.app.domains.activity.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserActivityLogDTO {
    private Long log_id;
    private String member_id;
    
    @JsonProperty("type")
    private String action_type;
    
    @JsonProperty("festivalId")
    private Long content_id;
    
    @JsonProperty("searchQuery")
    private String keyword;
    
    private LocalDateTime created_at;

    public UserActivityLogDTO() {}

    public UserActivityLogDTO(Long log_id, String member_id, String action_type, Long content_id, String keyword, LocalDateTime created_at) {
        this.log_id = log_id;
        this.member_id = member_id;
        this.action_type = action_type;
        this.content_id = content_id;
        this.keyword = keyword;
        this.created_at = created_at;
    }
    
    private String title;
    
    

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public Long getContent_id() {
        return content_id;
    }

    public void setContent_id(Long content_id) {
        this.content_id = content_id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
