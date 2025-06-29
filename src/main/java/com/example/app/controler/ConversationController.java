package com.example.app.controler;

import com.example.app.entity.Conversation;
import com.example.app.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    @Autowired
    private ConversationRepository conversationRepository;

    @PostMapping("/create")
    public ResponseEntity<Conversation> createConversation(@RequestParam String user1, @RequestParam String user2, @RequestParam(required = false) String name) {
        // Kiểm tra đã tồn tại conversation giữa 2 user chưa (cả 2 chiều)
        Conversation existing = conversationRepository.findByUser1AndUser2(user1, user2);
        if (existing == null) {
            existing = conversationRepository.findByUser2AndUser1(user1, user2);
        }
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        Conversation conversation = new Conversation();
        conversation.setUser1(user1);
        conversation.setUser2(user2);
        conversation.setName(name != null ? name : user1 + "_" + user2);
        conversationRepository.save(conversation);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/find")
    public ResponseEntity<Conversation> findConversation(@RequestParam String user1, @RequestParam String user2) {
        Conversation conversation = conversationRepository.findByUser1AndUser2(user1, user2);
        if (conversation == null) {
            conversation = conversationRepository.findByUser2AndUser1(user1, user2);
        }
        if (conversation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long id) {
        return conversationRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
