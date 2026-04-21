package com.example.bigproject1.repository;

import com.example.bigproject1.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Lấy thông báo theo phòng (hoặc thông báo chung có targetRoom là null), sắp xếp mới nhất lên đầu
    List<Notification> findByTargetRoomIdOrTargetRoomIsNullOrderByCreatedAtDesc(Long roomId);

    // Admin lấy tất cả thông báo
    List<Notification> findAllByOrderByCreatedAtDesc();
}