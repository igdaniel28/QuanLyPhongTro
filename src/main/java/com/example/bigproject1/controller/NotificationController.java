package com.example.bigproject1.controller;

import com.example.bigproject1.model.Notification;
import com.example.bigproject1.service.NotificationService;
import com.example.bigproject1.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final RoomService roomService;

    @GetMapping
    public String listNotifications(Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("notification", new Notification());
        return "notifications/list"; // Giao diện lát làm sau
    }

    @PostMapping("/send")
    public String sendNotification(@ModelAttribute Notification notification,
                                   @RequestParam(required = false) Long roomId,
                                   RedirectAttributes redirectAttributes) {
        notificationService.createNotification(notification, roomId);
        redirectAttributes.addFlashAttribute("successMessage", "Gửi thông báo thành công!");
        return "redirect:/admin/notifications";
    }
}