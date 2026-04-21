package com.example.bigproject1.service;

import com.example.bigproject1.exception.DuplicateUserException;
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

    @Transactional
    public void createTenantWithAccount(Tenant tenant, String username, String rawPassword) {
        // THAY ĐỔI: Sử dụng DuplicateUserException với thông báo chi tiết
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUserException("Tên đăng nhập '" + username + "' đã có người sử dụng! Vui lòng chọn một tên khác.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(User.Role.ROLE_TENANT);

        tenant.setUser(user);
        tenantRepository.save(tenant);
    }
}