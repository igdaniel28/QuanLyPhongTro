package com.example.bigproject1.controller;

import com.example.bigproject1.model.ChatMessage;
import com.example.bigproject1.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // Trả về giao diện phòng chat
    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("history", chatService.getChatHistory());
        return "chat/room"; // Lát làm giao diện sau
    }

    // Nhận tin nhắn từ Client và Broadcast (Gửi) lại cho mọi người
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        // Principal chứa thông tin user đang đăng nhập hiện tại
        String username = principal.getName();
        // Lưu tin nhắn vào DB và trả về để gửi tới tất cả người dùng
        return chatService.saveMessage(username, chatMessage.getContent());
    }
}