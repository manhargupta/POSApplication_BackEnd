package com.nagarro.pos.dto;

import com.nagarro.pos.model.Orders;

public class Order_TotalPrice {

	Orders order;
	float totalAmount;

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Order_TotalPrice(Orders order, float totalAmount) {
		super();
		this.order = order;
		this.totalAmount = totalAmount;
	}

}
