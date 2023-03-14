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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "quotation_product")
public class QuotationProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quotation_product_id", unique = true, nullable = false, updatable = false)
	private Long quotationProductId;

	@NotNull(message = "Quantity is missing")
	@Column(name = "quantity", nullable = false)
	private Long quantity;
	
	@Column(name = "product_price")
	private BigDecimal productPrice;
	
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "quotation_id", nullable = false)
	private Quotation quotation;

	
	@Column(name="product_gst_per")
	private BigDecimal productGstPer;
	
	@Column(name="product_gst_amount")
	private BigDecimal productGstAmount;
	
	@Column(name="product_tax_amount")
	private BigDecimal productTaxAmount;
	
	@Transient
	private Long productId;
	
	public Long getQuotationProductId() {
		return quotationProductId;
	}

	public void setQuotationProductId(Long quotationProductId) {
		this.quotationProductId = quotationProductId;
	}
	
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	
	
	
	}

	
	


