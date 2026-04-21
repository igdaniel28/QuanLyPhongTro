package com.example.bigproject1.service;

import com.example.bigproject1.exception.InvalidNotificationException;
import com.example.bigproject1.exception.ResourceNotFoundException;
import com.example.bigproject1.model.Notification;
import com.example.bigproject1.model.Room;
import com.example.bigproject1.repository.NotificationRepository;
import com.example.bigproject1.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final RoomRepository roomRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Notification> getNotificationsForRoom(Long roomId) {
        return notificationRepository.findByTargetRoomIdOrTargetRoomIsNullOrderByCreatedAtDesc(roomId);
    }

    @Transactional
    public void createNotification(Notification notification, Long roomId) {
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new InvalidNotificationException("Tiêu đề thông báo không được để trống!");
        }
        if (notification.getContent() == null || notification.getContent().trim().isEmpty()) {
            throw new InvalidNotificationException("Nội dung thông báo không được để trống!");
        }

        if (roomId != null) { // Gửi riêng cho 1 phòng
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng đích để gửi thông báo!"));
            notification.setTargetRoom(room);
        } else { // Gửi cho tất cả
            notification.setTargetRoom(null);
        }

        notificationRepository.save(notification);
    }
}