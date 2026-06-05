package com.study.app.domains.review.dto;

import java.time.LocalDateTime;

public class ReviewImageDTO {
	
    private Long image_id;
    private Long review_id;
    private String image_url;

    private LocalDateTime created_at;

    public ReviewImageDTO() {}

	public ReviewImageDTO(Long image_id, Long review_id, String image_url, LocalDateTime created_at) {
		super();
		this.image_id = image_id;
		this.review_id = review_id;
		this.image_url = image_url;
		this.created_at = created_at;
	}

	public Long getImage_id() {
		return image_id;
	}

	public void setImage_id(Long image_id) {
		this.image_id = image_id;
	}

	public Long getReview_id() {
		return review_id;
	}

	public void setReview_id(Long review_id) {
		this.review_id = review_id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
    
    
    
    

}
