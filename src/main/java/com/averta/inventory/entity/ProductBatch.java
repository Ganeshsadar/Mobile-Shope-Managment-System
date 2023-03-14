/**
 * 
 */
package com.averta.inventory.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="product_batch")
public class ProductBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_id", unique = true)
	private Long batchId;
	
	@Column(name="batch_name")
	private String batchName;
	
	@ManyToOne
	@JoinColumn(name="productId",nullable = false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="procurementId",nullable = false)
	private Procurement procurement;
	
	@Column(name="product_price")
	private BigDecimal ProductPrice; 
	
	@Column(name = "quantity")
	private Long quantity;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Procurement getProcurement() {
		return procurement;
	}

	public void setProcurement(Procurement procurement) {
		this.procurement = procurement;
	}

	public BigDecimal getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		ProductPrice = productPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	
}
