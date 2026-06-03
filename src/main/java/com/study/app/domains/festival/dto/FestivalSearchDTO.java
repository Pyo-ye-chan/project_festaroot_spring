package com.study.app.domains.festival.dto;

public class FestivalSearchDTO {

	private String keyword;
	private String region_code;
	private String sigungu_code;
	private String event_start_date;
	private String event_end_date;
	private String sort; // 정렬기준
	private int page = 1; // 기본값 1페이지
	private int size = 10; // 한 페이지에 보여줄 개수 (필요시 사용)
	private String searchScope; // title 또는 소개글 포함 검색
	private boolean ongoingOnly; // 프론트에서 ongoingOnly: true/false 보냄. (예: 진행 중인 축제만 보기 토글)

	@Override
	public String toString() {
		return "FestivalSearchDTO [keyword=" + keyword + ", region_code=" + region_code + ", sigungu_code="
				+ sigungu_code + ", event_start_date=" + event_start_date + ", event_end_date=" + event_end_date
				+ ", sort=" + sort + ", page=" + page + ", size=" + size + ", searchScope=" + searchScope
				+ ", ongoingOnly=" + ongoingOnly + "]";
	}

	public FestivalSearchDTO() {}

	public FestivalSearchDTO(String keyword, String region_code, String sigungu_code, String event_start_date,
			String event_end_date, String sort, int page, int size, String searchScope, boolean ongoingOnly) {
		super();
		this.keyword = keyword;
		this.region_code = region_code;
		this.sigungu_code = sigungu_code;
		this.event_start_date = event_start_date;
		this.event_end_date = event_end_date;
		this.sort = sort;
		this.page = page;
		this.size = size;
		this.searchScope = searchScope;
		this.ongoingOnly = ongoingOnly;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getEvent_start_date() {
		return event_start_date;
	}

	public void setEvent_start_date(String event_start_date) {
		this.event_start_date = event_start_date;
	}

	public String getEvent_end_date() {
		return event_end_date;
	}

	public void setEvent_end_date(String event_end_date) {
		this.event_end_date = event_end_date;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSearchScope() {
		return searchScope;
	}

	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}

	public boolean isOngoingOnly() {
		return ongoingOnly;
	}

	public void setOngoingOnly(boolean ongoingOnly) {
		this.ongoingOnly = ongoingOnly;
	}

	// 시작일과 종료일의 유효성(역순 입력 여부)을 검증하는 메서드
	public boolean isValidPeriod() {
		// 날짜를 입력 안 한 경우(전체 조회) 정상 통과, NullPointerException을 방지
		if (event_start_date == null || event_end_date == null) {
			return true;
		}

		// 시작일이 종료일보다 과거이거나 같은 날이면 정상(true), 미래이면 역순 오류(false)
		return event_start_date.compareTo(event_end_date) <= 0;
	}
}