package com.study.app.domains.festival;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.app.domains.festival.dto.CommonDetailDTO;
import com.study.app.domains.festival.dto.EventPlaceDTO;
import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.festival.dto.FoodPlaceDTO;
import com.study.app.domains.festival.dto.NearbyPlaceDTO;
import com.study.app.domains.festival.dto.PlaceDetailResponse;
import com.study.app.domains.festival.dto.TourPlaceDTO;

@Service
public class FestivalService {

	@Autowired
	private FestivalDAO fdao;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${kto.service.key}")
	private String serviceKey;

	public List<FestivalDTO> getAllFestival() {
		return fdao.getAllFestival();
	}

	public FestivalDTO selectByContentId(String contentId) {
		return fdao.selectByContentId(contentId);
	}

	/**
	 * 한국관광공사 TourAPI (detailCommon2) 호출하여 공통 상세 정보를 가져옵니다.
	 */
	public CommonDetailDTO getCommonDetail(String contentId) {
		try {
			URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B551011/KorService2/detailCommon2")
					.queryParam("serviceKey", serviceKey).queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "AppTest").queryParam("_type", "json").queryParam("contentId", contentId)
					// .queryParam("defaultYN", "Y")
					// .queryParam("firstImageYN", "Y")
					// .queryParam("addrinfoYN", "Y")
					// .queryParam("overviewYN", "Y")
					.build(true).toUri();

			System.out.println(">>> [CommonDetail] Request URI: " + uri);
			String response = restTemplate.getForObject(uri, String.class);
			System.out.println(">>> [CommonDetail] Raw Response: " + response);

			if (response == null || response.trim().startsWith("<")) {
				System.err.println("❌ [TourAPI 에러] 유효하지 않은 응답입니다 (Common).");
				return null;
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);
			JsonNode itemNode = root.path("response").path("body").path("items").path("item");

			if (itemNode.isMissingNode() || (itemNode.isTextual() && itemNode.asText().equals("")))
				return null;

			JsonNode data = itemNode.isArray() ? itemNode.get(0) : itemNode;

			CommonDetailDTO dto = new CommonDetailDTO();
			dto.setOverview(data.path("overview").asText());
			dto.setHomepage(data.path("homepage").asText());
			dto.setTel(data.path("tel").asText());
			dto.setTelname(data.path("telname").asText());
			dto.setZipcode(data.path("zipcode").asText());
			dto.setFirstimage2(data.path("firstimage2").asText());

			return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 한국관광공사 TourAPI (detailIntro2) 호출하여 상세 정보(소개 정보)를 가져옵니다. contentTypeId에 따라
	 * 반환되는 DTO 타입이 달라집니다.
	 */
	@SuppressWarnings("unchecked")
	public <T> PlaceDetailResponse<T> getPlaceDetail(String contentId, String contentTypeId) {
		try {
			// 1. 공통 정보 가져오기
			CommonDetailDTO commonInfo = getCommonDetail(contentId);

			// 2. 특정 상세 정보 (detailIntro2) 가져오기
			URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B551011/KorService2/detailIntro2")
					.queryParam("serviceKey", serviceKey).queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "AppTest").queryParam("_type", "json").queryParam("contentId", contentId)
					.queryParam("contentTypeId", contentTypeId).build(true).toUri();

			System.out.println(">>> [SpecificDetail] Request URI: " + uri);
			String response = restTemplate.getForObject(uri, String.class);
			System.out.println(">>> [SpecificDetail] Raw Response: " + response);

			if (response == null || response.trim().startsWith("<")) {
				System.err.println("❌ [TourAPI 에러] 유효하지 않은 응답입니다 (Intro).");
				return new PlaceDetailResponse<>(commonInfo, null);
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);
			JsonNode itemNode = root.path("response").path("body").path("items").path("item");

			T specificInfo = null;
			if (!itemNode.isMissingNode() && !(itemNode.isTextual() && itemNode.asText().equals(""))) {
				JsonNode data = itemNode.isArray() ? itemNode.get(0) : itemNode;

				if ("39".equals(contentTypeId)) { // 음식점
					FoodPlaceDTO dto = new FoodPlaceDTO();
					dto.setFirstmenu(data.path("firstmenu").asText());
					dto.setTreatmenu(data.path("treatmenu").asText());
					dto.setOpentimefood(data.path("opentimefood").asText());
					dto.setRestdatefood(data.path("restdatefood").asText());
					dto.setInfocenterfood(data.path("infocenterfood").asText());
					specificInfo = (T) dto;
				} else if ("12".equals(contentTypeId)) { // 관광지
					TourPlaceDTO dto = new TourPlaceDTO();
					dto.setUsetime(data.path("usetime").asText());
					dto.setRestdate(data.path("restdate").asText());
					dto.setInfocenter(data.path("infocenter").asText());
					dto.setParking(data.path("parking").asText());
					dto.setChkpet(data.path("chkpet").asText());
					dto.setChkbabycarriage(data.path("chkbabycarriage").asText());
					dto.setExpguide(data.path("expguide").asText());
					dto.setExpagerange(data.path("expagerange").asText());
					specificInfo = (T) dto;
				} else if ("15".equals(contentTypeId)) { // 행사/축제
					EventPlaceDTO dto = new EventPlaceDTO();
					dto.setEventstartdate(data.path("eventstartdate").asText());
					dto.setEventenddate(data.path("eventenddate").asText());
					dto.setEventplace(data.path("eventplace").asText());
					dto.setUsefee(data.path("usefee").asText());
					dto.setProgram(data.path("program").asText());
					dto.setPlaytime(data.path("playtime").asText());
					dto.setSpendtimefestival(data.path("spendtimefestival").asText());
					dto.setAgelimit(data.path("agelimit").asText());
					dto.setBookingplace(data.path("bookingplace").asText());
					dto.setDiscountinfofestival(data.path("discountinfofestival").asText());
					dto.setSponsor1(data.path("sponsor1").asText());
					dto.setSponsor1tel(data.path("sponsor1tel").asText());
					specificInfo = (T) dto;
				}
			}

			return new PlaceDetailResponse<>(commonInfo, specificInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<NearbyPlaceDTO> getNearbyPlaces(Double lat, Double lng, Integer radius, String contentTypeId) {
		List<NearbyPlaceDTO> list = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();

		// 1. 프론트엔드가 넘겨준 파라미터 방어 코드
		if (radius == null)
			radius = 5000; // 기본값 5km
		if (contentTypeId == null || contentTypeId.isEmpty())
			contentTypeId = "12"; // 기본값 관광지(12)

		// 💡 2. contenttypeid 소수점 및 공백 완벽 제거 (★핵심 패치)
		String cleanContentTypeId = "";
		if (contentTypeId != null && !contentTypeId.toString().trim().isEmpty()
				&& !contentTypeId.toString().equals("null")) {
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
					.queryParam("serviceKey", serviceKey).queryParam("numOfRows", 30).queryParam("pageNo", 1)
					.queryParam("MobileOS", "ETC").queryParam("MobileApp", "AppTest").queryParam("_type", "json")
					// .queryParam("listYN", "Y")
					.queryParam("arrange", "E").queryParam("mapX", lng) // 경도
					.queryParam("mapY", lat) // 위도
					.queryParam("radius", radius)
					// 💡 contentTypeId (X) -> 전부 소문자인 contenttypeid (O)로 변경해야 합니다.
					.queryParam("contentTypeId", cleanContentTypeId).build(true).toUri();
			// 3. API 서버 호출
			String response = restTemplate.getForObject(uri, String.class);
			// System.out.println(response);

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
			if (bodyNode.isMissingNode() || itemsNode.isMissingNode()
					|| (itemsNode.isTextual() && itemsNode.asText().equals(""))) {
				System.out.println("ℹ️ [TourAPI] 해당 반경 내에 조회된 장소가 존재하지 않습니다.");
				return list;
			}

			JsonNode itemNode = itemsNode.path("item");

			// 7. 데이터가 여러 개여서 '배열(Array)' 구조로 들어온 경우 파싱
			if (itemNode.isArray()) {
				// System.out.println("배열 - 리스트");
				for (JsonNode item : itemNode) {
					list.add(parseItemToDTO(item));
				}
			}
			// 8. 데이터가 단 1개만 잡혀서 '객체(Object)' 구조로 들어온 경우 파싱 (★고질병 방어)
			else if (itemNode.isObject()) {
				// System.out.println("객체 - 리스트");
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

	// festival 정보를 DB에 저장하는 메서드
	public void saveFestivalInfoFromApi() {
		try {
			// 동적으로 현재 연도 구하기 (예: 2026년이면 2026이 담김)
	        int currentYear = LocalDate.now().getYear();
	        String startDate = currentYear + "0101"; // "20260101" 형태로 문자열 조립
			
			int numOfRows = 100; // 한 페이지에 가져올 양 변수 지정
			int totalPages = 1; // 최소 1페이지로 변수 지정

			for (int i = 1; i <= totalPages; i++) { // totalPages 수와 동일해질 때 까지 반복문
				// 관광공사 축제 목록 API 주소(searchFestival2) 조립
				URI uri = UriComponentsBuilder
						.fromUriString("https://apis.data.go.kr/B551011/KorService2/searchFestival2")
						.queryParam("serviceKey", serviceKey).queryParam("MobileOS", "ETC")
						.queryParam("MobileApp", "AppTest").queryParam("_type", "json")
						.queryParam("numOfRows", numOfRows) // 한 번에 조회되는 값
						.queryParam("pageNo", i) // 100개씩 페이지 나눌것임. 1페이지부터 시작. 두번째 반복문 때 i 반영됨.
						.queryParam("eventStartDate", startDate) // 데이터 값을 가져올 기준 날짜
						.build(true).toUri();

				String response = restTemplate.getForObject(uri, String.class); // API 호출

				// API 내용 확인 > JSON
				//System.out.println(">>> [공공 API 응답] : " + response); // 길어서 주석처리함.

				// 잭슨 라이브러리로 item 까지 들어가기
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response); // 파싱
				JsonNode itemNode = root.path("response").path("body").path("items").path("item");

				// itemNode의 타입 확인하기 > ARRAY
				//System.out.println(">>>> [itemNode 타입]: " + itemNode.getNodeType());

				
				if(i == 1) { // 처음 이 for문을 돌 때, 전체 데이터 수와 페이지 수 계산
					// root에서 totalCount int로 총 데이터 수 뽑기
					int totalCount = root.path("response").path("body").path("totalCount").asInt();

					// 총 데이터 수와 한 페이지 수로 '총 페이지 수' 계산하기
					totalPages = (int) Math.ceil((double) totalCount / numOfRows);
					System.out.println("총 데이터 수 : " + totalCount + ", 총 페이지 수 : " + totalPages);
				}
			
				if (itemNode.isArray()) { // boolean으로 배열인지 확인
					for (JsonNode item : itemNode) {
						// FestivalDTO에 item에서 꺼낸 값 담기
						FestivalDTO dto = new FestivalDTO();

						dto.setContent_id(item.path("contentid").asLong()); // api에서 contentid 꺼내오고 dto에 설정한 Long형으로 받기
						dto.setTitle(item.path("title").asText()); // 축제명
						dto.setAddr1(item.path("addr1").asText());
						dto.setAddr2(item.path("addr2").asText());

						// NOT NULL 값 0으로 처리
						String areaCode = item.path("areacode").asText(); // 지역 코드
						dto.setRegion_code(areaCode.isEmpty() ? "0" : areaCode); // 만약 가리키는 값이 비어있다면 "0", 있으면 그대로 쓰기

						String sigunguCode = item.path("sigungucode").asText(); // 시군구
						dto.setSigungu_code(sigunguCode.isEmpty() ? "0" : sigunguCode);

						dto.setFirst_image(item.path("firstimage").asText()); // 대표 이미지
						dto.setFirst_image2(item.path("firstimage2").asText()); // 썸네일 이미지
						dto.setMap_x(item.path("mapx").asDouble()); // 경도
						dto.setMap_y(item.path("mapy").asDouble()); // 위도
						dto.setMap_level(item.path("mlevel").asInt());
						dto.setEvent_start_date(item.path("eventstartdate").asText()); // 행사 시작일
						dto.setEvent_end_date(item.path("eventenddate").asText()); // 행사 종료일

						// overview, sponplace, usetimefestival, sponsor1tel, hompage 는 상세를 눌렀을때 담기도록
						// 메서드 만들어야함.

						dto.setCreated_time(item.path("createdtime").asText()); // 축제 등록일
						dto.setModified_time(item.path("modifiedtime").asText()); // 축제 수정일

						// festivalDAO 호출 : 값을 가져온 범위 내에서 이미 값이 있으면 update, 없으면 insert
						fdao.upsertFestival(dto); // api 값 담은 dto 전달
					}
				}
				// 현재 저장된 진행 상황
				System.out.println("[동기화 된 페이지] " + i + " / " + totalPages + " 페이지 저장 완료 (" + itemNode.size() + "개 항목)");
			} // for 문 끝
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("관광공사 데이터 동기화 실패", e);
		}
	}

}
