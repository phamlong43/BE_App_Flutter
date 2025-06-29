package com.example.app.controler;

import com.example.app.entity.Conversation;
import com.example.app.model.ChatMessage;
import com.example.app.repository.ChatMessageRepository;
import com.example.app.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage chatMessage) {
        Conversation conversation = null;
        // Nếu có conversationId thì tìm theo id
        if (chatMessage.getConversationId() != null) {
            conversation = conversationRepository.findById(chatMessage.getConversationId()).orElse(null);
        }
        // Nếu chưa có, tìm hội thoại giữa 2 user (cả 2 chiều)
        if (conversation == null) {
            conversation = conversationRepository.findByUser1AndUser2(chatMessage.getFrom(), chatMessage.getTo());
            if (conversation == null) {
                conversation = conversationRepository.findByUser1AndUser2(chatMessage.getTo(), chatMessage.getFrom());
            }
        }
        // Nếu vẫn chưa có thì tạo mới
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setUser1(chatMessage.getFrom());
            conversation.setUser2(chatMessage.getTo());
            conversation.setName(chatMessage.getFrom() + "_" + chatMessage.getTo());
            conversation = conversationRepository.save(conversation);
        }
        com.example.app.entity.ChatMessage entity = new com.example.app.entity.ChatMessage();
        entity.setSender(chatMessage.getFrom());
        entity.setReceiver(chatMessage.getTo());
        entity.setContent(chatMessage.getContent());
        entity.setSentAt(LocalDateTime.now());
        entity.setConversation(conversation);
        chatMessageRepository.save(entity);
        // Bổ sung thông tin hội thoại và thời gian gửi vào response
        chatMessage.setConversationId(conversation.getId());
        chatMessage.setConversationName(conversation.getName());
        chatMessage.setUser1(conversation.getUser1());
        chatMessage.setUser2(conversation.getUser2());
        chatMessage.setTime(entity.getSentAt().toString()); // thêm thời gian gửi vào response
        return ResponseEntity.ok(chatMessage);
    }
}
