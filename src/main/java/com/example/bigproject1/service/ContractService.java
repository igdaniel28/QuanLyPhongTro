package com.example.bigproject1.service;

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
        // 1. Lấy thông tin phòng và người thuê
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng!"));
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người thuê!"));

        // 2. Kiểm tra logic: Phòng đã có người ở chưa?
        if (room.getStatus() == Room.RoomStatus.OCCUPIED ||
                contractRepository.existsByRoomIdAndStatus(roomId, Contract.ContractStatus.ACTIVE)) {
            throw new RuntimeException("Phòng này đã có người thuê, không thể tạo hợp đồng mới!");
        }

        // 3. Gán dữ liệu vào hợp đồng
        contract.setRoom(room);
        contract.setTenant(tenant);
        contract.setStatus(Contract.ContractStatus.ACTIVE);

        // 4. Cập nhật trạng thái phòng thành "Đã có khách"
        room.setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // 5. Lưu hợp đồng
        contractRepository.save(contract);
    }
}