package com.example.bigproject1.service;

import com.example.bigproject1.exception.InvalidContractException;
import com.example.bigproject1.exception.ResourceNotFoundException;
import com.example.bigproject1.model.Contract;
import com.example.bigproject1.model.Room;
import com.example.bigproject1.model.Tenant;
import com.example.bigproject1.repository.ContractRepository;
import com.example.bigproject1.repository.RoomRepository;
import com.example.bigproject1.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;

    @Transactional
    public void createContract(Contract contract, Long roomId, Long tenantId) {
        // THAY ĐỔI: Sử dụng ResourceNotFoundException
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Hệ thống không tìm thấy dữ liệu phòng với ID: " + roomId));

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Hệ thống không tìm thấy dữ liệu khách thuê với ID: " + tenantId));

        // THAY ĐỔI: Sử dụng InvalidContractException với thông báo chi tiết
        if (room.getStatus() == Room.RoomStatus.OCCUPIED ||
                contractRepository.existsByRoomIdAndStatus(roomId, Contract.ContractStatus.ACTIVE)) {
            throw new InvalidContractException("Phòng [" + room.getRoomCode() + "] hiện đã có người thuê, không thể tạo hợp đồng mới!");
        }

        contract.setRoom(room);
        contract.setTenant(tenant);
        contract.setStatus(Contract.ContractStatus.ACTIVE);

        room.setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(room);
        contractRepository.save(contract);
    }
}