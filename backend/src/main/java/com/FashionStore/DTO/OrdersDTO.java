package com.FashionStore.DTO;

//import com.example.h2_shop.model.Product;
//import com.example.h2_shop.model.User;
//import jakarta.persistence.*;

import java.time.Instant;

public class OrdersDTO {
    private Long id;

    private String orderCode;
    private Instant orderDate;
    private Integer paymentMethod;//type_apparams PAYMENT

    private String phoneNumber;

    private String recipientAddress;

    private String buyerAddress;
    private Long provinceId;
    private Long districtId;
    private String ward;
    private String fullName;
    private Long payStatus;

    private Long quantity;

    private Long status;

    private String comment;

    private Long price;// giá của tổng sản phẩm được bán ra

    private String estimatePickUp;

    private Long rating;
    private Long shippingUnit; // type_apparams SHIPUNIT

    private float shipPrice;

    private Long userId;
    private Long productId;


//    private User user;
//
//    private Product product;
//
//    private List<OrderDetailDTO> orderDetailDTOList;

    private Long saleId; // mã giảm giá cho hóa đơn


//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public OrdersDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getEstimatePickUp() {
        return estimatePickUp;
    }

    public void setEstimatePickUp(String estimatePickUp) {
        this.estimatePickUp = estimatePickUp;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }


    public Long getShippingUnit() {
        return shippingUnit;
    }

    public void setShippingUnit(Long shippingUnit) {
        this.shippingUnit = shippingUnit;
    }

    public float getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(float shipPrice) {
        this.shipPrice = shipPrice;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

//    public List<OrderDetailDTO> getOrderDetailDTOList() {
//        return orderDetailDTOList;
//    }
//
//    public void setOrderDetailDTOList(List<OrderDetailDTO> orderDetailDTOList) {
//        this.orderDetailDTOList = orderDetailDTOList;
//    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
    public Long getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Long payStatus) {
        this.payStatus = payStatus;
    }

}
