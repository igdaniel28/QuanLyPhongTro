package com.example.bigproject1.controller;

import com.example.bigproject1.model.Tenant;
import com.example.bigproject1.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // Hiển thị danh sách khách thuê
    @GetMapping
    public String listTenants(Model model) {
        model.addAttribute("tenants", tenantService.getAllTenants());
        return "tenants/list";
    }

    // Hiển thị form thêm khách thuê mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("tenant", new Tenant());
        return "tenants/form";
    }

    // Xử lý lưu khách thuê và tạo tài khoản
    @PostMapping("/save")
    public String saveTenant(@ModelAttribute("tenant") Tenant tenant,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             RedirectAttributes redirectAttributes) {
        try {
            tenantService.createTenantWithAccount(tenant, username, password);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm khách thuê và cấp tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/tenants/add";
        }
        return "redirect:/admin/tenants";
    }
}