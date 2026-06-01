package com.study.app.domains.theme;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ThemeMasterDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public List<ThemeMasterDTO> selectAllTheme(){
		return mybatis.selectList("Theme.selectAllTheme");
	}

}
