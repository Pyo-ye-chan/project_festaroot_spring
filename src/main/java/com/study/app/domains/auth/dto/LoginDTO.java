package com.study.app.domains.auth.dto;

public class LoginDTO {
    private String member_id;
    private String password;

    public LoginDTO() {}

    public LoginDTO(String member_id, String password) {
        this.member_id = member_id;
        this.password = password;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
