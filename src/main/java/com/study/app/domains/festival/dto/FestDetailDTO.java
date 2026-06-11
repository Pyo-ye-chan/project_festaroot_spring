package com.study.app.domains.festival.dto;

import java.util.List;

import com.study.app.domains.theme.ThemeMasterDTO;

public class FestDetailDTO {
	
    private Long content_id;
    private String title;
    private String addr1;
    private String addr2;
    private String region_code;
    private String sigungu_code;
    private String first_image;
    private String first_image2;

    private Double map_x;
    private Double map_y;
    private Integer map_level;

    private String event_start_date;
    private String event_end_date;

    private String overview;
    private String spon_place;
    private String use_time_festival;
    private String sponsor1_tel;
    private String homepage;

    private String created_time;
    private String modified_time;

    private Long view_count;
    private Long like_count;
    private Long review_count;
    private Double rating_avg;
    
    private List<ThemeMasterDTO> themes;

    public FestDetailDTO() {}

	public FestDetailDTO(Long content_id, String title, String addr1, String addr2, String region_code,
			String sigungu_code, String first_image, String first_image2, Double map_x, Double map_y, Integer map_level,
			String event_start_date, String event_end_date, String overview, String spon_place,
			String use_time_festival, String sponsor1_tel, String homepage, String created_time, String modified_time,
			Long view_count, Long like_count, Long review_count, Double rating_avg) {
		super();
		this.content_id = content_id;
		this.title = title;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.region_code = region_code;
		this.sigungu_code = sigungu_code;
		this.first_image = first_image;
		this.first_image2 = first_image2;
		this.map_x = map_x;
		this.map_y = map_y;
		this.map_level = map_level;
		this.event_start_date = event_start_date;
		this.event_end_date = event_end_date;
		this.overview = overview;
		this.spon_place = spon_place;
		this.use_time_festival = use_time_festival;
		this.sponsor1_tel = sponsor1_tel;
		this.homepage = homepage;
		this.created_time = created_time;
		this.modified_time = modified_time;
		this.view_count = view_count;
		this.like_count = like_count;
		this.review_count = review_count;
		this.rating_avg = rating_avg;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(Long content_id) {
		this.content_id = content_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
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

	public String getFirst_image() {
		return first_image;
	}

	public void setFirst_image(String first_image) {
		this.first_image = first_image;
	}

	public String getFirst_image2() {
		return first_image2;
	}

	public void setFirst_image2(String first_image2) {
		this.first_image2 = first_image2;
	}

	public Double getMap_x() {
		return map_x;
	}

	public void setMap_x(Double map_x) {
		this.map_x = map_x;
	}

	public Double getMap_y() {
		return map_y;
	}

	public void setMap_y(Double map_y) {
		this.map_y = map_y;
	}

	public Integer getMap_level() {
		return map_level;
	}

	public void setMap_level(Integer map_level) {
		this.map_level = map_level;
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

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getSpon_place() {
		return spon_place;
	}

	public void setSpon_place(String spon_place) {
		this.spon_place = spon_place;
	}

	public String getUse_time_festival() {
		return use_time_festival;
	}

	public void setUse_time_festival(String use_time_festival) {
		this.use_time_festival = use_time_festival;
	}

	public String getSponsor1_tel() {
		return sponsor1_tel;
	}

	public void setSponsor1_tel(String sponsor1_tel) {
		this.sponsor1_tel = sponsor1_tel;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getModified_time() {
		return modified_time;
	}

	public void setModified_time(String modified_time) {
		this.modified_time = modified_time;
	}

	public Long getView_count() {
		return view_count;
	}

	public void setView_count(Long view_count) {
		this.view_count = view_count;
	}

	public Long getLike_count() {
		return like_count;
	}

	public void setLike_count(Long like_count) {
		this.like_count = like_count;
	}

	public Long getReview_count() {
		return review_count;
	}

	public void setReview_count(Long review_count) {
		this.review_count = review_count;
	}

	public Double getRating_avg() {
		return rating_avg;
	}

	public void setRating_avg(Double rating_avg) {
		this.rating_avg = rating_avg;
	}

	public java.util.List<com.study.app.domains.theme.ThemeMasterDTO> getThemes() {
		return themes;
	}

	public void setThemes(java.util.List<com.study.app.domains.theme.ThemeMasterDTO> themes) {
		this.themes = themes;
	}
    
    
	

}
