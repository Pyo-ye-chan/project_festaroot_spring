package com.study.app.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.app.dao.FestivalDAO;
import com.study.app.dto.FestivalDTO;
import com.study.app.dto.NearbyPlaceDTO;

@Service
public class FestivalService {

	@Autowired
	private FestivalDAO fdao;
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${kto.service.key}")
	private String serviceKey;

	public List<FestivalDTO> getAllFestival(){
		return fdao.getAllFestival();
	}
	
	public FestivalDTO selectByContentId(String contentId) {
		return fdao.selectByContentId(contentId);
	}

	public List<NearbyPlaceDTO> getNearbyPlaces(Double lat, Double lng, Integer radius, String contentTypeId) {
		List<NearbyPlaceDTO> list = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        
        // 1. 프론트엔드가 넘겨준 파라미터 방어 코드
        if (radius == null) radius = 5000; // 기본값 5km
        if (contentTypeId == null || contentTypeId.isEmpty()) contentTypeId = "12"; // 기본값 관광지(12)
        
     // 💡 2. contenttypeid 소수점 및 공백 완벽 제거 (★핵심 패치)
        String cleanContentTypeId = "";
        if (contentTypeId != null && !contentTypeId.toString().trim().isEmpty() && !contentTypeId.toString().equals("null")) {
            String rawStr = contentTypeId.toString().trim();
            
            // 혹시라도 "12.0" 처럼 소수점이 붙어 들어왔다면 앞의 정수 부분만 잘라냅니다.
            if (rawStr.contains(".")) {
                cleanContentTypeId = rawStr.split("\\.")[0];
            } else {
                cleanContentTypeId = rawStr;
            }
        }

        try {
        	URI uri = UriComponentsBuilder
        	        .fromUriString("https://apis.data.go.kr/B551011/KorService2/locationBasedList2")
        	        .queryParam("serviceKey", serviceKey)
        	        .queryParam("numOfRows", 30)
        	        .queryParam("pageNo", 1)
        	        .queryParam("MobileOS", "ETC")
        	        .queryParam("MobileApp", "AppTest")
        	        .queryParam("_type", "json")
        	        //.queryParam("listYN", "Y")
        	        .queryParam("arrange", "E") 
        	        .queryParam("mapX", lng)    // 경도
        	        .queryParam("mapY", lat)    // 위도
        	        .queryParam("radius", radius)
        	        // 💡 contentTypeId (X) -> 전부 소문자인 contenttypeid (O)로 변경해야 합니다.
        	        .queryParam("contentTypeId", cleanContentTypeId) 
        	        .build(true) 
        	        .toUri();
            // 3. API 서버 호출
            String response = restTemplate.getForObject(uri, String.class);
            //System.out.println(response);
            
            // 4. 공공 API 자체 인증 오류 처리 (JSON 요청했으나 에러 시 XML로 뱉는 현상 방어)
            if (response != null && response.trim().startsWith("<")) {
                System.err.println("❌ [TourAPI 에러] JSON이 아닌 XML 에러 응답이 반환되었습니다.");
                System.err.println("👉 응답 내용: " + response);
                return list; // 빈 리스트 반환하여 프론트단 다운 방지
            }

            // 5. JSON 데이터 계층 파싱 시작
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            
            JsonNode responseNode = root.path("response");
            JsonNode bodyNode = responseNode.path("body");
            JsonNode itemsNode = bodyNode.path("items");

            // 6. 결과 데이터가 아예 없는 경우 예외 처리
            // itemsNode가 아예 없거나, 데이터가 없어 빈 문자열("")로 들어온 경우만 체크합니다.
            // .asText()는 노드가 객체나 배열일 때도 ""을 반환하므로 .isTextual() 체크가 반드시 필요합니다.
            if (bodyNode.isMissingNode() || itemsNode.isMissingNode() || (itemsNode.isTextual() && itemsNode.asText().equals(""))) {
                System.out.println("ℹ️ [TourAPI] 해당 반경 내에 조회된 장소가 존재하지 않습니다.");
                return list;
            }

            JsonNode itemNode = itemsNode.path("item");

            // 7. 데이터가 여러 개여서 '배열(Array)' 구조로 들어온 경우 파싱
            if (itemNode.isArray()) {
            		//System.out.println("배열 - 리스트");
                for (JsonNode item : itemNode) {
                    list.add(parseItemToDTO(item));
                }
            } 
            // 8. 데이터가 단 1개만 잡혀서 '객체(Object)' 구조로 들어온 경우 파싱 (★고질병 방어)
            else if (itemNode.isObject()) {
            //	System.out.println("객체 - 리스트");
                list.add(parseItemToDTO(itemNode));
            } else {
                System.err.println("❌ [TourAPI 에러] 알 수 없는 item 노드 포맷입니다.");
            }

        } catch (Exception e) {
            System.err.println("❌ TourAPI 호출 및 파싱 파이프라인 중 예외 발생!!");
            e.printStackTrace();
        }
        
        return list;
    }

    /**
     * JsonNode 데이터를 기반으로 소문자 기반 NearbyPlaceDTO 구조에 매핑하는 헬퍼 메서드
     */
    private NearbyPlaceDTO parseItemToDTO(JsonNode item) {
        NearbyPlaceDTO dto = new NearbyPlaceDTO();
        
        // 데이터가 누락되어 비어있더라도 .path().asText()는 null 대신 빈 문자열("")을 뱉으므로 안전합니다.
        dto.setContentid(item.path("contentid").asText());
        dto.setTitle(item.path("title").asText());
        dto.setContenttypeid(item.path("contenttypeid").asText());
        dto.setFirstimage(item.path("firstimage").asText());
        dto.setAddr1(item.path("addr1").asText());
        
        // 숫자형 데이터가 없거나 유효하지 않을 경우 기본값 0.0 주입
        dto.setMapx(item.path("mapx").asDouble(0.0));
        dto.setMapy(item.path("mapy").asDouble(0.0));
        dto.setDist(item.path("dist").asDouble(0.0));
        
        return dto;
    }
}
