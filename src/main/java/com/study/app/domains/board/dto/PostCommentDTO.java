package com.study.app.domains.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostCommentDTO {
	
    private Long comment_id;
    private Long post_id;
    private String member_id;
    private String content;
    private Long parent_comment_id; // 부모 댓글 없으면 null
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    
    private String nickname;
    private List<PostCommentDTO> children;
    
    public PostCommentDTO() {}

	public PostCommentDTO(Long comment_id, Long post_id, String member_id, String content, Long parent_comment_id,
			LocalDateTime created_at, LocalDateTime updated_at, String nickname, List<PostCommentDTO> children) {
		super();
		this.comment_id = comment_id;
		this.post_id = post_id;
		this.member_id = member_id;
		this.content = content;
		this.parent_comment_id = parent_comment_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.nickname = nickname;
		this.children = children;
	}

	public Long getComment_id() {
		return comment_id;
	}

	public void setComment_id(Long comment_id) {
		this.comment_id = comment_id;
	}

	public Long getPost_id() {
		return post_id;
	}

	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getParent_comment_id() {
		return parent_comment_id;
	}

	public void setParent_comment_id(Long parent_comment_id) {
		this.parent_comment_id = parent_comment_id;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<PostCommentDTO> getChildren() {
		return children;
	}

	public void setChildren(List<PostCommentDTO> children) {
		this.children = children;
	}





}
