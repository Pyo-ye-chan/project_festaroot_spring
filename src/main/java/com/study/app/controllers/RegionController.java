package com.study.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.dto.RegionMasterDTO;
import com.study.app.services.RegionMasterService;

@RestController
@RequestMapping("/region")
public class RegionController {  // http://localhost/region/sync 접속 시 DB 저장 실행

	@Autowired
    private RegionMasterService regionMasterService;

    public RegionController(RegionMasterService regionMasterService) {
        this.regionMasterService = regionMasterService;
    }

    @GetMapping("/sync")
    public String sync() {
        int count = regionMasterService.syncRegionCodes();
        return "지역 코드 저장 완료: " + count + "건";
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

