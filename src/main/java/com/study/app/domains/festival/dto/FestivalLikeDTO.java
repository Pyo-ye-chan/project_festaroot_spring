package com.study.app.domains.festival.dto;

public class FestivalLikeDTO {
	private String member_id;
	private Long content_id;

	@Override
	public String toString() {
		return "FestivalLikeDTO [member_id=" + member_id + ", content_id=" + content_id + "]";
	}

	public FestivalLikeDTO() {}

	public FestivalLikeDTO(String member_id, Long content_id) {
		super();
		this.member_id = member_id;
		this.content_id = content_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(Long content_id) {
		this.content_id = content_id;
	}

}
