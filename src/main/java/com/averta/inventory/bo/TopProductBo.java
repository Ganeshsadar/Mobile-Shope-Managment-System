package com.averta.inventory.bo;

import com.averta.inventory.entity.Product;

public interface TopProductBo {

	 Long getProductId();

	 Product setProduct(Product product);

	 String getQuantity();

	 String getProductName();
}
