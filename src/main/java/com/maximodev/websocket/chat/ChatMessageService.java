package com.maximodev.websocket.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maximodev.websocket.chatroom.ChatRoomService;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage){
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                            .orElseThrow();
        chatMessage.setChatId(chatId);
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findChatMenssages(String senderId, String recipientId){
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);

        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());

    }

    
}
