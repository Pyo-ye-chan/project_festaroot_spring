package com.study.app.domains.region;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMasterMapper {
	
	// 값이 있는지 확인
	int selectRegionCount();
	
	int insertRegion(RegionMasterDTO dto);
	
	List<RegionMasterDTO> selectAllSido();
	
	List<RegionMasterDTO> selectAllSigungu(String regionCode);
	

}
