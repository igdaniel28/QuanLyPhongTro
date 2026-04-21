package com.example.bigproject1.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenants")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    private String email;

    // Liên kết 1-1 với tài khoản User do Admin cấp
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}