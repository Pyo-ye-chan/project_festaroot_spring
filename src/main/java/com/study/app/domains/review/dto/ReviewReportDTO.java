package com.study.app.domains.review.dto;

import java.time.LocalDateTime;

public class ReviewReportDTO {
	
    private Long report_id;
    private Long review_id;

    private String member_id;
    private String reason;

    private LocalDateTime created_at;

    public ReviewReportDTO() {}

	public ReviewReportDTO(Long report_id, Long review_id, String member_id, String reason, LocalDateTime created_at) {
		super();
		this.report_id = report_id;
		this.review_id = review_id;
		this.member_id = member_id;
		this.reason = reason;
		this.created_at = created_at;
	}

	public Long getReport_id() {
		return report_id;
	}

	public void setReport_id(Long report_id) {
		this.report_id = report_id;
	}

	public Long getReview_id() {
		return review_id;
	}

	public void setReview_id(Long review_id) {
		this.review_id = review_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
    
    

}
