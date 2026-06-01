package com.study.app.domains.region;

public class RegionMasterDTO {
	
	private String region_code;
	private String sigungu_code;
	private String region_name;
	private String sigungu_name;
	
	public RegionMasterDTO() {}

	public RegionMasterDTO(String region_code, String sigungu_code, String region_name, String sigungu_name) {
		super();
		this.region_code = region_code;
		this.sigungu_code = sigungu_code;
		this.region_name = region_name;
		this.sigungu_name = sigungu_name;
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

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getSigungu_name() {
		return sigungu_name;
	}

	public void setSigungu_name(String sigungu_name) {
		this.sigungu_name = sigungu_name;
	}
	
	

}
