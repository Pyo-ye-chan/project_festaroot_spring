package com.study.app.dto;

public class TourPlaceDTO {
	
	// 1단계: 필수 이용 정보
    private String usetime;           // 이용 시간 (관람 시간)
    private String restdate;          // 쉬는 날 (휴무일)
    private String infocenter;        // 문의 및 안내 (고객센터 전화번호)

    // 2단계: 체험 및 편의 시설 정보 (축제 방문객 맞춤형)
    private String parking;           // 주차 시설 여부/안내
    private String chkpet;            // 반려동물 동반 가능 여부
    private String chkbabycarriage;   // 유모차 대여 여부
    private String expguide;          // 체험 안내 (운영 프로그램 등)
    private String expagerange;       // 체험 가능 연령
    
    
    public TourPlaceDTO() {}
    
	public TourPlaceDTO(String usetime, String restdate, String infocenter, String parking, String chkpet,
			String chkbabycarriage, String expguide, String expagerange) {
		super();
		this.usetime = usetime;
		this.restdate = restdate;
		this.infocenter = infocenter;
		this.parking = parking;
		this.chkpet = chkpet;
		this.chkbabycarriage = chkbabycarriage;
		this.expguide = expguide;
		this.expagerange = expagerange;
	}
	public String getUsetime() {
		return usetime;
	}
	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}
	public String getRestdate() {
		return restdate;
	}
	public void setRestdate(String restdate) {
		this.restdate = restdate;
	}
	public String getInfocenter() {
		return infocenter;
	}
	public void setInfocenter(String infocenter) {
		this.infocenter = infocenter;
	}
	public String getParking() {
		return parking;
	}
	public void setParking(String parking) {
		this.parking = parking;
	}
	public String getChkpet() {
		return chkpet;
	}
	public void setChkpet(String chkpet) {
		this.chkpet = chkpet;
	}
	public String getChkbabycarriage() {
		return chkbabycarriage;
	}
	public void setChkbabycarriage(String chkbabycarriage) {
		this.chkbabycarriage = chkbabycarriage;
	}
	public String getExpguide() {
		return expguide;
	}
	public void setExpguide(String expguide) {
		this.expguide = expguide;
	}
	public String getExpagerange() {
		return expagerange;
	}
	public void setExpagerange(String expagerange) {
		this.expagerange = expagerange;
	}
    
    

}
