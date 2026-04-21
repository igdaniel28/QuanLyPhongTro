package com.example.bigproject1.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi không tìm thấy dữ liệu (ID không tồn tại)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/rooms");
    }

    // Gộp chung tất cả các lỗi nghiệp vụ (Business Logic) vào đây
    @ExceptionHandler({
            DuplicateRoomCodeException.class,
            RoomOccupiedException.class,
            DuplicateUserException.class,
            InvalidContractException.class,
            DuplicateInvoiceException.class,
            InvalidInvoiceException.class
    })
    public String handleBusinessException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        // Trả về đúng trang mà người dùng vừa thao tác bị lỗi
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/rooms");
    }
}