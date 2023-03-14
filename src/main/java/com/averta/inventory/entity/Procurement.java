package com.averta.inventory.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "procurement")
public class Procurement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "procurement_id", unique = true, nullable = false, updatable = false)
	private Long procurementId;

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

	@Column(name = "bill_number", length = 50)
	private String billNumber;

	@OneToOne
	@JoinColumn(name = "SellerId", nullable = false)
	private Seller seller;

	@Column(name = "bill_date")
	private Date billDate;

	@Transient
	private Long sellerId;

	@Column(name = "gstin_number")
	private String gstinNumber;

	@Column(name = "discound")
	private BigDecimal discount;

	@Column(name = "total_tax")
	private BigDecimal totalTax;

	@Column(name = "final_amount")
	private BigDecimal finalAmount;

	@Column(name = "s_gst")
	private BigDecimal sGst;

	@Column(name = "c_gst")
	private BigDecimal cGst;

	@Transient
	private List<Procurement> procurements = new ArrayList<>();

	@Transient
	private List<ProcurementProducts> ProcurementProducts = new ArrayList<>();

	public Long getProcurementId() {
		return procurementId;
	}

	public void setProcurementId(Long procurementId) {
		this.procurementId = procurementId;
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

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getGstinNumber() {
		return gstinNumber;
	}

	public void setGstinNumber(String gstinNumber) {
		this.gstinNumber = gstinNumber;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
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

	public List<Procurement> getProcurements() {
		return procurements;
	}

	public void setProcurements(List<Procurement> procurements) {
		this.procurements = procurements;
	}

	public List<ProcurementProducts> getProcurementProducts() {
		return ProcurementProducts;
	}

	public void setProcurementProducts(List<ProcurementProducts> procurementProducts) {
		ProcurementProducts = procurementProducts;
	}
	
	
}
