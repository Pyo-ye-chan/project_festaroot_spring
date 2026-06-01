package com.study.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.dto.ThemeMasterDTO;
import com.study.app.services.ThemeMaterService;

@RestController
@RequestMapping("/theme")
public class ThemeController {
	
	@Autowired
	private ThemeMaterService themeMasterService;
	
    @GetMapping("/theme")
    public List<ThemeMasterDTO> getThemeList() {
        return themeMasterService.getThemeList();
    }

}
