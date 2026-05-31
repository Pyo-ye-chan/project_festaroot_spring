package com.study.app.domains.festival.dto;

public class EventPlaceDTO {
	
	
	private String eventstartdate;      // 행사 시작일 (YYYYMMDD)
    private String eventenddate;        // 행사 종료일 (YYYYMMDD)
    private String eventplace;          // 행사 장소 (세부 장소 명칭)
    private String usefee;              // 이용 요금 (티켓 가격, 입장료 등)

    // 2단계: 축제 상세 컨텐츠 정보
    private String program;             // 행사 프로그램 (주요 내용)
    private String playtime;            // 공연/행사 시간 (예: 평일 20시 등)
    private String spendtimefestival;   // 관람 소요 시간
    private String agelimit;            // 관람 가능 연령 (연령 제한)
    private String bookingplace;        // 예매처
    private String discountinfofestival;// 할인 정보

    // 3단계: 주최측 연락 정보
    private String sponsor1;            // 주최자 정보
    private String sponsor1tel;         // 주최자 연락처
    
    
    public EventPlaceDTO() {}
    
	public EventPlaceDTO(String eventstartdate, String eventenddate, String eventplace, String usefee, String program,
			String playtime, String spendtimefestival, String agelimit, String bookingplace,
			String discountinfofestival, String sponsor1, String sponsor1tel) {
		super();
		this.eventstartdate = eventstartdate;
		this.eventenddate = eventenddate;
		this.eventplace = eventplace;
		this.usefee = usefee;
		this.program = program;
		this.playtime = playtime;
		this.spendtimefestival = spendtimefestival;
		this.agelimit = agelimit;
		this.bookingplace = bookingplace;
		this.discountinfofestival = discountinfofestival;
		this.sponsor1 = sponsor1;
		this.sponsor1tel = sponsor1tel;
	}
	public String getEventstartdate() {
		return eventstartdate;
	}
	public void setEventstartdate(String eventstartdate) {
		this.eventstartdate = eventstartdate;
	}
	public String getEventenddate() {
		return eventenddate;
	}
	public void setEventenddate(String eventenddate) {
		this.eventenddate = eventenddate;
	}
	public String getEventplace() {
		return eventplace;
	}
	public void setEventplace(String eventplace) {
		this.eventplace = eventplace;
	}
	public String getUsefee() {
		return usefee;
	}
	public void setUsefee(String usefee) {
		this.usefee = usefee;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getPlaytime() {
		return playtime;
	}
	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}
	public String getSpendtimefestival() {
		return spendtimefestival;
	}
	public void setSpendtimefestival(String spendtimefestival) {
		this.spendtimefestival = spendtimefestival;
	}
	public String getAgelimit() {
		return agelimit;
	}
	public void setAgelimit(String agelimit) {
		this.agelimit = agelimit;
	}
	public String getBookingplace() {
		return bookingplace;
	}
	public void setBookingplace(String bookingplace) {
		this.bookingplace = bookingplace;
	}
	public String getDiscountinfofestival() {
		return discountinfofestival;
	}
	public void setDiscountinfofestival(String discountinfofestival) {
		this.discountinfofestival = discountinfofestival;
	}
	public String getSponsor1() {
		return sponsor1;
	}
	public void setSponsor1(String sponsor1) {
		this.sponsor1 = sponsor1;
	}
	public String getSponsor1tel() {
		return sponsor1tel;
	}
	public void setSponsor1tel(String sponsor1tel) {
		this.sponsor1tel = sponsor1tel;
	}
    
    
    

}
