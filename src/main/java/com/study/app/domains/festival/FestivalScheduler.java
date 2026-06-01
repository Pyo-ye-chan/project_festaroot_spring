package com.study.app.domains.festival;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component // Spring이 이 클래스를 관리하도록 Bean으로 등록
public class FestivalScheduler {

	@Autowired
	private FestivalService festivalService; // 기존 서비스를 주입(DB 업데이트 코드가 있는 클래스)

	// @Scheduled : 일정 주기마다 축제 데이터를 동기화하는 메서드
	// ** 사용할 방식에 따라 어노테이션에 달리 주석 제거하면 됨.

	// 방법 A: 매일 새벽 4시에 실행하고 싶을 때 (크론식 사용)
	// @Scheduled(cron = "0 0 4 * * *") // 초 분 시 일 월 요일 순서
	
	// 방법 B: 주기적으로 실행하고 싶을 때 (예: 1시간마다 = 3600000ms) 
	// @Scheduled(fixedDelay = 3600000)
	public void autoUpdateFestivalData() {
		try {
			System.out.println("[스케줄러 작동] 관광공사 데이터 동기화 시작");

			// 기존 FestivalService에 만들어 둔 메서드를 그대로 호출
			festivalService.saveFestivalInfoFromApi();

			System.out.println("[스케줄러 작동] 동기화 정상 완료");
		} catch (Exception e) {
			System.err.println("[스케줄러 에러] 동기화 중 오류 발생");
			e.printStackTrace();
		}
	}

}
