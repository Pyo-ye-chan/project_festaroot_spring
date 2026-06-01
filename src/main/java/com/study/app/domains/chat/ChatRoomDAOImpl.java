package com.study.app.domains.chat;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository // 실제 오라클DB랑 소통하는 클래스
public class ChatRoomDAOImpl implements ChatRoomDAO{ // ChatRoomDAO 인터페이스 구현하기
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	// @Override
	
	
	

}
