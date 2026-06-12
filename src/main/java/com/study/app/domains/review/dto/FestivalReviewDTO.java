package com.study.app.domains.review.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FestivalReviewDTO {

    private Long review_id;
    private Long content_id;
    private String member_id;

    private Double rating;
    private String content;

    private LocalDate visit_date;

    private Long report_count;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private String is_deleted;

    // 추가 조회용
    private String nickname;
    private List<FestivalReviewImageDTO> images;

    public FestivalReviewDTO() {}

	public FestivalReviewDTO(Long review_id, Long content_id, String member_id, Double rating, String content,
			LocalDate visit_date, Long report_count, LocalDateTime created_at, LocalDateTime updated_at,
			String is_deleted, String nickname, List<FestivalReviewImageDTO> images) {
		super();
		this.review_id = review_id;
		this.content_id = content_id;
		this.member_id = member_id;
		this.rating = rating;
		this.content = content;
		this.visit_date = visit_date;
		this.report_count = report_count;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.is_deleted = is_deleted;
		this.nickname = nickname;
		this.images = images;
	}

	public Long getReview_id() {
		return review_id;
	}

	public void setReview_id(Long review_id) {
		this.review_id = review_id;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(Long content_id) {
		this.content_id = content_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDate getVisit_date() {
		return visit_date;
	}

	public void setVisit_date(LocalDate visit_date) {
		this.visit_date = visit_date;
	}

	public Long getReport_count() {
		return report_count;
	}

	public void setReport_count(Long report_count) {
		this.report_count = report_count;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public String getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<FestivalReviewImageDTO> getImages() {
		return images;
	}

	public void setImages(List<FestivalReviewImageDTO> images) {
		this.images = images;
	}
    

}
