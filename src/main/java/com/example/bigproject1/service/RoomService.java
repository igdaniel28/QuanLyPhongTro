package com.example.bigproject1.service;

import com.example.bigproject1.exception.DuplicateRoomCodeException;
import com.example.bigproject1.exception.ResourceNotFoundException;
import com.example.bigproject1.exception.RoomOccupiedException;
import com.example.bigproject1.model.Room;
import com.example.bigproject1.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + id));
    }

    public void saveRoom(Room room) {
        if (room.getId() == null) {
            if (roomRepository.existsByRoomCode(room.getRoomCode())) {
                throw new DuplicateRoomCodeException("Mã phòng đã tồn tại!");
            }
        } else {
            Room existingRoom = getRoomById(room.getId());
            if (!existingRoom.getRoomCode().equals(room.getRoomCode()) &&
                    roomRepository.existsByRoomCode(room.getRoomCode())) {
                throw new DuplicateRoomCodeException("Mã phòng đã tồn tại!");
            }
            room.setCreatedAt(existingRoom.getCreatedAt());
        }
        roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        if (room.getStatus() == Room.RoomStatus.OCCUPIED) {
            throw new RoomOccupiedException("Không thể xóa phòng đang có người ở!");
        }
        roomRepository.deleteById(id);
    }
}