package com.study.app.domains.region;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/region")
public class RegionController {

	@Autowired
	private RegionMasterService regionMasterService;

	public RegionController(RegionMasterService regionMasterService) {
		this.regionMasterService = regionMasterService;
	}

	@GetMapping("/sido")
	public List<RegionMasterDTO> getSidoList() {
		return regionMasterService.getSidoList();
	}

	@GetMapping("/sigungu")
	public List<RegionMasterDTO> getSigunguList(@RequestParam String regionCode) {
		return regionMasterService.getSigunguList(regionCode);
	}
}