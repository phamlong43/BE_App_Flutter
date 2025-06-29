package com.example.app.controler;

import org.springframework.web.bind.annotation.*;
import com.example.app.entity.ChatMessage;
import com.example.app.repository.ChatMessageRepository;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {
    private final ChatMessageRepository chatMessageRepository;

    public ChatHistoryController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(@RequestParam String user1, @RequestParam String user2) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.addAll(chatMessageRepository.findBySenderAndReceiverOrderBySentAtAsc(user1, user2));
        messages.addAll(chatMessageRepository.findBySenderAndReceiverOrderBySentAtAsc(user2, user1));
        messages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()));
        // Tránh lỗi tuần hoàn khi trả về entity (do JPA proxy), chỉ trả về DTO hoặc xóa trường conversation
        for (ChatMessage msg : messages) {
            msg.setConversation(null);
        }
        return messages;
    }
}
