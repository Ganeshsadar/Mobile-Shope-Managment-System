package com.averta.inventory.service;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Procurement;
import com.averta.inventory.entity.ProcurementProducts;

public interface ProcurementService {

	public void addProcurement(Procurement procurement) throws Exception;

	public Procurement updateProcurement(Procurement procurement, Long procurementId) throws Exception;

	public Procurement getProcurementById(Long procurementId) throws Exception;

	public Response getProcurements(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder,
			String searchBy) throws Exception;

	public void deleteByProcurementId(Long procurementId) throws Exception;
	
	public Double monthNetProfit() throws Exception;
	
	public ProcurementProducts getProcurementProductById(Long procurementProductsId)throws Exception;
}
