package com.study.app.domains.region;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMasterDAO {
	
	int insertRegion(RegionMasterDTO dto);
	List<RegionMasterDTO> selectAllSido();
	List<RegionMasterDTO> selectAllSigungu(String regionCode);

}
