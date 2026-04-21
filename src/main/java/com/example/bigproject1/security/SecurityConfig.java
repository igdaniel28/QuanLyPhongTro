package com.example.bigproject1.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/login").permitAll() // Mở khóa file tĩnh và trang đăng nhập
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ Admin được vào /admin/...
                        .requestMatchers("/tenant/**").hasRole("TENANT") // Chỉ người thuê được vào /tenant/...
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Đường dẫn tới trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/process-login")
                        .successHandler((request, response, authentication) -> {
                            // Chuyển hướng dựa theo Role sau khi đăng nhập thành công
                            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                response.sendRedirect("/admin/rooms");
                            } else {
                                response.sendRedirect("/tenant/dashboard");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}