package com.study.app.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.app.dto.ThemeMasterDTO;

@Repository
public class ThemeMasterDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public List<ThemeMasterDTO> selectAllTheme(){
		return mybatis.selectList("Theme.selectAllTheme");
	}

}
