package com.study.app.dto;

public class CommonDetailDTO {
    
    private String overview;   // 개요 (장소 설명)
    private String homepage;   // 홈페이지 URL (HTML 태그 포함될 수 있음)
    private String tel;        // 전화번호
    private String telname;    // 전화번호 명칭
    private String zipcode;    // 우편번호
    private String firstimage2;// 썸네일 이미지 URL

    public CommonDetailDTO() {}

    public CommonDetailDTO(String overview, String homepage, String tel, String telname, String zipcode, String firstimage2) {
        this.overview = overview;
        this.homepage = homepage;
        this.tel = tel;
        this.telname = telname;
        this.zipcode = zipcode;
        this.firstimage2 = firstimage2;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelname() {
        return telname;
    }

    public void setTelname(String telname) {
        this.telname = telname;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }
}
