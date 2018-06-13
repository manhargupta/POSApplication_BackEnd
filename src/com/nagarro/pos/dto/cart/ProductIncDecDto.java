package com.nagarro.pos.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nagarro.pos.model.CartProductMapper;

public class ProductIncDecDto {
	@JsonProperty("response")
	private CartProductMapper cartProductMapper;

	public CartProductMapper getCartProductMapper() {
		return cartProductMapper;
	}

	public void setCartProductMapper(CartProductMapper cartProductMapper) {
		this.cartProductMapper = cartProductMapper;
	}

}
