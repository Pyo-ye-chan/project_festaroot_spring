package com.study.app.domains.festival.dto;

public class CultureFacilityDTO {
	
    private String infocenterculture;      // 문의 및 안내
    private String usetimeculture;         // 이용시간
    private String restdateculture;        // 쉬는날
    private String usefee;                 // 이용요금
    private String parkingculture;         // 주차시설
    private String chkpetculture;          // 애완동물 동반가능정보
    private String chkbabycarriageculture; // 유모차 대여정보
    private String spendtime;              // 관람소요시간
    
    
    
    public CultureFacilityDTO() {}
    
	public CultureFacilityDTO(String infocenterculture, String usetimeculture, String restdateculture, String usefee,
			String parkingculture, String chkpetculture, String chkbabycarriageculture, String spendtime) {
		super();
		this.infocenterculture = infocenterculture;
		this.usetimeculture = usetimeculture;
		this.restdateculture = restdateculture;
		this.usefee = usefee;
		this.parkingculture = parkingculture;
		this.chkpetculture = chkpetculture;
		this.chkbabycarriageculture = chkbabycarriageculture;
		this.spendtime = spendtime;
	}
	public String getInfocenterculture() {
		return infocenterculture;
	}
	public void setInfocenterculture(String infocenterculture) {
		this.infocenterculture = infocenterculture;
	}
	public String getUsetimeculture() {
		return usetimeculture;
	}
	public void setUsetimeculture(String usetimeculture) {
		this.usetimeculture = usetimeculture;
	}
	public String getRestdateculture() {
		return restdateculture;
	}
	public void setRestdateculture(String restdateculture) {
		this.restdateculture = restdateculture;
	}
	public String getUsefee() {
		return usefee;
	}
	public void setUsefee(String usefee) {
		this.usefee = usefee;
	}
	public String getParkingculture() {
		return parkingculture;
	}
	public void setParkingculture(String parkingculture) {
		this.parkingculture = parkingculture;
	}
	public String getChkpetculture() {
		return chkpetculture;
	}
	public void setChkpetculture(String chkpetculture) {
		this.chkpetculture = chkpetculture;
	}
	public String getChkbabycarriageculture() {
		return chkbabycarriageculture;
	}
	public void setChkbabycarriageculture(String chkbabycarriageculture) {
		this.chkbabycarriageculture = chkbabycarriageculture;
	}
	public String getSpendtime() {
		return spendtime;
	}
	public void setSpendtime(String spendtime) {
		this.spendtime = spendtime;
	}
	
    
    
    
}
