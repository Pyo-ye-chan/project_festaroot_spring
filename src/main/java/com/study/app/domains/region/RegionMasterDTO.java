package com.study.app.domains.region;

// @Data
public class RegionMasterDTO {
	
	private String region_code;
	private String sigungu_code;
	private String region_name;
	private String sigungu_name;
	
	// 클래스 위에 @Data 어노테이션 달면 Lombok 라이브러리가 아래 기본 코드들을 다 안보이게 생성해줌...
	// 즉, 밑에 @Data 달면 아래에 코드 다 지워도 됨.
	
	@Override
	public String toString() {
		return "RegionMasterDTO [region_code=" + region_code + ", sigungu_code=" + sigungu_code + ", region_name="
				+ region_name + ", sigungu_name=" + sigungu_name + "]";
	}

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
