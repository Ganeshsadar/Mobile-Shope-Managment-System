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
@Table(name = "invoice_product")
public class InvoiceProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_product_id", unique = true, nullable = false, updatable = false)
	private Long invoiceProductId;

	@NotNull(message = "Quantity is missing")
	@Column(name = "quantity", nullable = false)
	private Long quantity;

	@Column(name = "product_price")
	private BigDecimal productPrice;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;

	@Column(name = "product_gst_per")
	private BigDecimal productGstPer;

	@Column(name = "product_gst_amount")
	private BigDecimal productGstAmount;

	@Column(name = "product_tax_amount")
	private BigDecimal productTaxAmount;

	@ManyToOne
	@JoinColumn(name = "procurement_products_id")
	private ProcurementProducts procurementProducts;

	@Transient
	private Long productId;

	@Transient
	private Long procurementProductsId;

	public Long getInvoiceProductId() {
		return invoiceProductId;
	}

	public void setInvoiceProductId(Long invoiceProductId) {
		this.invoiceProductId = invoiceProductId;
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

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	// *

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

	public ProcurementProducts getProcurementProducts() {
		return procurementProducts;
	}

	public void setProcurementProducts(ProcurementProducts procurementProducts) {
		this.procurementProducts = procurementProducts;
	}

	public Long getProcurementProductsId() {
		return procurementProductsId;
	}

	public void setProcurementProductsId(Long procurementProductsId) {
		this.procurementProductsId = procurementProductsId;
	}

}
