package com.study.app.dto;

public class InterestThemeDTO {
	
	private String member_id;
	private String theme_code;
	
	public InterestThemeDTO() {}
	
	public InterestThemeDTO(String member_id, String theme_code) {
		super();
		this.member_id = member_id;
		this.theme_code = theme_code;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getTheme_code() {
		return theme_code;
	}
	public void setTheme_code(String theme_code) {
		this.theme_code = theme_code;
	}
	
	

}
