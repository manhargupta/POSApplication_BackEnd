package com.nagarro.pos.dto.cart;

import com.nagarro.pos.model.Product;

public class AddToCartDto {

	private Product product;

	private boolean status;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public AddToCartDto(Product product, boolean status) {
		super();
		this.product = product;
		this.status = status;
	}

}
