package com.example.bigproject1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ràng buộc N-1: Nhiều hợp đồng có thể thuộc về 1 phòng (lưu lịch sử)
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // Ràng buộc N-1: 1 người thuê có thể có nhiều hợp đồng (lưu lịch sử các lần thuê)
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate; // Cho phép null nếu hợp đồng thuê vô thời hạn

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status = ContractStatus.ACTIVE;

    public enum ContractStatus {
        ACTIVE,      // Đang có hiệu lực
        EXPIRED,     // Đã hết hạn
        TERMINATED   // Đã chấm dứt (trước thời hạn)
    }
}