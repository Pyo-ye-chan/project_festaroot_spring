package com.study.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.app.dao.ThemeMasterDAO;
import com.study.app.dto.ThemeMasterDTO;

@Service
public class ThemeMaterService {
	
	@Autowired
	private ThemeMasterDAO themeMasterDAO;
	
	public List<ThemeMasterDTO> getThemeList(){
		return themeMasterDAO.selectAllTheme();
	}

}
