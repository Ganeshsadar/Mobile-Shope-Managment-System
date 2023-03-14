package com.averta.inventory.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id", unique = true, nullable = false, updatable = false)
	private Long invoiceId;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Column(name = "invoice_number", length = 50)
	private String invoiceNumber;

	@Column(name = "due_date")
	@DateTimeFormat
	private Date dueDate;

	@OneToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@DateTimeFormat
	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Transient
	private Long customerId;

	@Column(name = "gstin_number")
	private String gstinNumber;

	@Column(name = "discount")
	private BigDecimal discount;

	@Column(name = "status")
	private String status;

	@OneToOne
	@JoinColumn(name = "quotation_id")
	private Quotation quotation;

	@Column(name = "total_tax")
	private BigDecimal totalTax;

	@Column(name = "final_amount")
	private BigDecimal finalAmount;

	@Column(name = "s_gst")
	private BigDecimal sGst;

	@Column(name = "c_gst")
	private BigDecimal cGst;

	@Column(name = "with_gst")
	private Boolean withGst;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
	private List<InvoiceProduct> invoiceProduct = new ArrayList<>();

	@Transient
	private Long quotationId;

	@Transient
	private List<Invoice> invoices = new ArrayList<>();

	@Transient
	private List<InvoiceProduct> invoiceProducts = new ArrayList<>();

	@Transient
	private List<ProcurementProducts> procurementProducts = new ArrayList<>();

	public List<ProcurementProducts> getProcurementProducts() {
		return procurementProducts;
	}

	public void setProcurementProducts(List<ProcurementProducts> procurementProducts) {
		this.procurementProducts = procurementProducts;
	}

	public String getGstinNumber() {
		return gstinNumber;
	}

	public void setGstinNumber(String gstinNumber) {
		this.gstinNumber = gstinNumber;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public Long getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public List<InvoiceProduct> getInvoiceProducts() {
		return invoiceProducts;
	}

	public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
		this.invoiceProducts = invoiceProducts;
	}

	public BigDecimal getsGst() {
		return sGst;
	}

	public void setsGst(BigDecimal sGst) {
		this.sGst = sGst;
	}

	public BigDecimal getcGst() {
		return cGst;
	}

	public void setcGst(BigDecimal cGst) {
		this.cGst = cGst;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public List<InvoiceProduct> getInvoiceProduct() {
		return invoiceProduct;
	}

	public void setInvoiceProduct(List<InvoiceProduct> invoiceProduct) {
		this.invoiceProduct = invoiceProduct;
	}

	public Boolean getWithGst() {
		return withGst;
	}

	public void setWithGst(Boolean withGst) {
		this.withGst = withGst;
	}

}
