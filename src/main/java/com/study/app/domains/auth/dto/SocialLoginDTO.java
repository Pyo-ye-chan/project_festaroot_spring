package com.study.app.domains.auth.dto;

public class SocialLoginDTO {
	
    private String social_provider;
    private String social_id;

    private String email;
    private String nickname;
    private String profile_image_url;
    
	public SocialLoginDTO(String social_provider, String social_id, String email, String nickname,
			String profile_image_url) {
		super();
		this.social_provider = social_provider;
		this.social_id = social_id;
		this.email = email;
		this.nickname = nickname;
		this.profile_image_url = profile_image_url;
	}
	public String getSocial_provider() {
		return social_provider;
	}
	public void setSocial_provider(String social_provider) {
		this.social_provider = social_provider;
	}
	public String getSocial_id() {
		return social_id;
	}
	public void setSocial_id(String social_id) {
		this.social_id = social_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
    
    

}
