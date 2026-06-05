package com.study.app.domains.member.dto;

public class InterestRegionDTO {
	
	private String member_id;
	private String region_code;
	private String sigungu_code;
	
	private String region_name;
	
	
	
	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public InterestRegionDTO() {}
	
	public InterestRegionDTO(String member_id, String region_code, String sigungu_code) {
		super();
		this.member_id = member_id;
		this.region_code = region_code;
		this.sigungu_code = sigungu_code;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getRegion_code() {
		return region_code;
	}

	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}

	public String getSigungu_code() {
		return sigungu_code;
	}

	public void setSigungu_code(String sigungu_code) {
		this.sigungu_code = sigungu_code;
	}
	
	
	

}
