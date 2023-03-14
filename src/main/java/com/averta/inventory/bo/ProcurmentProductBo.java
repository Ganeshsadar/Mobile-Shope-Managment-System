package com.averta.inventory.bo;

import java.math.BigDecimal;
import com.averta.inventory.entity.ProcurementProducts;
import com.averta.inventory.entity.Product;

public interface ProcurmentProductBo {

	ProcurementProducts setProduct(ProcurementProducts procurementProducts);

	Product setproduct(Product product);
	
	String getBatchName();
	
	Long getProductId();

	BigDecimal getAvailableQuantity();

	String getProductName();
	
	BigDecimal getproductPrice();
	
	Double getProductGst();
	
	Long getProcurementProductId();
}
