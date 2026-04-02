package com.example.bigproject1.repository;

import com.example.bigproject1.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomCode(String roomCode);

    Optional<Room> findByRoomCode(String roomCode);
}