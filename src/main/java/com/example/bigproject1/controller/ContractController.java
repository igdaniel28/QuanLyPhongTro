package com.example.bigproject1.controller;

import com.example.bigproject1.model.Contract;
import com.example.bigproject1.service.ContractService;
import com.example.bigproject1.service.RoomService;
import com.example.bigproject1.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final RoomService roomService;
    private final TenantService tenantService;

    // Hiển thị form tạo hợp đồng
    @GetMapping("/add")
    public String showCreateContractForm(Model model) {
        model.addAttribute("contract", new Contract());

        // Truyền danh sách phòng và người thuê sang View để hiển thị trong thẻ <select>
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("tenants", tenantService.getAllTenants());

        return "contracts/form";
    }

    // Xử lý lưu hợp đồng khi submit form
    @PostMapping("/save")
    public String saveContract(@ModelAttribute("contract") Contract contract,
                               @RequestParam("roomId") Long roomId,
                               @RequestParam("tenantId") Long tenantId,
                               RedirectAttributes redirectAttributes) {
        try {
            // Gọi hàm xử lý logic: Tạo hợp đồng và tự động đổi trạng thái phòng sang "Đã có khách"
            contractService.createContract(contract, roomId, tenantId);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo hợp đồng thuê phòng thành công!");
        } catch (Exception e) {
            // Nếu có lỗi (phòng đã có người, không tìm thấy người thuê...), báo lỗi và quay lại form
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/contracts/add";
        }

        // Thành công thì chuyển hướng về danh sách phòng
        return "redirect:/admin/rooms";
    }
}