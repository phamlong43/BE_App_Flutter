package com.example.app.repository;

import com.example.app.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrderBySentAtAsc(String sender, String receiver);
    List<ChatMessage> findByReceiverAndSenderOrderBySentAtAsc(String receiver, String sender);
}
