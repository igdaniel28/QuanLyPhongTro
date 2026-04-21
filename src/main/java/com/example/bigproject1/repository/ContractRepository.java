package com.example.bigproject1.repository;

import com.example.bigproject1.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    // Kiểm tra xem phòng có hợp đồng nào đang ACTIVE không
    boolean existsByRoomIdAndStatus(Long roomId, Contract.ContractStatus status);
    List<Contract> findByRoomId(Long roomId);
}