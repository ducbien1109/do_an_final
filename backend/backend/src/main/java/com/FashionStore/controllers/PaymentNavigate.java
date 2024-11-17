package com.FashionStore.controllers;

import com.FashionStore.models.Orders;
import com.FashionStore.repositories.OrdersRepository;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping()
public class PaymentNavigate {
    @Autowired
    OrdersRepository ordersRepository;


    @GetMapping("/api")
    public ResponseEntity<?> tests(@RequestParam("vnp_Amount") String vnpAmount,
                                   @RequestParam("vnp_BankCode") String vnpBankCode,
                                   @RequestParam("vnp_BankTranNo") String vnpBankTranNo,
                                   @RequestParam("vnp_CardType") String vnpCardType,
                                   @RequestParam("vnp_OrderInfo") String vnpOrderInfo,
                                   @RequestParam("vnp_PayDate") String vnpPayDate,
                                   @RequestParam("vnp_ResponseCode") String vnpResponseCode,
                                   @RequestParam("vnp_TmnCode") String vnpTmnCode,
                                   @RequestParam("vnp_TransactionNo") String vnpTransactionNo,
                                   @RequestParam("vnp_TransactionStatus") String vnpTransactionStatus,
                                   @RequestParam("vnp_TxnRef") String vnpTxnRef,
                                   @RequestParam("vnp_TxnRef") String vnpSecureHash) throws IOException {
        System.out.println(vnpOrderInfo);
        Orders orders = ordersRepository.findOrdersByOrderID(Long.valueOf(vnpOrderInfo));
        orders.setOrderStatus("Chờ xác nhận");
        orders.setPayment("Thanh toán online");
        ordersRepository.save(orders);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "http://localhost:3000/profile/orders?userID=" + orders.getUserID());
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
