package com.study.app.domains.theme;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeMaterService {
	
	@Autowired
	private ThemeMasterDAO themeMasterDAO;
	
	public List<ThemeMasterDTO> getThemeList(){
		return themeMasterDAO.selectAllTheme();
	}

}
