package com.study.app.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.study.app.dao.RegionMasterDAO;
import com.study.app.dto.RegionMasterDTO;

@Service
public class RegionMasterService {

    private final RestTemplate restTemplate;
    private final RegionMasterDAO regionMasterDAO;

    @Value("${tour.api.key}")
    private String serviceKey;

    public RegionMasterService(
            RestTemplate restTemplate,
            RegionMasterDAO regionMasterDAO
    ) {
        this.restTemplate = restTemplate;
        this.regionMasterDAO = regionMasterDAO;
    }

    public int syncRegionCodes() {
        int totalCount = 0;

        String sidoUrl =
                "https://apis.data.go.kr/B551011/KorService2/ldongCode2"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=100"
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=FestaRoute"
                + "&_type=json"
                + "&lDongListYn=N";

        Map<String, Object> sidoResponse =
                restTemplate.getForObject(sidoUrl, Map.class);

        Map response = (Map) sidoResponse.get("response");
        Map body = (Map) response.get("body");
        Map items = (Map) body.get("items");

        Object itemObj = items.get("item");

        List<Map<String, Object>> sidoList;

        if (itemObj instanceof List) {
            sidoList = (List<Map<String, Object>>) itemObj;
        } else {
            sidoList = List.of((Map<String, Object>) itemObj);
        }

        for (Map<String, Object> sido : sidoList) {
            String regionCode = String.valueOf(sido.get("code"));
            String regionName = String.valueOf(sido.get("name"));

            totalCount += syncSigungu(regionCode, regionName);
        }

        System.out.println("총 저장 건수 = " + totalCount);
        return totalCount;
    }

    private int syncSigungu(String regionCode, String regionName) {
        int count = 0;

        String sigunguUrl =
                "https://apis.data.go.kr/B551011/KorService2/ldongCode2"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=100"
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=FestaRoute"
                + "&_type=json"
                + "&lDongRegnCd=" + regionCode
                + "&lDongListYn=N";

        Map<String, Object> sigunguResponse =
                restTemplate.getForObject(sigunguUrl, Map.class);

        Map response = (Map) sigunguResponse.get("response");
        Map body = (Map) response.get("body");
        Map items = (Map) body.get("items");

        Object itemObj = items.get("item");

        List<Map<String, Object>> sigunguList;

        if (itemObj instanceof List) {
            sigunguList = (List<Map<String, Object>>) itemObj;
        } else {
            sigunguList = List.of((Map<String, Object>) itemObj);
        }

        for (Map<String, Object> sigungu : sigunguList) {
            RegionMasterDTO dto = new RegionMasterDTO();

            dto.setRegion_code(regionCode);
            dto.setRegion_name(regionName);
            dto.setSigungu_code(String.valueOf(sigungu.get("code")));
            dto.setSigungu_name(String.valueOf(sigungu.get("name")));

            int result = regionMasterDAO.insertRegion(dto);
            count += result;

            System.out.println("저장: " + regionName + " "
                    + dto.getSigungu_name() + " / result=" + result);
        }

        return count;
    }
    
    public List<RegionMasterDTO> getSidoList() {
    	return regionMasterDAO.selectAllSido();
    }
    
    public List<RegionMasterDTO> getSigunguList(String regionCode) {
    	return regionMasterDAO.selectAllSigungu(regionCode);
    	
    }
}