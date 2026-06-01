package com.study.app.domains.member.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberDTO {
	
	private String member_id; // DTO 변수명 snake_case
	private String password;
	private String name;
	private String nickname;
	private String phone;
	private String email;
	private LocalDate birthdate; // date 타입은 LocalDate(날짜) or LocalDateTime(시간)
	private String gender;
	private String addr_sido;
	private String addr_sigungu;
	private String reside_area_code;
	private String reside_sigungu_code;
	private String profile_image_url;
	private String social_provider;
	private Long exp_point; // number 타입은 Long
	private Long title_id;
	private LocalDateTime created_at;
	
	// 추가
	private String social_id;
	
	public MemberDTO() {}

	public MemberDTO(String member_id, String password, String name, String nickname, String phone, String email,
			LocalDate birthdate, String gender, String addr_sido, String addr_sigungu, String reside_area_code,
			String reside_sigungu_code, String profile_image_url, String social_provider, Long exp_point, Long title_id,
			LocalDateTime created_at, String social_id) {
		super();
		this.member_id = member_id;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.phone = phone;
		this.email = email;
		this.birthdate = birthdate;
		this.gender = gender;
		this.addr_sido = addr_sido;
		this.addr_sigungu = addr_sigungu;
		this.reside_area_code = reside_area_code;
		this.reside_sigungu_code = reside_sigungu_code;
		this.profile_image_url = profile_image_url;
		this.social_provider = social_provider;
		this.exp_point = exp_point;
		this.title_id = title_id;
		this.created_at = created_at;
		this.social_id = social_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddr_sido() {
		return addr_sido;
	}

	public void setAddr_sido(String addr_sido) {
		this.addr_sido = addr_sido;
	}

	public String getAddr_sigungu() {
		return addr_sigungu;
	}

	public void setAddr_sigungu(String addr_sigungu) {
		this.addr_sigungu = addr_sigungu;
	}

	public String getReside_area_code() {
		return reside_area_code;
	}

	public void setReside_area_code(String reside_area_code) {
		this.reside_area_code = reside_area_code;
	}

	public String getReside_sigungu_code() {
		return reside_sigungu_code;
	}

	public void setReside_sigungu_code(String reside_sigungu_code) {
		this.reside_sigungu_code = reside_sigungu_code;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getSocial_provider() {
		return social_provider;
	}

	public void setSocial_provider(String social_provider) {
		this.social_provider = social_provider;
	}

	public Long getExp_point() {
		return exp_point;
	}

	public void setExp_point(Long exp_point) {
		this.exp_point = exp_point;
	}

	public Long getTitle_id() {
		return title_id;
	}

	public void setTitle_id(Long title_id) {
		this.title_id = title_id;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	
	public String getSocial_id() {
		return social_id;
	}

	public void setSocial_id(String social_id) {
		this.social_id = social_id;
	}
	
	
	

}