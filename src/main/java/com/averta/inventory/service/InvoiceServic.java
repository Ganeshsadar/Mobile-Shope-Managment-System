package com.averta.inventory.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.averta.inventory.bo.CustomerReport;
import com.averta.inventory.bo.ReportOne;
import com.averta.inventory.bo.Response;
import com.averta.inventory.bo.TopProductBo;
import com.averta.inventory.entity.Invoice;

public interface InvoiceServic {

	public void addInvoice(Invoice invoice) throws Exception;

	public Invoice updateInvoice(Invoice invoice, Long invoiceId) throws Exception;

	public Invoice getInvoiceById(Long invoiceId) throws Exception;

	public Response listInvoice(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception;

	public String generateInvoicePdf(Long invoiceId) throws Exception;

	public String generateInvoicePrint(Long invoiceId) throws Exception;

	void importInvoices(MultipartFile importFile) throws Exception;

	public void DeletByInvoiceId(Long invoiceId) throws Exception;

	public Double monthRevenue() throws Exception;

	public List<TopProductBo> topProducts() throws Exception;

	public List<ReportOne> monthlyBuying() throws Exception;

	public List<CustomerReport> monthlySells() throws Exception;

	public Double totalProducts() throws Exception;

	public Double netVsRev() throws Exception;

}
