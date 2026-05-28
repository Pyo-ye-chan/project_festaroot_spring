package com.study.app.domains.chat;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

// MongoRepository<사용할Document클래스, Document 클래스 중 PK(id)의 데이터타입>을 상속받기
public interface ChatMessageRepository extends MongoRepository<ChatMessageDocument, String>{
	List<ChatMessageDocument> findByRoomIdOrderByCreatedAtAsc(Long roomId);
	// roomId가 들어왔을때, orderBy(정렬)를 asc(오름차순)으로 정렬해서 가져오는 코드
}
