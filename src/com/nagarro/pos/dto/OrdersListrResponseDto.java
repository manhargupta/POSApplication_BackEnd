package com.nagarro.pos.dto;

import java.util.List;
import java.util.Map;

public class OrdersListrResponseDto {

	Map<String, List<Order_TotalPrice>> orders;
	boolean status = true;

	public Map<String, List<Order_TotalPrice>> getOrders() {
		return orders;
	}

	public void setOrders(Map<String, List<Order_TotalPrice>> orders) {
		this.orders = orders;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public OrdersListrResponseDto(Map<String, List<Order_TotalPrice>> orders) {
		super();
		this.orders = orders;
	}

}
