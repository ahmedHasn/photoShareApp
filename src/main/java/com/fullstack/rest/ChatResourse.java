package com.fullstack.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.dto.Message;

@RestController
public class ChatResourse {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
//	@MessageMapping("/test-chat")
//	public ResponseEntity<?> sendSpecific(@Payload Message msg, 
//			Principal principal, @Header("sessionId") String sessionId)throws Exception {
//		
//	}
}
