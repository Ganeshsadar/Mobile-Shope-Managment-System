package com.averta.inventory.bo;

import com.averta.inventory.entity.ProcurementProducts;

public interface CustomerReport {

	ProcurementProducts setProduct(ProcurementProducts procurementProducts);

	Long getproductId();

	String getavailableQuantity();

	String getproductname();

	String getsoldProducts();

	String getcustomerName();

	String getproductRate();
}
