package com.example.app.controler;

import com.example.app.entity.Conversation;
import com.example.app.repository.ChatMessageRepository;
import com.example.app.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageRepository chatMessageRepository, ConversationRepository conversationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.conversationRepository = conversationRepository;
    }

    @MessageMapping("/chat/{conversationId}")
    public void sendMessage(@DestinationVariable Long conversationId, @Payload com.example.app.model.ChatMessage chatMessage) {
        // Lưu tin nhắn vào database
        com.example.app.entity.ChatMessage entity = new com.example.app.entity.ChatMessage();
        entity.setSender(chatMessage.getFrom());
        entity.setReceiver(chatMessage.getTo());
        entity.setContent(chatMessage.getContent());
        entity.setSentAt(LocalDateTime.now());
        // Gán conversation
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) return;
        entity.setConversation(conversation);
        chatMessageRepository.save(entity);
        // Bổ sung thông tin hội thoại vào chatMessage trả về client
        chatMessage.setConversationId(conversationId);
        chatMessage.setConversationName(conversation.getName());
        chatMessage.setUser1(conversation.getUser1());
        chatMessage.setUser2(conversation.getUser2());
        chatMessage.setTime(entity.getSentAt().toString()); // thêm thời gian gửi
        // Gửi tin nhắn đến topic conversation
        messagingTemplate.convertAndSend(
            "/topic/conversation/" + conversationId, chatMessage);
    }
}
