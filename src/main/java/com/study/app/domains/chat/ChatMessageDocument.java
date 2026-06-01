package com.study.app.domains.chat;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data				// Getter, Setter, toString 자동 생성
@NoArgsConstructor	// 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드 포함한 생성자 자동 생성
@Builder			// new 대신 빌더 패턴을 사용한 객체 자동 생성
@Document(collection="chat_messages") // 오라클 테이블 지정 느낌, 몽고디비에서는 컬렉션 이라고 부름
public class ChatMessageDocument {
// MongoDB용, 채팅메세지 컬럼명을 저장하는 클래스(DTO 느낌)
	
	@Id // 몽고디비의 PK 역할
	private String id; // String으로 선언해서, 데이터를 넣을 때 고유한 문자열을 알아서 1씩 증가 자동으로 채움.
	
	@Field("room_id") // 몽고디비에 저장하거나 꺼내올때는 알아서 스케이크케이스로 읽어주는 매핑
	private String roomId; // 자바 표준 문법 지키기.
	
	@Field("member_id") // 발신자 ID
	private String memberId;
	
	@Field("content") // 메세지 본문
	private String content;
	
	@Field("created_at") // 전송일시
	private LocalDateTime createdAt;
	
}
