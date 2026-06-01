package com.study.app.domains.auth.dto;

import java.time.LocalDateTime;

public class LoginHistoryDTO {
	
    private Long log_id;
    private String member_id;
    private String login_type;
    private String login_ip;
    private String login_device;
    private String login_status;
    private LocalDateTime login_at;
    
    public LoginHistoryDTO() {};
    
	public LoginHistoryDTO(Long log_id, String member_id, String login_type, String login_ip, String login_device,
			String login_status, LocalDateTime login_at) {
		super();
		this.log_id = log_id;
		this.member_id = member_id;
		this.login_type = login_type;
		this.login_ip = login_ip;
		this.login_device = login_device;
		this.login_status = login_status;
		this.login_at = login_at;
	}
	public Long getLog_id() {
		return log_id;
	}
	public void setLog_id(Long log_id) {
		this.log_id = log_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getLogin_type() {
		return login_type;
	}
	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
	public String getLogin_device() {
		return login_device;
	}
	public void setLogin_device(String login_device) {
		this.login_device = login_device;
	}
	public String getLogin_status() {
		return login_status;
	}
	public void setLogin_status(String login_status) {
		this.login_status = login_status;
	}
	public LocalDateTime getLogin_at() {
		return login_at;
	}
	public void setLogin_at(LocalDateTime login_at) {
		this.login_at = login_at;
	}
    
    

}
