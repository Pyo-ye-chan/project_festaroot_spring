package com.study.app.domains.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

	@Autowired
	private ChatRoomDAO chatRoomDAO; // 인터페이스
	
	
}
