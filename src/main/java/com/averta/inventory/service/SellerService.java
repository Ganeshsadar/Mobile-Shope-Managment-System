package com.averta.inventory.service;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Seller;

public interface SellerService {

	public void add(Seller seller) throws Exception;

	public Seller update(Seller seller,Long sellerId) throws Exception;

	public Seller getSellerById(Long sellerId) throws Exception;
	
	public Response listSellers(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy) throws Exception;

}

