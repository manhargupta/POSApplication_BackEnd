package com.nagarro.pos.dto;

import java.util.List;

import com.nagarro.pos.model.Product;

public class ProductListDto {

	List<Product> products;
	boolean status = true;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ProductListDto(List<Product> products) {
		super();
		this.products = products;
	
	}

}
