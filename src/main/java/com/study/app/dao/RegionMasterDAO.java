package com.study.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.app.dto.RegionMasterDTO;

@Mapper
public interface RegionMasterDAO {
	
	int insertRegion(RegionMasterDTO dto);
	List<RegionMasterDTO> selectAllSido();
	List<RegionMasterDTO> selectAllSigungu(String regionCode);

}
