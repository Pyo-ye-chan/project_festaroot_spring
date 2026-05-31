package com.study.app.domains.festival.dto;

public class FoodPlaceDTO {
	
	
	private String firstmenu;         // 대표 메뉴
    private String treatmenu;         // 취급 메뉴 (전체 메뉴판 항목들)
    private String opentimefood;       // 영업 시간
    private String restdatefood;      // 쉬는 날 (휴무일)
    private String infocenterfood;     // 문의처 (전화번호)
	
    public FoodPlaceDTO() {}
    
    public FoodPlaceDTO(String firstmenu, String treatmenu, String opentimefood, String restdatefood,
			String infocenterfood) {
		super();
		this.firstmenu = firstmenu;
		this.treatmenu = treatmenu;
		this.opentimefood = opentimefood;
		this.restdatefood = restdatefood;
		this.infocenterfood = infocenterfood;
	}
	public String getFirstmenu() {
		return firstmenu;
	}
	public void setFirstmenu(String firstmenu) {
		this.firstmenu = firstmenu;
	}
	public String getTreatmenu() {
		return treatmenu;
	}
	public void setTreatmenu(String treatmenu) {
		this.treatmenu = treatmenu;
	}
	public String getOpentimefood() {
		return opentimefood;
	}
	public void setOpentimefood(String opentimefood) {
		this.opentimefood = opentimefood;
	}
	public String getRestdatefood() {
		return restdatefood;
	}
	public void setRestdatefood(String restdatefood) {
		this.restdatefood = restdatefood;
	}
	public String getInfocenterfood() {
		return infocenterfood;
	}
	public void setInfocenterfood(String infocenterfood) {
		this.infocenterfood = infocenterfood;
	}
    
    

}
