package com.example.app.repository;

import com.example.app.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findByUser1AndUser2(String user1, String user2);
    Conversation findByUser2AndUser1(String user2, String user1);
}
