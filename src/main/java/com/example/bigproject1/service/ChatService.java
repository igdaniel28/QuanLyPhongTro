package com.example.bigproject1.service;

import com.example.bigproject1.exception.ResourceNotFoundException;
import com.example.bigproject1.model.ChatMessage;
import com.example.bigproject1.model.User;
import com.example.bigproject1.repository.ChatMessageRepository;
import com.example.bigproject1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public List<ChatMessage> getChatHistory() {
        return chatMessageRepository.findTop50ByOrderByTimestampAsc();
    }

    @Transactional
    public ChatMessage saveMessage(String username, String content) {
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Tài khoản không tồn tại trong hệ thống!"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setContent(content);
        return chatMessageRepository.save(message);
    }
}