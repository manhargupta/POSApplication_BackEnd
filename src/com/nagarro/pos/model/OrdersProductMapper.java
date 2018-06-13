package com.nagarro.pos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Entity
@Table(name = "Orders_Product")
public class OrdersProductMapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6145500085147756545L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "orderId")
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;

	@Column(name = "quantity")
	private int quantity;

	public OrdersProductMapper() {
	}
	public OrdersProductMapper(Orders orders, Product product, int quantity) {
		super();
		this.orders = orders;
		this.product = product;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
