package com.study.app.dto;

public class PlaceDetailResponse<T> {
    private CommonDetailDTO commonInfo;
    private T specificInfo;

    public PlaceDetailResponse() {}

    public PlaceDetailResponse(CommonDetailDTO commonInfo, T specificInfo) {
        this.commonInfo = commonInfo;
        this.specificInfo = specificInfo;
    }

    public CommonDetailDTO getCommonInfo() {
        return commonInfo;
    }

    public void setCommonInfo(CommonDetailDTO commonInfo) {
        this.commonInfo = commonInfo;
    }

    public T getSpecificInfo() {
        return specificInfo;
    }

    public void setSpecificInfo(T specificInfo) {
        this.specificInfo = specificInfo;
    }
}
