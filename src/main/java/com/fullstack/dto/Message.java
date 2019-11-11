package com.fullstack.dto;

import com.fullstack.model.User;

public class Message {

	private String content;
	private User sender;
	private MessageType type;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public enum MessageType {
		CHAT, LEAV, JOIN
	}
}
