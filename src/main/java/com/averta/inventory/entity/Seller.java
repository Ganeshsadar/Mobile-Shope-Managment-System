package com.averta.inventory.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Table(name = "seller")
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seller_id", unique = true)
	private Long sellerId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "state")
	private String state;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "address")
	private String address;

	@Email(message = "Please enter valid email")
	@Column(name = "email",nullable = true)
	private String email;

	@Column(name = "gsnt_number")
	private String gstnNumber;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
	private List<SellerProduct> sellerProduct = new ArrayList<>();

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<SellerProduct> getSellerProduct() {
		return sellerProduct;
	}

	public void setSellerProduct(List<SellerProduct> sellerProduct) {
		this.sellerProduct = sellerProduct;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getGstnNumber() {
		return gstnNumber;
	}

	public void setGstnNumber(String gstnNumber) {
		this.gstnNumber = gstnNumber;
	}

	@Override
	public String toString() {
		return "Seller [sellerId=" + sellerId + ", name=" + name + ", mobileNumber=" + mobileNumber + ", state=" + state
				+ ", companyName=" + companyName + ", address=" + address + ", email=" + email + ", gstnNumber="
				+ gstnNumber + ", sellerProduct=" + sellerProduct + ", getSellerId()=" + getSellerId() + ", getName()="
				+ getName() + ", getMobileNumber()=" + getMobileNumber() + ", getState()=" + getState()
				+ ", getAddress()=" + getAddress() + ", getEmail()=" + getEmail() + ", getSellerProduct()="
				+ getSellerProduct() + ", getCompanyName()=" + getCompanyName() + ", getGstnNumber()=" + getGstnNumber()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
