package com.study.app.dto;

public class ThemeMasterDTO {
	
	private String theme_code;
	private String theme_name;
	
	public ThemeMasterDTO() {}
	
	public ThemeMasterDTO(String theme_code, String theme_name) {
		super();
		this.theme_code = theme_code;
		this.theme_name = theme_name;
	}

	public String getTheme_code() {
		return theme_code;
	}

	public void setTheme_code(String theme_code) {
		this.theme_code = theme_code;
	}

	public String getTheme_name() {
		return theme_name;
	}

	public void setTheme_name(String theme_name) {
		this.theme_name = theme_name;
	}
	
	
	
	

}
