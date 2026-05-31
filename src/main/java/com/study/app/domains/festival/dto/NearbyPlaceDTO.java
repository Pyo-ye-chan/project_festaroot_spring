package com.study.app.domains.festival.dto;

/**
 * 한국관광공사 Open API 주변 관광 정보 데이터를 담는 DTO
 */
public class NearbyPlaceDTO {

    private String contentid;      // 장소 고유 ID
    private String title;          // 장소 이름
    private String contenttypeid;  // 장소 유형 ID
    private String firstimage;     // 대표 이미지 URL
    private String addr1;          // 주소
    private Double mapx;           // 경도
    private Double mapy;           // 위도
    private Double dist;           // 기준점으로부터의 거리 (m)

    public NearbyPlaceDTO() {}

    public NearbyPlaceDTO(String contentid, String title, String contenttypeid, String firstimage, String addr1, Double mapx, Double mapy, Double dist) {
        this.contentid = contentid;
        this.title = title;
        this.contenttypeid = contenttypeid;
        this.firstimage = firstimage;
        this.addr1 = addr1;
        this.mapx = mapx;
        this.mapy = mapy;
        this.dist = dist;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public Double getMapx() {
        return mapx;
    }

    public void setMapx(Double mapx) {
        this.mapx = mapx;
    }

    public Double getMapy() {
        return mapy;
    }

    public void setMapy(Double mapy) {
        this.mapy = mapy;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    @Override
    public String toString() {
        return "NearbyPlaceDTO{" +
                "contentid='" + contentid + '\'' +
                ", title='" + title + '\'' +
                ", contenttypeid='" + contenttypeid + '\'' +
                ", firstimage='" + firstimage + '\'' +
                ", addr1='" + addr1 + '\'' +
                ", mapx=" + mapx +
                ", mapy=" + mapy +
                ", dist=" + dist +
                '}';
    }
}
