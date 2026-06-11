package com.study.app.domains.board.dto;

import java.time.LocalDate;

public class CommunityPostDTO {
	
    private Long post_id;
    private String member_id;
    private String category;
    private String title;
    private String content;
    private Long view_count;
    private Long like_count;
    private Long report_count;
    private LocalDate created_at;
    private LocalDate updated_at;
    
    // 닉네임
    private String nickname;

    public CommunityPostDTO() {}

	public CommunityPostDTO(Long post_id, String member_id, String category, String title, String content,
			Long view_count, Long like_count, Long report_count, LocalDate created_at, LocalDate updated_at,
			String nickname) {
		super();
		this.post_id = post_id;
		this.member_id = member_id;
		this.category = category;
		this.title = title;
		this.content = content;
		this.view_count = view_count;
		this.like_count = like_count;
		this.report_count = report_count;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.nickname = nickname;
	}

	public Long getPost_id() {
		return post_id;
	}

	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getView_count() {
		return view_count;
	}

	public void setView_count(Long view_count) {
		this.view_count = view_count;
	}

	public Long getLike_count() {
		return like_count;
	}

	public void setLike_count(Long like_count) {
		this.like_count = like_count;
	}

	public Long getReport_count() {
		return report_count;
	}

	public void setReport_count(Long report_count) {
		this.report_count = report_count;
	}

	public LocalDate getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDate created_at) {
		this.created_at = created_at;
	}

	public LocalDate getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDate updated_at) {
		this.updated_at = updated_at;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	
    

}
