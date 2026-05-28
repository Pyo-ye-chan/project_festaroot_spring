package com.study.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.dto.EventPlaceDTO;
import com.study.app.dto.FestivalDTO;
import com.study.app.dto.FoodPlaceDTO;
import com.study.app.dto.NearbyPlaceDTO;
import com.study.app.dto.PlaceDetailResponse;
import com.study.app.dto.TourPlaceDTO;
import com.study.app.services.FestivalService;

@RestController
@RequestMapping("/api/festivals")
public class FestivalController {

	@Autowired
	private FestivalService feServ;

	@GetMapping
	public ResponseEntity<List<FestivalDTO>> getAllFestival(){
		List<FestivalDTO> list = feServ.getAllFestival();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/nearby")
	public ResponseEntity<List<NearbyPlaceDTO>> getNearbyPlaces(
			@RequestParam Double lat,
			@RequestParam Double lng,
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
	public ResponseEntity<PlaceDetailResponse<FoodPlaceDTO>> getFoodPlaceDetail(@PathVariable String contentId){
		System.out.println("도착");
		PlaceDetailResponse<FoodPlaceDTO> dto = feServ.getPlaceDetail(contentId,"39");
		System.out.println(dto.getCommonInfo().getHomepage());
		System.out.println(dto.getSpecificInfo().getFirstmenu());
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/tour/{contentId}")
	public ResponseEntity<PlaceDetailResponse<TourPlaceDTO>> getTourPlaceDetail(@PathVariable String contentId){

		PlaceDetailResponse<TourPlaceDTO> dto = feServ.getPlaceDetail(contentId,"12");

		return ResponseEntity.ok(dto);
	}
	@GetMapping("/event/{contentId}")
	public ResponseEntity<PlaceDetailResponse<EventPlaceDTO>> getEventPlaceDetail(@PathVariable String contentId){

		PlaceDetailResponse<EventPlaceDTO> dto = feServ.getPlaceDetail(contentId,"15");

		return ResponseEntity.ok(dto);
	}

}

