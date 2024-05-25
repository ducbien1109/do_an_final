package com.FashionStore.controllers;

import com.FashionStore.DTO.PaymentRestDTO;
import com.FashionStore.DTO.TransactionStatusDTO;
import com.FashionStore.config.Config;
import com.FashionStore.models.*;
import com.FashionStore.repositories.*;
import com.FashionStore.security.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${order.param.orderID}")
    private String ORDER_PARAM_ORDER_ID;

    @Value("${order.param.addressID}")
    private String ORDER_PARAM_ADDRESS_ID;

    @Value("${order.param.totalAmount}")
    private String ORDER_PARAM_TOTAL_AMOUNT;

    @Value("${order.param.productID}")
    private String ORDER_PARAM_PRODUCT_ID;

    @Value("${order.param.sizeID}")
    private String ORDER_PARAM_SIZE_ID;

    @Value("${order.param.quantityPurchase}")
    private String ORDER_PARAM_QUANTITY_PURCHASE;

    @Value("${order.param.orderStatus}")
    private String ORDER_PARAM_ORDER_STATUS;

    @Value("${order.param.userID}")
    private String ORDER_PARAM_USER_ID;

    @Value("${order.param.recipientPhone}")
    private String ORDER_PARAM_RECIPIENT_PHONE;

    @Value("${order.param.startOrderDate}")
    private String ORDER_PARAM_START_ORDER_DATE;

    @Value("${order.param.endOrderDate}")
    private String ORDER_PARAM_END_ORDER_DATE;

    @Value("${header.authorization}")
    private String HEADER_AUTHORIZATION;

    @Value("${authorization.bearer}")
    private String AUTHORIZATION_BEARER;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OrdersController ordersController;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    private final String ORDER_STATUS_PENDING = "Chờ xác nhận";

@PostMapping ("/create_payment")
public ResponseEntity<?> createPayment(HttpServletRequest request) throws UnsupportedEncodingException {
    Long addressId = Long.valueOf(request.getParameter("addressID"));
    Long totalAmount = Long.valueOf(request.getParameter("totalAmount"));
    Long productId = Long.valueOf(request.getParameter("productID"));
    Long sizeId = Long.valueOf(request.getParameter("sizeID"));
    Long quantityPurchase = Long.valueOf(request.getParameter("quantityPurchase"));

    Address address = addressRepository.findAddressByAddressID(addressId);
    Users findByEmail = usersRepository.findUsersByUserID(1L);
    Date orderDate = new Date();
    Users users = findByEmail;
    Long userID = users.getUserID();
    Orders orders = new Orders(orderDate, totalAmount, ORDER_STATUS_PENDING, userID,
            address.getRecipientName(), address.getRecipientPhone(), address.getAddressDetails());
    ordersRepository.save(orders);

    List<OrderDetails> orderDetailsList = new ArrayList<>();
    Orders ordersSave = ordersRepository.save(orders);
    Product product = ordersController.getProductDetails(Long.valueOf(productId));
    ProductSize productSize = productSizeRepository.findProductSizeBySizeID(sizeId);
    OrderDetails orderDetails = new OrderDetails(orders.getOrderID(), product.getProductID(),
            product.getProductName(),
            String.valueOf(product.getProductImages().get(0).getImagePath()), String.valueOf(productSize.getSizeName()), product.getProductPrice(), quantityPurchase,
            product.getProductPrice() * quantityPurchase);
    orderDetailsList.add(orderDetails);
    orderDetailsRepository.save(orderDetails);
    String vnp_TxnRef = Config.getRandomNumber(8);
    String vnp_IpAddr = "192.168.73.100";

    String vnp_TmnCode = Config.vnp_TmnCode;

    Map<String, String> vnp_Params = new HashMap<>();
    vnp_Params.put("vnp_Version", Config.vnp_Version);
    vnp_Params.put("vnp_Command", Config.vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(totalAmount*100));
    vnp_Params.put("vnp_CurrCode", "VND");
    vnp_Params.put("vnp_BankCode", "NCB");
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        vnp_Params.put("vnp_OrderInfo", String.valueOf(ordersSave.getOrderID()));
    }catch (Exception e){

    }
    vnp_Params.put("vnp_Locale", "vn");
    vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
    vnp_Params.put("vnp_OrderType", Config.orderType);

    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    List fieldNames = new ArrayList(vnp_Params.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator itr = fieldNames.iterator();
    while (itr.hasNext()) {
        String fieldName = (String) itr.next();
        String fieldValue = (String) vnp_Params.get(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            //Build hash data
            hashData.append(fieldName);
            hashData.append('=');
            hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            //Build query
            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
            query.append('=');
            query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            if (itr.hasNext()) {
                query.append('&');
                hashData.append('&');
            }
        }
    }
    String queryUrl = query.toString();
    String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

    PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
    paymentRestDTO.setStatus("Ok");
    paymentRestDTO.setUrl(paymentUrl);
    return ResponseEntity.ok(paymentRestDTO);

}
    @PostMapping ("/create_payment_order")
    public ResponseEntity<?> createPaymentOrder(HttpServletRequest request) throws UnsupportedEncodingException {
        Long addressId = Long.valueOf(request.getParameter("addressID"));
        Long totalAmount = Long.valueOf(request.getParameter("totalAmount"));
        Long productId = Long.valueOf(request.getParameter("productID"));
        Long sizeId = Long.valueOf(request.getParameter("sizeID"));
        Long quantityPurchase = Long.valueOf(request.getParameter("quantityPurchase"));

        Address address = addressRepository.findAddressByAddressID(addressId);
        Users findByEmail = usersRepository.findUsersByUserID(1L);
        Date orderDate = new Date();
        Users users = findByEmail;
        Long userID = users.getUserID();
        Orders orders = new Orders(orderDate, totalAmount, ORDER_STATUS_PENDING, userID,
                address.getRecipientName(), address.getRecipientPhone(), address.getAddressDetails());
        ordersRepository.save(orders);

        Cart cart = cartRepository.findCartByUserID(userID);

        if (cart == null) {
            ResponseObject responseObject = new ResponseObject("1");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseObject);
        }
        List<CartItem> cartItems = cartItemRepository.findCartItemByCartID(cart.getCartID());

        if (cartItems.isEmpty()) {
            ResponseObject responseObject = new ResponseObject("1");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseObject);
        }

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        Orders ordersSave = ordersRepository.save(orders);
        for (CartItem cartItem: cartItems) {
            Product product = ordersController.getProductDetails(cartItem.getProductID());
            ProductSize productSize = productSizeRepository.findProductSizeBySizeID(cartItem.getSizeID());
            OrderDetails orderDetails = new OrderDetails(orders.getOrderID(), product.getProductID(),
                    product.getProductName(),
                    String.valueOf(product.getProductImages().get(1).getImagePath()), String.valueOf(productSize.getSizeName()), product.getProductPrice(), cartItem.getQuantityPurchase(),
                    product.getProductPrice() * quantityPurchase);
            orderDetailsList.add(orderDetails);
            orderDetailsRepository.save(orderDetails);
            cartItemRepository.delete(cartItem);
        }
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "192.168.73.100";

        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            vnp_Params.put("vnp_OrderInfo", String.valueOf(ordersSave.getOrderID()));
        }catch (Exception e){

        }
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_OrderType", Config.orderType);

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        paymentRestDTO.setStatus("Ok");
        paymentRestDTO.setUrl(paymentUrl);
        return ResponseEntity.ok(paymentRestDTO);

    }
    @GetMapping("/payment_infor")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount",required = false)String amount,
            @RequestParam(value = "vnp_BankCode",required = false)String bankCode,
            @RequestParam(value = "vnp_OrderInfo",required = false)String orderInfo,
            @RequestParam(value = "vnp_ResponseCode",required = false)String responseCode
    ){

        TransactionStatusDTO transactionStatusDTO= new TransactionStatusDTO();
        if(responseCode.equals("00")){

            transactionStatusDTO.setStatus("OK");
            transactionStatusDTO.setMessage("Thanh toan thanh cong");
            transactionStatusDTO.setCode("OK OK");
        }else{

            transactionStatusDTO.setStatus("Faile");
            transactionStatusDTO.setMessage("Thanh toan khong thanh cong");
            transactionStatusDTO.setCode("NO no");

        }
        return ResponseEntity.ok(transactionStatusDTO);
    }
}
