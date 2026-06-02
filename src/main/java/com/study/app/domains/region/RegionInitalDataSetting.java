package com.study.app.domains.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RegionInitalDataSetting implements CommandLineRunner {
	// 서버가 실행할 때

	@Autowired
	private RegionMasterService regionMasterService;

	// 서버가 실행되면서 DB 저장 실행
	@Override
	public void run(String... args) throws Exception {
		// 시/도, 시/군/구 데이터 DB에 1회 저장
		regionMasterService.initRegionAndSigunguData();
	}

}
