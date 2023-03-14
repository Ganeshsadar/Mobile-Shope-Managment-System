package com.averta.inventory.bo;

import com.averta.inventory.entity.ProcurementProducts;

public interface ReportOne {

	ProcurementProducts setProduct(ProcurementProducts procurementProducts);

	Long getproductId();

	String getavailableQuantity();

	String getproductname();

	String getorderedQuantity();

	String getsellername();

	String getproductRate();
}
