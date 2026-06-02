package com.study.app.domains.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.study.app.domains.region.RegionMasterDTO;
import com.study.app.domains.region.RegionMasterMapper;


@Service
public class RegionMasterService {

	private final RestTemplate restTemplate;
	private final RegionMasterMapper regionMasterMapper;

	@Value("${tour.api.key}")
	private String serviceKey;

	public RegionMasterService(RestTemplate restTemplate, RegionMasterMapper regionMasterMapper) {
		this.restTemplate = restTemplate;
		this.regionMasterMapper = regionMasterMapper;
	}

	// DB에 값이 있는 지 확인
	public int selectRegionCount() {
		return regionMasterMapper.selectRegionCount();
	}

	public void initRegionAndSigunguData() {
		int region = selectRegionCount(); 
		//System.out.println("DB에 등록된 지역 행 수 : " + region);

		if (region == 0) { 
			// 이제 이 메서드 하나로 시도와 시군구를 한 방에 동기화
			syncRegionAndSigungu(); 
		}
	}

	public void syncRegionAndSigungu() {
		int totalCount = 0;

		// numOfRows=500으로 지정해서 단 한 번의 호출로 전국의 모든 데이터를 가져옴
		String url = "https://apis.data.go.kr/B551011/KorService2/ldongCode2" + "?serviceKey=" + serviceKey
				+ "&numOfRows=500" + "&pageNo=1" + "&MobileOS=ETC" + "&MobileApp=FestaRoute" + "&_type=json"
				+ "&lDongListYn=Y";

		Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);

		Map response = (Map) apiResponse.get("response");
		Map body = (Map) response.get("body");

		if (body == null) {
			System.out.println("[관광공사 API] 응답 body가 비어있습니다.");
			return;
		}

		List<Map<String, Object>> regionList = new ArrayList<>();
		Object itemsObj = body.get("items");

		// 빈 문자열("") 버그 방지를 위한 안전장치
		if (itemsObj instanceof Map) {
			Map items = (Map) itemsObj;
			Object itemObj = items.get("item");

			if (itemObj instanceof List) {
				regionList = (List<Map<String, Object>>) itemObj;
				
			} else if (itemObj instanceof Map) {
				regionList = List.of((Map<String, Object>) itemObj);
			}
		} else {
			System.out.println("가져올 지역 데이터가 존재하지 않습니다.");
			return;
		}

		// 실제 수신된 JSON 키값으로 매핑 진행
		for (Map<String, Object> region : regionList) {
			RegionMasterDTO dto = new RegionMasterDTO();

			// 보내준 실제 JSON 키값 반영!
			dto.setRegion_code(String.valueOf(region.get("lDongRegnCd")));
			dto.setRegion_name(String.valueOf(region.get("lDongRegnNm")));
			dto.setSigungu_code(String.valueOf(region.get("lDongSignguCd")));
			dto.setSigungu_name(String.valueOf(region.get("lDongSignguNm")));

			// 세종특별자치시처럼 하위 시군구가 없어 null 문자열이 찍히는 경우 예외 처리
			if ("null".equals(dto.getSigungu_code()) || dto.getSigungu_code() == null) {
				dto.setSigungu_code("");
				dto.setSigungu_name("");
			}

			int result = regionMasterMapper.insertRegion(dto);
			totalCount += result;
		}

		System.out.println("전국의 모든 시도/시군구 동기화가 성공적으로 완료되었습니다! 총 저장 건수 = " + totalCount);
	}

	public List<RegionMasterDTO> getSidoList() {
		return regionMasterMapper.selectAllSido();
	}

	public List<RegionMasterDTO> getSigunguList(String regionCode) {
		return regionMasterMapper.selectAllSigungu(regionCode);
	}
}