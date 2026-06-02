package com.study.app.domains.festival;

import java.util.List;

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
import com.study.app.domains.festival.dto.FestivalDTO;
import com.study.app.domains.festival.dto.FoodPlaceDTO;
import com.study.app.domains.festival.dto.NearbyPlaceDTO;
import com.study.app.domains.festival.dto.PlaceDetailResponse;
import com.study.app.domains.festival.dto.TourPlaceDTO;

@RestController
@RequestMapping("/api/festivals")
public class FestivalController {

	@Autowired
	private FestivalService feServ;

	@GetMapping
	public ResponseEntity<List<FestivalDTO>> getAllFestival() {
		List<FestivalDTO> list = feServ.getAllFestival();
		return ResponseEntity.ok(list);
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
	public ResponseEntity<String> syncFestivalData(){
		try {
			feServ.saveFestivalInfoFromApi(); // 축제 정보 DB 동기화
			return ResponseEntity.ok("축제 데이터 동기화 성공");	 // 성공시 리액트에게 200 ok 전달
		}catch(Exception e) {
			System.err.println("[컨트롤러 에러] 축제 데이터 동기화 중 예외 발생 : " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
								.body("데이터 동기화 실패 : " + e.getMessage()); // 실패시 리액트에게 500 에러 전달
		}

	}
	
	// 축제 검색 > 축제 목록 보기
	public ResponseEntity<List<FestivalDTO>> searchFestivalList(){
//		List<FestivalDTO> list = feServ.searchFestivalKeyword();
//		return ResponseEntity.ok(list);
		return ResponseEntity.ok().build();
	}
	
	// 축제 상세보기
	@GetMapping("/detail/{contentId}")
	public ResponseEntity<FestDetailDTO> getFestivalDetail(@PathVariable String contentId) {
		FestDetailDTO dto = feServ.getFestivalDetail(contentId);
		return ResponseEntity.ok(dto);
	}

}
