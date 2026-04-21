package com.example.bigproject1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "invoice_month", nullable = false)
    private Integer month;

    @Column(name = "invoice_year", nullable = false)
    private Integer year;

    // Chỉ số điện nước
    private Integer oldElecIndex;
    private Integer newElecIndex;
    private Integer oldWaterIndex;
    private Integer newWaterIndex;

    // Đơn giá áp dụng tại thời điểm chốt (đề phòng sau này đổi giá thì hóa đơn cũ không bị ảnh hưởng)
    private Double elecPrice;
    private Double waterPrice;

    // Chi tiết tiền từng khoản
    private Double roomPriceTotal;
    private Double elecPriceTotal;
    private Double waterPriceTotal;
    private Double serviceFeeTotal;

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.UNPAID;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum PaymentStatus {
        UNPAID, // Chưa thanh toán
        PAID    // Đã thanh toán
    }
}