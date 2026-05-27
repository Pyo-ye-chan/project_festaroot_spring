package com.study.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.dto.FestivalDTO;
import com.study.app.dto.NearbyPlaceDTO;
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
		
		for(NearbyPlaceDTO dto : list) {
			System.out.println("리스트" +dto.getAddr1()+ " : " + dto.getContentid() + " : " + dto.getContenttypeid()
			+ " : " + dto.getFirstimage() + " : " + dto.getTitle());
		}
		
		return ResponseEntity.ok(list);
	}
	
	
}
