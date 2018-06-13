package com.nagarro.pos.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nagarro.pos.model.Cart;

public class CartRequestDto {

	@JsonProperty("response")
	private Cart cart;

	private boolean status;

	public CartRequestDto() {
		this.status = true;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public CartRequestDto(Cart cart) {
		super();
		this.cart = cart;
		this.status = true;
	}

	public CartRequestDto(Cart cart, boolean status) {
		super();
		this.cart = cart;
		this.status = status;
	}

}
