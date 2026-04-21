package com.example.bigproject1.repository;

import com.example.bigproject1.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Lấy 50 tin nhắn gần nhất để hiển thị lịch sử chat
    List<ChatMessage> findTop50ByOrderByTimestampAsc();
}