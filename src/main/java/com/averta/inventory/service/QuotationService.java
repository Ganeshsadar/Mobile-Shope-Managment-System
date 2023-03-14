package com.averta.inventory.service;


import org.springframework.web.multipart.MultipartFile;

import com.averta.inventory.bo.Response;

import com.averta.inventory.entity.Quotation;

public interface QuotationService {

	public void addQuotation(Quotation quotation) throws Exception;

	
	public Quotation getQuotationById(Long quotationId) throws Exception;


	public Response listQuotation(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy,Long customerId)
			throws Exception;
	

	public Object generateQuotationPdf(Long quotationId)throws Exception;

	public Object generateQuotationPrint(Long quotationId)throws Exception;;

	void importQuotations(MultipartFile importFile) throws Exception;

    public Quotation updateQuotation(Quotation quotation, Long quotationId) throws Exception;

	public void DeletByInvoiceId(Long quotationId) throws Exception;

	
}



