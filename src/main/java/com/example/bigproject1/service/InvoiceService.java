package com.example.bigproject1.service;

import com.example.bigproject1.exception.DuplicateInvoiceException;
import com.example.bigproject1.exception.InvalidInvoiceException;
import com.example.bigproject1.exception.ResourceNotFoundException;
import com.example.bigproject1.model.Invoice;
import com.example.bigproject1.model.Room;
import com.example.bigproject1.repository.InvoiceRepository;
import com.example.bigproject1.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final RoomRepository roomRepository;

    private final Double DEFAULT_ELEC_PRICE = 3500.0;
    private final Double DEFAULT_WATER_PRICE = 20000.0;
    private final Double DEFAULT_SERVICE_FEE = 100000.0;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public void createInvoice(Invoice invoice, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dữ liệu phòng với ID: " + roomId));

        if (room.getStatus() != Room.RoomStatus.OCCUPIED) {
            throw new InvalidInvoiceException("Phòng [" + room.getRoomCode() + "] hiện đang trống, không thể xuất hóa đơn!");
        }

        if (invoiceRepository.existsByRoomIdAndMonthAndYear(roomId, invoice.getMonth(), invoice.getYear())) {
            throw new DuplicateInvoiceException("Phòng [" + room.getRoomCode() + "] đã chốt hóa đơn tháng " + invoice.getMonth() + "/" + invoice.getYear() + ". Không thể tạo trùng!");
        }

        if (invoice.getOldElecIndex() < 0 || invoice.getNewElecIndex() < 0 ||
                invoice.getOldWaterIndex() < 0 || invoice.getNewWaterIndex() < 0) {
            throw new InvalidInvoiceException("Chỉ số điện và nước không được phép nhập số âm!");
        }

        if (invoice.getNewElecIndex() < invoice.getOldElecIndex()) {
            throw new InvalidInvoiceException("Chỉ số điện mới (" + invoice.getNewElecIndex() + ") không được nhỏ hơn chỉ số cũ (" + invoice.getOldElecIndex() + ")!");
        }

        if (invoice.getNewWaterIndex() < invoice.getOldWaterIndex()) {
            throw new InvalidInvoiceException("Chỉ số nước mới (" + invoice.getNewWaterIndex() + ") không được nhỏ hơn chỉ số cũ (" + invoice.getOldWaterIndex() + ")!");
        }

        invoice.setRoom(room);
        invoice.setElecPrice(DEFAULT_ELEC_PRICE);
        invoice.setWaterPrice(DEFAULT_WATER_PRICE);

        double roomTotal = room.getPrice();
        double elecTotal = (invoice.getNewElecIndex() - invoice.getOldElecIndex()) * DEFAULT_ELEC_PRICE;
        double waterTotal = (invoice.getNewWaterIndex() - invoice.getOldWaterIndex()) * DEFAULT_WATER_PRICE;
        double serviceTotal = DEFAULT_SERVICE_FEE;

        invoice.setRoomPriceTotal(roomTotal);
        invoice.setElecPriceTotal(elecTotal);
        invoice.setWaterPriceTotal(waterTotal);
        invoice.setServiceFeeTotal(serviceTotal);

        invoice.setTotalAmount(roomTotal + elecTotal + waterTotal + serviceTotal);
        invoice.setStatus(Invoice.PaymentStatus.UNPAID);

        invoiceRepository.save(invoice);
    }

    @Transactional
    public void confirmPayment(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dữ liệu hóa đơn với ID: " + invoiceId));

        if (invoice.getStatus() == Invoice.PaymentStatus.PAID) {
            throw new InvalidInvoiceException("Hóa đơn này đã được xác nhận thanh toán trước đó!");
        }

        invoice.setStatus(Invoice.PaymentStatus.PAID);
        invoiceRepository.save(invoice);
    }
}