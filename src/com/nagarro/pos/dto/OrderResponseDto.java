package com.nagarro.pos.dto;

import java.util.Date;

public class OrderResponseDto {
	
	String orderId;
	Date orderDate;
	String orderStatus;
	String paymentMade;
	boolean status;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPaymentMade() {
		return paymentMade;
	}
	public void setPaymentMade(String paymentMade) {
		this.paymentMade = paymentMade;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public OrderResponseDto(String orderId, Date orderDate, String orderStatus, String paymentMade, boolean status) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.paymentMade = paymentMade;
		this.status = status;
	}
	
	
	

}
