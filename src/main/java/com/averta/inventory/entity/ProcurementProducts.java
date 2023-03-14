package com.averta.inventory.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "procurement_products")
public class ProcurementProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "procurement_products_id")
	private Long procurementProductsId;

	@NotNull(message = "Quantity is missing")
	@Column(name = "quantity", nullable = false)
	private Long quantity;

	@Column(name = "product_price")
	private BigDecimal productPrice;

	@Column(name = "product_gst_per")
	private BigDecimal productGstPer;

	@Column(name = "product_gst_amount")
	private BigDecimal productGstAmount;

	@Column(name = "product_tax_amount") // total amount with gst
	private BigDecimal productTaxAmount;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "procurement_id", nullable = false)
	private Procurement procurement;

	@Column(name = "available_quantity")
	private Long availableQuantity;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(name = "batch_name")
	private String BatchName;

	@Transient
	private Long productId;

	public Long getProcurementProductsId() {
		return procurementProductsId;
	}

	public void setProcurementProductsId(Long procurementProductsId) {
		this.procurementProductsId = procurementProductsId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getProductGstPer() {
		return productGstPer;
	}

	public void setProductGstPer(BigDecimal productGstPer) {
		this.productGstPer = productGstPer;
	}

	public BigDecimal getProductGstAmount() {
		return productGstAmount;
	}

	public void setProductGstAmount(BigDecimal productGstAmount) {
		this.productGstAmount = productGstAmount;
	}

	public BigDecimal getProductTaxAmount() {
		return productTaxAmount;
	}

	public void setProductTaxAmount(BigDecimal productTaxAmount) {
		this.productTaxAmount = productTaxAmount;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getBatchName() {
		return BatchName;
	}

	public void setBatchName(String batchName) {
		BatchName = batchName;
	}

}
