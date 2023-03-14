package com.averta.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table
public class SellerProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seller_product_id", unique = true)
	private Long sllerProductId;

	@JsonBackReference(value = "seller_product_product_id")
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@JsonBackReference(value = "seller_product_seller_id")
	@ManyToOne
	@JoinColumn(name = "seller_id", nullable = false)
	private Seller seller;

	public Long getSllerProductId() {
		return sllerProductId;
	}

	public void setSllerProductId(Long sllerProductId) {
		this.sllerProductId = sllerProductId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

}
