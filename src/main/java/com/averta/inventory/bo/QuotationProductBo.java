package com.averta.inventory.bo;

public class QuotationProductBo {
	private String productName;

	private String hsnCode;

	private String productRate;

	private String quantity;

	private String productGstPer;

	private String totalPriceWithOutGst;

	private String productTaxAmount;

	private String sGst;

	private String cGst;
	
	private String quotationNumber;
	
	private String totalAmount;
	
	private String gstinNumber;
	
	private String quotationDate;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getProductRate() {
		return productRate;
	}

	public void setProductRate(String productRate) {
		this.productRate = productRate;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getProductGstPer() {
		return productGstPer;
	}

	public void setProductGstPer(String productGstPer) {
		this.productGstPer = productGstPer;
	}

	public String getTotalPriceWithOutGst() {
		return totalPriceWithOutGst;
	}

	public void setTotalPriceWithOutGst(String totalPriceWithOutGst) {
		this.totalPriceWithOutGst = totalPriceWithOutGst;
	}

	public String getProductTaxAmount() {
		return productTaxAmount;
	}

	public void setProductTaxAmount(String productTaxAmount) {
		this.productTaxAmount = productTaxAmount;
	}

	public String getsGst() {
		return sGst;
	}

	public void setsGst(String sGst) {
		this.sGst = sGst;
	}

	public String getcGst() {
		return cGst;
	}

	public void setcGst(String cGst) {
		this.cGst = cGst;
	}

	public String getQuotationNumber() {
		return quotationNumber;
	}

	public void setQuotationNumber(String quotationNumber) {
		this.quotationNumber = quotationNumber;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getGstinNumber() {
		return gstinNumber;
	}

	public void setGstinNumber(String gstinNumber) {
		this.gstinNumber = gstinNumber;
	}

	public String getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	
}
