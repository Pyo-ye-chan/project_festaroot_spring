package com.study.app.domains.festival;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.app.domains.festival.dto.CommonDetailDTO;
import com.study.app.domains.festival.dto.EventPlaceDTO;
import com.study.app.domains.festival.dto.FestDetailDTO;
import com.study.app.domains.festival.dto.FestImageDTO;
import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.festival.dto.FestivalSearchDTO;
import com.study.app.domains.festival.dto.FoodPlaceDTO;
import com.study.app.domains.festival.dto.NearbyPlaceDTO;
import com.study.app.domains.festival.dto.PlaceDetailResponse;
import com.study.app.domains.festival.dto.TourPlaceDTO;

@Service
public class FestivalService {

	@Autowired
	private FestivalDAO fdao;

	@Autowired
	private com.study.app.domains.achievement.AchievementService achievementService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${kto.service.key}")
	private String serviceKey;
	
	public List<FestivalDTO> getAllFestival() {
		return fdao.getAllFestival();
	}

	// 축제 찾기 > 검색 조건에 맞는 축제 목록 가져오기
	public List<FestivalDTO> getSearchFestivals(FestivalSearchDTO searchDTO) {
		return fdao.getSearchFestivals(searchDTO);
	}

	// 축제 찾기 > 네비게이터 카운트
	public int getSearchFestivalCount(FestivalSearchDTO searchDTO) {
		return fdao.getSearchFestivalCount(searchDTO);
	}

	// 축제 찾기 > 목록 클릭 > 축제별 조회수
	public void increaseViewCount(String contentId) {
		fdao.increaseViewCount(contentId);
	}
	
	// 홈 > 지역별 인기 축제 목록 top3
	public List<FestivalDTO> getTop3ByRegion(String regionName){
		return fdao.getTop3ByRegion(regionName);
	}
	
	// 홈 > 종료 임박 축제 목록
	public List<FestivalDTO> getClosingSoonFestivals() {
        return fdao.getClosingSoonFestivals();
    }
	
	// 홈 > 랜덤 축제 추천
	public Map<String, Object> getRandomFestival(String memberId) {
		Map<String, Object> result = new HashMap<>();
		List<com.study.app.domains.achievement.dto.AchievementResultDTO> achievementResults = new ArrayList<>();
		
		FestivalDTO festival = fdao.getRandomFestival();
		result.put("festival", festival);
		
		if (festival != null && memberId != null) {
			// 로그인한 유저의 경우 랜덤 뽑기 업적 업데이트
			achievementResults = achievementService.updateProgress(memberId, "RANDOM_PICK");
		}
		
		result.put("achievements", achievementResults);
		return result;
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
				} else if ("14".equals(contentTypeId)) { // 문화시설
					com.study.app.domains.festival.dto.CultureFacilityDTO dto = new com.study.app.domains.festival.dto.CultureFacilityDTO();
					dto.setInfocenterculture(data.path("infocenterculture").asText());
					dto.setUsetimeculture(data.path("usetimeculture").asText());
					dto.setRestdateculture(data.path("restdateculture").asText());
					dto.setUsefee(data.path("usefee").asText());
					dto.setParkingculture(data.path("parkingculture").asText());
					dto.setChkpetculture(data.path("chkpetculture").asText());
					dto.setChkbabycarriageculture(data.path("chkbabycarriageculture").asText());
					dto.setSpendtime(data.path("spendtime").asText());
					specificInfo = (T) dto;
				}
				/* 
				else if ("15".equals(contentTypeId)) { // 행사/축제
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
				*/
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
				// System.out.println(">>> [공공 API 응답] : " + response); // 길어서 주석처리함.

				// 잭슨 라이브러리로 item 까지 들어가기
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response); // 파싱
				JsonNode itemNode = root.path("response").path("body").path("items").path("item");

				// itemNode의 타입 확인하기 > ARRAY
				// System.out.println(">>>> [itemNode 타입]: " + itemNode.getNodeType());

				if (i == 1) { // 처음 이 for문을 돌 때, 전체 데이터 수와 페이지 수 계산
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

						long contentId = item.path("contentid").asLong();

						dto.setContent_id(item.path("contentid").asLong()); // api에서 contentid 꺼내오고 dto에 설정한 Long형으로 받기
						dto.setTitle(item.path("title").asText()); // 축제명
						dto.setAddr1(item.path("addr1").asText());
						dto.setAddr2(item.path("addr2").asText());

						// NOT NULL 값 0으로 처리
						String lDongRegnCd = item.path("lDongRegnCd").asText(); // 지역 코드
						dto.setRegion_code(lDongRegnCd.isEmpty() ? "0" : lDongRegnCd); // 만약 가리키는 값이 비어있다면 "0", 있으면 그대로
																						// 쓰기

						String lDongSignguCd = item.path("lDongSignguCd").asText(); // 시군구
						dto.setSigungu_code(lDongSignguCd.isEmpty() ? "0" : lDongSignguCd);

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

						// DB에 저장된 기존 축제 조회
						FestivalDTO dbFestival = fdao.selectByContentId(String.valueOf(contentId));

						// TourAPI 목록에서 받은 최신 수정일
						String apiModifiedTime = item.path("modifiedtime").asText();

						// DB에 저장된 수정일
						String dbModifiedTime = dbFestival == null ? null : dbFestival.getModified_time();

						// DB의 수정일과 API의 수정일이 다르면,
						// 관광공사 쪽 데이터가 수정된 것으로 판단
						boolean isUpdated = dbFestival == null || isBlank(dbModifiedTime)
								|| !apiModifiedTime.equals(dbModifiedTime);

						// detailCommon2 호출 여부 판단
						boolean needCommon = dbFestival == null || isUpdated || isBlank(dbFestival.getOverview())
								|| isBlank(dbFestival.getHomepage()) || isBlank(dbFestival.getSponsor1_tel());

						// detailIntro2 호출 여부 판단
						boolean needIntro = dbFestival == null || isUpdated || isBlank(dbFestival.getSpon_place())
								|| isBlank(dbFestival.getUse_time_festival());

						// 공통 상세정보가 없거나,
						// 관광공사 데이터가 수정된 경우 다시 조회
						if (needCommon) {
							fillDetailCommon(dto, contentId, mapper);
						} else {
							dto.setOverview(dbFestival.getOverview());
							dto.setHomepage(dbFestival.getHomepage());
							dto.setSponsor1_tel(dbFestival.getSponsor1_tel());
						}

						// 축제 소개 상세정보가 없거나,
						// 관광공사 데이터가 수정된 경우 다시 조회
						if (needIntro) {
							fillDetailIntro(dto, contentId, mapper);
						} else {
							dto.setSpon_place(dbFestival.getSpon_place());
							dto.setUse_time_festival(dbFestival.getUse_time_festival());
						}

						// festivalDAO 호출 : 값을 가져온 범위 내에서 이미 값이 있으면 update, 없으면 insert
						// fdao.upsertFestival(dto); // api 값 담은 dto 전달

						try {
							System.out.println("저장 시도 contentId = " + dto.getContent_id());
							System.out.println("title = " + dto.getTitle());
							System.out.println("homepage length = "
									+ (dto.getHomepage() == null ? 0 : dto.getHomepage().length()));
							System.out.println("overview length = "
									+ (dto.getOverview() == null ? 0 : dto.getOverview().length()));

							fdao.upsertFestival(dto);
							fdao.updateFestivalDetail(dto);

						} catch (Exception e) {
							System.out.println("저장 실패 contentId = " + dto.getContent_id());
							System.out.println("title = " + dto.getTitle());
							System.out.println("homepage = " + dto.getHomepage());
							throw e;
						}

					}
				}
				// 현재 저장된 진행 상황
				System.out
						.println("[동기화 된 페이지] " + i + " / " + totalPages + " 페이지 저장 완료 (" + itemNode.size() + "개 항목)");
			} // for 문 끝
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("관광공사 데이터 동기화 실패", e);
		}
	}

	/**
	 * 문자열이 null 이거나 비어있는지 확인
	 *
	 * true : null, "", " " false : 실제 값 존재
	 */
	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	// ============================================================================
	// detailCommon2 조회
	//
	// 목적
	// - 축제 소개글(overview)
	// - 문의 전화번호(tel)
	// - 홈페이지(homepage)
	//
	// 목록 API(searchFestival2)에서는 가져올 수 없는 상세 정보를 조회한다.
	// ============================================================================
	private void fillDetailCommon(FestivalDTO dto, long contentId, ObjectMapper mapper) {

		try {

			// TourAPI 상세공통정보 조회 URL 생성
			URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B551011/KorService2/detailCommon2")

					// 공공데이터포털 인증키
					.queryParam("serviceKey", serviceKey)

					// 필수 파라미터
					.queryParam("MobileOS", "ETC").queryParam("MobileApp", "AppTest")

					// JSON 응답 요청
					.queryParam("_type", "json")

					// 조회할 축제 contentId
					.queryParam("contentId", contentId)

					// 15 = 축제/공연/행사
					// .queryParam("contentTypeId", "15")

					// 소개글 조회
					// .queryParam("overviewYN", "Y")

					// 기본정보 조회
					// title, tel, homepage 등 포함
					// .queryParam("defaultYN", "Y")

					// .queryParam("firstImageYN", "Y")

					.build(true).toUri();

			// TourAPI 호출
			String response = restTemplate.getForObject(uri, String.class);

			System.out.println(response);

			// JSON 문자열 → JsonNode 변환
			JsonNode root = mapper.readTree(response);

			// 응답 구조 접근
			JsonNode item = root.path("response").path("body").path("items").path("item");

	        // item이 배열 또는 객체로 반환될 수 있음
	        JsonNode data = item.isArray() ? item.get(0) : item;

			System.out.println("overview = " + data.path("overview").asText());
			System.out.println("tel = " + data.path("tel").asText());
			System.out.println("homepage = " + data.path("homepage").asText());

			// 정상 데이터 존재 여부 확인
			if (data != null && !data.isMissingNode()) {

				// 소개글
				String overview = data.path("overview").asText();

				// 문의전화
				String tel = data.path("tel").asText();

				// 홈페이지
				String homepage = data.path("homepage").asText();

				// 값이 존재할 경우 DTO에 저장
				if (!overview.isBlank()) {
					dto.setOverview(overview);
				}

				if (!tel.isBlank()) {
					dto.setSponsor1_tel(tel);
				}

				if (!homepage.isBlank()) {
					dto.setHomepage(homepage);
				}
			}

		} catch (Exception e) {

			// 상세 조회 실패 시에도
			// 목록 데이터 저장은 계속 진행되도록 예외 무시
			System.err.println("[detailCommon2 실패] contentId=" + contentId + " : " + e.getMessage());
		}
	}

	// ============================================================================
	// detailIntro2 조회
	//
	// 목적
	// - 행사 장소(eventplace)
	// - 이용요금/관람정보(usetimefestival)
	//
	// 축제 전용 상세 정보를 조회한다.
	// ============================================================================
	private void fillDetailIntro(FestivalDTO dto, long contentId, ObjectMapper mapper) {

		try {

			// TourAPI 상세소개정보 조회 URL 생성
			URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B551011/KorService2/detailIntro2")
					.queryParam("serviceKey", serviceKey).queryParam("MobileOS", "ETC")
					.queryParam("MobileApp", "AppTest").queryParam("_type", "json")

					// 조회 대상 축제 ID
					.queryParam("contentId", contentId)

					// 15 = 축제
					// .queryParam("contentTypeId", "15")

					.build(true).toUri();

			// API 호출
			String response = restTemplate.getForObject(uri, String.class);

			// JSON 파싱
			JsonNode root = mapper.readTree(response);

			JsonNode item = root.path("response").path("body").path("items").path("item");

			JsonNode data = item.isArray() ? item.get(0) : item;

			if (data != null && !data.isMissingNode()) {

				// 행사 장소
				String sponPlace = data.path("eventplace").asText();

				// 이용요금 및 관람안내
				String useTimeFestival = data.path("usetimefestival").asText();

				// DTO 저장
				if (!sponPlace.isBlank()) {
					dto.setSpon_place(sponPlace);
				}

				if (!useTimeFestival.isBlank()) {
					dto.setUse_time_festival(useTimeFestival);
				}
			}

		} catch (Exception e) {

			// 상세 정보 조회 실패 시
			// 전체 동기화 작업은 계속 진행
			System.err.println("[detailIntro2 실패] contentId=" + contentId + " : " + e.getMessage());
		}
	}
	
	// HomepageURL 주소값만 추출
	private String extractHomepageUrl(String homepage) {

	    // href="..." 형태
	    Pattern hrefPattern = Pattern.compile("href\\s*=\\s*\"([^\"]+)\"");
	    Matcher hrefMatcher = hrefPattern.matcher(homepage);

	    if (hrefMatcher.find()) {
	        return hrefMatcher.group(1);
	    }

	    // 일반 URL
	    Pattern urlPattern =
	        Pattern.compile("(https?://[^\\s\"<>]+|www\\.[^\\s\"<>]+)");

	    Matcher urlMatcher = urlPattern.matcher(homepage);

	    if (urlMatcher.find()) {

	        String url = urlMatcher.group();

	        if (url.startsWith("www.")) {
	            url = "https://" + url;
	        }

	        return url;
	    }

	    return null;
	}
	
	// 축제 상세보기 정보 가져오기
	public FestivalDTO getFestivalDetail(String contentId) {
		FestivalDTO dto = fdao.selectDeatilByContentId(contentId);
		if(dto != null && dto.getHomepage() != null) {
			dto.setHomepage(extractHomepageUrl(dto.getHomepage()));
		}
		return dto;
	}
	
	// 축제 이미지 가져오기
	public List<FestImageDTO> getFestivalImages(String contentId) {
	    String url = "https://apis.data.go.kr/B551011/KorService2/detailImage2"
	        + "?serviceKey=" + serviceKey
	        + "&MobileOS=ETC"
	        + "&MobileApp=FestaRoute"
	        + "&_type=json"
	        + "&contentId=" + contentId
	        + "&imageYN=Y"
	        + "&numOfRows=10"
	        + "&pageNo=1";

	    RestTemplate restTemplate = new RestTemplate();

	    Map response = restTemplate.getForObject(url, Map.class);

	    if (response == null || response.get("response") == null) {
	        return new ArrayList<>();
	    }

	    Map responseMap = (Map) response.get("response");
	    Map body = (Map) responseMap.get("body");

	    if (body == null || body.get("items") == null) {
	        return new ArrayList<>();
	    }

	    Object itemsObj = body.get("items");

	    // 핵심: items가 "" 문자열이면 이미지 없음
	    if (!(itemsObj instanceof Map)) {
	        return new ArrayList<>();
	    }

	    Map items = (Map) itemsObj;

	    if (items.get("item") == null) {
	        return new ArrayList<>();
	    }

	    Object itemObj = items.get("item");

	    ObjectMapper objectMapper = new ObjectMapper();

	    if (itemObj instanceof List) {
	        return objectMapper.convertValue(
	            itemObj,
	            new TypeReference<List<FestImageDTO>>() {}
	        );
	    }

	    FestImageDTO image = objectMapper.convertValue(itemObj, FestImageDTO.class);
	    return List.of(image);
	}

	// 로그인 기준 축제 찜 목록 조회
	public List<Long> getMyFestivalLikedIds(String memberId) {
		return fdao.getMyFestivalLikedIds(memberId);
	}

	// 로그인 기준 축제 찜 분기 처리
	@Transactional
	public Map<String, Object> toggleFestivalLike(String memberId, Long contentId) {
		Map<String, Object> result = new HashMap<>();
		List<com.study.app.domains.achievement.dto.AchievementResultDTO> achievementResults = new ArrayList<>();
		
		Map<String, Object> toggle = new HashMap<>();
		toggle.put("member_id", memberId);
		toggle.put("content_id", contentId);

		// 이미 찜했는지 개수 확인 (0 또는 1)
		int count = fdao.checkLikeExists(toggle);

		boolean isLiked;
		if (count == 0) {
			// 찜 안 되어 있으면 찜 추가 + 총 찜 개수 증가
			fdao.insertLike(toggle);
			fdao.incrementLikeCount(contentId);
			isLiked = true;
			
			// 업적 체크: 찜 추가 시에만 수행
			achievementResults = achievementService.updateProgress(memberId, "FESTIVAL_LIKE");
		} else {
			// 이미 찜 되어 있음 ➡️ 찜 삭제 + 총 찜 개수 감소
			fdao.deleteLike(toggle);
			fdao.decrementLikeCount(contentId);
			isLiked = false;
		}
		
		result.put("isLiked", isLiked);
		result.put("achievements", achievementResults);
		
		return result;
	}
	
	// 특정 축제의 실시간 총 찜 개수 조회 (실무형 방어 코드 포함)
	@Transactional(readOnly = true)
	public int getFestivalLikeCount(Long contentId) {
		if (contentId == null) {
			return 0;
		}
		return fdao.getFestivalLikeCount(contentId);
	}
}