package com.study.app.domains.inquiry.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class InquiryDTO {
	
	private Long inquiry_id;
	private String member_id;
	private String category;
	private String title;
	private String content;
	private String status;
	private LocalDate created_at;
	
	private List<MultipartFile> files;
	private List<InquiryAttachmentDTO> attachments;
	
	public List<InquiryAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<InquiryAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public InquiryDTO() {}
	
	public InquiryDTO(Long inquiry_id, String member_id, String category, String title, String content, String status,
			LocalDate created_at) {
		super();
		this.inquiry_id = inquiry_id;
		this.member_id = member_id;
		this.category = category;
		this.title = title;
		this.content = content;
		this.status = status;
		this.created_at = created_at;
	}
	public Long getInquiry_id() {
		return inquiry_id;
	}
	public void setInquiry_id(Long inquiry_id) {
		this.inquiry_id = inquiry_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDate created_at) {
		this.created_at = created_at;
	}
	
	
	
}
