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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="bill")
public class Bill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_id", unique = true, nullable = false, updatable = false)
	private Long billiId;
	
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
	
	@Column(name = "bill_number")
	private Long billNumber;
	
	@Column(name="due_date")
	@DateTimeFormat
	private Date dueDate;
	
	@OneToOne
	@JoinColumn(name="customer_id",nullable=false)
	private Customer customer;
	
	@Transient
	private Long customerId;
		
	@Column(name="gstin_number")
	private BigDecimal gstinNumber;
	
	@Column(name="discount")
	private BigDecimal discount;
	
	@Column(name="status")
	private String status;	
	
	@OneToOne
	@JoinColumn(name="invoice_id")
	private Invoice invoice;
	
	@Column(name="total_tax")
	private BigDecimal totalTax;
	
	@Column(name="final_amount")
	private BigDecimal finalAmount;
	
	@Transient
	private Long invoiceId;
	
	@Transient
	private List<Bill> bills =new ArrayList<>();
	
//	@Transient
//	private List<BillProduct> billProducts=new ArrayList<>();
//	       
	
}
