package com.example.bigproject1.repository;

import com.example.bigproject1.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Kiểm tra xem phòng này đã có hóa đơn trong tháng/năm này chưa
    boolean existsByRoomIdAndMonthAndYear(Long roomId, Integer month, Integer year);

    // Lấy danh sách hóa đơn theo tháng năm (để Admin xem tổng quát)
    List<Invoice> findByMonthAndYear(Integer month, Integer year);
}