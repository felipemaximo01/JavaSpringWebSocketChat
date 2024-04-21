package com.maximodev.websocket.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage){
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        ChatNotification chatNotification = new ChatNotification();
        chatNotification.setId(savedMsg.getId());
        chatNotification.setContent(savedMsg.getContent());
        chatNotification.setRecipientId(savedMsg.getRecipientId());
        chatNotification.setSenderId(savedMsg.getSenderId());
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", chatNotification);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,@PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMenssages(senderId, recipientId));
    }
    
    
}
