package com.study.app.domains.festival;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.domains.festival.dto.EventPlaceDTO;
import com.study.app.domains.festival.dto.FestDetailDTO;
import com.study.app.domains.festival.dto.FestImageDTO;
import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.festival.dto.FestivalSearchDTO;
import com.study.app.domains.festival.dto.FoodPlaceDTO;
import com.study.app.domains.festival.dto.NearbyPlaceDTO;
import com.study.app.domains.festival.dto.PlaceDetailResponse;
import com.study.app.domains.festival.dto.TourPlaceDTO;
import com.study.app.domains.region.RegionMasterDTO;
import com.study.app.domains.region.RegionMasterService;

@RestController
@RequestMapping("/api/festivals")
public class FestivalController {

	@Autowired
	private FestivalService feServ;
	
	@Autowired
	private RegionMasterService regionMasterService;
	
	@GetMapping("/map")
	public ResponseEntity<List<FestivalDTO>> getAllFestival() {
		List<FestivalDTO> list = feServ.getAllFestival();
		return ResponseEntity.ok(list);
	}

	// 축제 찾기 > 네비게이터 반영한 축제 목록 (getAllFestivals 대체)	
	@GetMapping
	public ResponseEntity<?> getFestivals(FestivalSearchDTO searchDTO) {
	    // 프론트에서 넘어온 페이징 파라미터 확인
	    int currentPage = searchDTO.getPage() > 0 ? searchDTO.getPage() : 1;
	    int size = searchDTO.getSize() > 0 ? searchDTO.getSize() : 9;
	    
	    // DB에서 검색 조건에 맞는 '총 게시글 수' 조회
	    int totalCount = feServ.getSearchFestivalCount(searchDTO);
	    
	    // 실제 DB에서 해당 페이지 분량만큼의 리스트 조회
	    List<FestivalDTO> list = feServ.getSearchFestivals(searchDTO);
	    
	    // 페이징 네비게이터 계산 로직
	    int pageBlock = 5; // 하단에 보여줄 페이지 번호 개수 (예: 1 2 3 4 5)
	    int totalPage = (int) Math.ceil((double) totalCount / size); // 총 페이지 수
	    
	    // 현재 페이지 기준 종료 페이지 계산
	    int endPage = (int) (Math.ceil(currentPage / (double) pageBlock)) * pageBlock;
	    int startPage = endPage - pageBlock + 1;
	    
	    // 실제 총 페이지 수가 계산된 endPage보다 작다면 조절
	    if (endPage > totalPage) {
	        endPage = totalPage;
	    }
	    if (startPage < 1) {
	        startPage = 1;
	    }
	    
	    // 이전 / 다음 블록 존재 여부
	    boolean existPrev = startPage > 1;
	    boolean existNext = endPage < totalPage;
	    
	    // 리액트가 정확히 수신할 수 있도록 객체(Map) 구조화
	    Map<String, Object> responseMap = new HashMap<>();
	    responseMap.put("list", list); // 축제 데이터 배열
	    
	    Map<String, Object> pageInfoMap = new HashMap<>();
	    pageInfoMap.put("startPage", startPage);
	    pageInfoMap.put("endPage", endPage);
	    pageInfoMap.put("existPrev", existPrev);
	    pageInfoMap.put("existNext", existNext);
	    pageInfoMap.put("totalCount", totalCount); // 총 결과 개수 반영
	    
	    responseMap.put("pageInfo", pageInfoMap);
	    
	    return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/nearby")
	public ResponseEntity<List<NearbyPlaceDTO>> getNearbyPlaces(@RequestParam Double lat, @RequestParam Double lng,
			@RequestParam(defaultValue = "5000") Integer radius,
			@RequestParam(required = false, defaultValue = "") String contentTypeId) {
//		System.out.println("프론트가 준 값 -> lat: " + lat + ", lng: " + lng + ", radius: " + radius + 
//				", contenttypeid :" + contentTypeId);
		List<NearbyPlaceDTO> list = feServ.getNearbyPlaces(lat, lng, radius, contentTypeId);

//		for(NearbyPlaceDTO dto : list) {
//			System.out.println("리스트" +dto.getAddr1()+ " : " + dto.getContentid() + " : " + dto.getContenttypeid()
//			+ " : " + dto.getFirstimage() + " : " + dto.getTitle());
//		}

		return ResponseEntity.ok(list);
	}

	@GetMapping("/food/{contentId}")
	public ResponseEntity<PlaceDetailResponse<FoodPlaceDTO>> getFoodPlaceDetail(@PathVariable String contentId) {
		System.out.println("도착");
		PlaceDetailResponse<FoodPlaceDTO> dto = feServ.getPlaceDetail(contentId, "39");
		System.out.println(dto.getCommonInfo().getHomepage());
		System.out.println(dto.getSpecificInfo().getFirstmenu());
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/tour/{contentId}")
	public ResponseEntity<PlaceDetailResponse<TourPlaceDTO>> getTourPlaceDetail(@PathVariable String contentId) {

		PlaceDetailResponse<TourPlaceDTO> dto = feServ.getPlaceDetail(contentId, "12");

		return ResponseEntity.ok(dto);
	}

	@GetMapping("/event/{contentId}")
	public ResponseEntity<PlaceDetailResponse<EventPlaceDTO>> getEventPlaceDetail(@PathVariable String contentId) {

		PlaceDetailResponse<EventPlaceDTO> dto = feServ.getPlaceDetail(contentId, "15");

		return ResponseEntity.ok(dto);
	}

	// 축제 정보 DB에 저장
	@PostMapping("/sync")
	public ResponseEntity<String> syncFestivalData() {
		try {
			feServ.saveFestivalInfoFromApi(); // 축제 정보 DB 동기화
			return ResponseEntity.ok("축제 데이터 동기화 성공"); // 성공시 리액트에게 200 ok 전달
		} catch (Exception e) {
			System.err.println("[컨트롤러 에러] 축제 데이터 동기화 중 예외 발생 : " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 동기화 실패 : " + e.getMessage()); // 실패시																												// 전달
		}
	}
	
	// 축제 찾기 > 시도, 시군구 값 불러오기
	@GetMapping("/sido")
	public List<RegionMasterDTO> getSidoList() {
		return regionMasterService.getSidoList();
	}

	@GetMapping("/sigungu")
	public List<RegionMasterDTO> getSigunguList(@RequestParam String region_code) {
		return regionMasterService.getSigunguList(region_code);
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
	
	// 축제 상세보기
	@GetMapping("/detail/{contentId}")
	public ResponseEntity<FestDetailDTO> getFestivalDetail(@PathVariable String contentId) {
		FestDetailDTO dto = feServ.getFestivalDetail(contentId);
		if(dto != null && dto.getHomepage() != null) {
			dto.setHomepage(extractHomepageUrl(dto.getHomepage()));
		}
		return ResponseEntity.ok(dto);
	}
	
	// 축제 이미지 가져오기
	@GetMapping("/images/{contentId}")
	public ResponseEntity<List<FestImageDTO>> getFestivalImages(@PathVariable String contentId) {
		List<FestImageDTO> images = feServ.getFestivalImages(contentId);
		System.out.println("이미지" + images);
		return ResponseEntity.ok(images);
	}

}
