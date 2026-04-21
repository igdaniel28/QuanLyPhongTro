package com.example.bigproject1.controller;

import com.example.bigproject1.model.Invoice;
import com.example.bigproject1.service.InvoiceService;
import com.example.bigproject1.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final RoomService roomService;

    // 1. Hiển thị danh sách tất cả hóa đơn
    @GetMapping
    public String listInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "invoices/list";
    }

    // 2. Hiển thị form tạo hóa đơn (nhập chỉ số điện nước)
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Invoice invoice = new Invoice();
        // Mặc định lấy tháng và năm hiện tại
        invoice.setMonth(LocalDate.now().getMonthValue());
        invoice.setYear(LocalDate.now().getYear());

        model.addAttribute("invoice", invoice);
        model.addAttribute("rooms", roomService.getAllRooms()); // Truyền danh sách phòng để chọn
        return "invoices/form";
    }

    // 3. Xử lý lưu hóa đơn mới
    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice,
                              @RequestParam("roomId") Long roomId,
                              RedirectAttributes redirectAttributes) {
        // Mọi logic kiểm tra lỗi đã được đẩy xuống InvoiceService và chặn bởi GlobalExceptionHandler
        invoiceService.createInvoice(invoice, roomId);
        redirectAttributes.addFlashAttribute("successMessage", "Chốt hóa đơn tiền phòng thành công!");
        return "redirect:/admin/invoices";
    }

    // 4. Xác nhận người thuê đã thanh toán
    @GetMapping("/confirm/{id}")
    public String confirmPayment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        invoiceService.confirmPayment(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xác nhận thanh toán thành công!");
        return "redirect:/admin/invoices";
    }
}