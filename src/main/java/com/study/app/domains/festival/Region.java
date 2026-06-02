package com.study.app.domains.festival;

public enum Region {
// addr1의 주소 텍스트와 관광공사 코드를 연결해주는 고정 세트

	// 고정된 상수들을 선언하면서, 뒤에 원하는 데이터("이름", 숫자코드)를 달아줌.
	SEOUL("서울", 1),
    INCHEON("인천", 2),
    DAEJEON("대전", 3),
    DAEGU("대구", 4),
    GWANGJU("광주", 5),
    BUSAN("부산", 6),
    ULSAN("울산", 7),
    SEJONG("세종", 8),
    GYEONGGI("경기", 31),
    GANGWON("강원", 32),
    CHUNGBUK("충북", 33),
    CHUNGNAM("충남", 34),
    GYEONGBUK("경북", 35),
    GYEONGNAM("경남", 36),
    JEONBUK("전북", 37),
    JEONNAM("전남", 38),
    JEJU("제주", 39);
	
	// 어디서든 Region.JEJU.getCode() 를 호출하면 39라는 숫자가 나옴.
	// 같은 원래로 getName을 하면 제주가 나옴.
	// Enum은 무분별한 숫자나 문자열(하드코딩) 대신 딱 정해진 선택지들만 정의해두고
	// 관련 데이터와 기능까지 묶어서 관리하는 파일

	// 데이터들을 담을 상수(값 변경 불가) <- final 특성
	private final String name;
	private final int code;

	// 생성자 (데이터를 변수에 매칭 시켜줌)
	private Region(String name, int code) {
		this.name = name;
		this.code = code;
	}

	// 꺼내 쓸 수 있도록 getter 설정
	public String getName() {
		return name;
	}
	public int getCode() {
		return code;
	}
	
	
	// addr1 주소 텍스트를 분석해서 관광공사 지역 코드를 찾아주는 메서드
	public static int findCodeByAddress(String addr1) {
		if(addr1 == null || addr1.isEmpty()) return 0;
		
		// 예 : "충청북도"로 데이터가 들어와도 "충북" 리스트에 걸리도록 글자수 보정처리를 하거나 포함 여부로 체크
		for(Region region : values()) {
			// values()를 호출하면 내부적으로 아래와 같은 배열이 만들어짐.
			//[ Region.SEOUL, Region.INCHEON, Region.DAEJEON, ..., Region.JEJU ]
			
			if(addr1.contains(region.getName())) {
				return region.getCode();
			}
		}
		
		// 예외 처리: 전라북도 -> 전북 매칭용 방어 코드
        if (addr1.contains("전라북")) return JEONBUK.getCode();
        if (addr1.contains("전라남")) return JEONNAM.getCode();
        if (addr1.contains("경상북")) return GYEONGBUK.getCode();
        if (addr1.contains("경상남")) return GYEONGNAM.getCode();
        if (addr1.contains("충청북")) return CHUNGBUK.getCode();
        if (addr1.contains("충청남")) return CHUNGNAM.getCode();

        return 0; // 아무것도 매칭 안 되면 기본값 0

	}
	
	
}
