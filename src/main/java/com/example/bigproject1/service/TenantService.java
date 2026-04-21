package com.example.bigproject1.service;

import com.example.bigproject1.model.Tenant;
import com.example.bigproject1.model.User;
import com.example.bigproject1.repository.TenantRepository;
import com.example.bigproject1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Transactional // Đảm bảo nếu lỗi thì rollback cả User và Tenant
    public void createTenantWithAccount(Tenant tenant, String username, String rawPassword) {
        // 1. Kiểm tra xem username đã tồn tại chưa
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
        }

        // 2. Tạo tài khoản User cho người thuê
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // Mã hóa mật khẩu
        user.setRole(User.Role.ROLE_TENANT);

        // 3. Liên kết User vào Tenant và lưu vào DB
        tenant.setUser(user);
        tenantRepository.save(tenant);
    }
}