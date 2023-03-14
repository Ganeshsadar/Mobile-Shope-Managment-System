package com.averta.inventory.bo;

import java.math.BigDecimal;

import com.averta.inventory.entity.ProcurementProducts;
import com.averta.inventory.entity.Product;

public interface ProductPage {

	ProcurementProducts setProduct(ProcurementProducts procurementProducts);

	Product setproduct(Product product);

	String getProductName();

	Long getProductId();

	String getBatchName();

	Long getProductGst();

	String getColor();

	String getBrand();

	String getGrade();

	String getType();

	Long getAveQua();

	String getBase();

	String getVolume();

	Boolean getStatus();
	
	String getUnit();
	
	BigDecimal getProductRate();

}
