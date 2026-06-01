package com.study.app.domains.chat;

import java.time.LocalDateTime;

public class ChatRoomDTO {
	private Long room_id;
	private String room_type;
	private String owner_id;
	private String room_title;
	private LocalDateTime created_at;
	
	@Override
	public String toString() {
		return "ChatRoomDTO [room_id=" + room_id + ", room_type=" + room_type + ", owner_id=" + owner_id
				+ ", room_title=" + room_title + ", created_at=" + created_at + "]";
	}
	
	public ChatRoomDTO() {}
	public ChatRoomDTO(Long room_id, String room_type, String owner_id, String room_title, LocalDateTime created_at) {
		super();
		this.room_id = room_id;
		this.room_type = room_type;
		this.owner_id = owner_id;
		this.room_title = room_title;
		this.created_at = created_at;
	}



	public Long getRoom_id() {
		return room_id;
	}

	public void setRoom_id(Long room_id) {
		this.room_id = room_id;
	}

	public String getRoom_type() {
		return room_type;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getRoom_title() {
		return room_title;
	}

	public void setRoom_title(String room_title) {
		this.room_title = room_title;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	
	
}
