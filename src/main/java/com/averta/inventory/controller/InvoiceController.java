package com.averta.inventory.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.averta.inventory.bo.CustomerReport;
import com.averta.inventory.bo.ReportOne;
import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Invoice;
import com.averta.inventory.service.InvoiceServic;
import com.averta.inventory.utility.ErrorConstants;
import com.averta.inventory.utility.ExcelGenerator;
import com.averta.inventory.utility.ExcelGenerator1;

@RestController
@RequestMapping("/v1/invoice")
@CrossOrigin()
public class InvoiceController {

	@Autowired
	private InvoiceServic invoiceServic;

	@CrossOrigin
	@PostMapping
	public ResponseEntity<Response> addInvoice(@Valid @RequestBody Invoice invoice) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice created successfully");
		invoiceServic.addInvoice(invoice);
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);

	}

	@CrossOrigin
	@PutMapping(value = "/{invoiceId}")
	private ResponseEntity<Response> updateInvoice(@Valid @RequestBody Invoice invoice, @PathVariable Long invoiceId)
			throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.updateInvoice(invoice, invoiceId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice Updated SucessFully");

		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@GetMapping
	private ResponseEntity<Response> listInvoice(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "invoiceId") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy) throws Exception {
		return ResponseEntity.ok(invoiceServic.listInvoice(pageNum, numPerPage, sortBy, sortOrder, searchBy));

	}

	@GetMapping("/{invoiceId}")
	private ResponseEntity<Response> getInvoiceById(@PathVariable Long invoiceId) throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.getInvoiceById(invoiceId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice Details");
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/pdf/{invoiceId}")
	public ResponseEntity<Response> generateInvoicePdf(@PathVariable Long invoiceId) throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.generateInvoicePdf(invoiceId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice Details");
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/pdf/print/{invoiceId}")
	public ResponseEntity<Response> generateInvoicePrint(@PathVariable Long invoiceId) throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.generateInvoicePrint(invoiceId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice Details");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@PostMapping("/import-invoice")
	public ResponseEntity<Response> importInvoice(@RequestParam("importFile") MultipartFile importFile)
			throws Exception {
		Response response = new Response();
		invoiceServic.importInvoices(importFile);
		response.setStatus("200");
		response.setMessage("Invoice successfully imported");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@DeleteMapping("/{invoiceId}")
	public ResponseEntity<Response> deleteInvoiceById(@PathVariable Long invoiceId) throws Exception {
		invoiceServic.DeletByInvoiceId(invoiceId);
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice deleted");
		return ResponseEntity.ok(response);

	}

	@GetMapping("/monthty-Revenue")
	private ResponseEntity<Response> getCustomersOfMonth() throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.monthRevenue());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Revenue of this month");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/top-products")
	private ResponseEntity<Response> getTopProducts() throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.topProducts());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Top 5 products");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products-sold")
	private ResponseEntity<Response> getTotalpro() throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.totalProducts());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("products");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/net-Rev")
	private ResponseEntity<Response> netVsRev() throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.netVsRev());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("111");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthly-buying")
	private ResponseEntity<Response> monthlyBuying() throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.monthlyBuying());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("report");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthly-selling")
	private ResponseEntity<Response> monthlySelling() throws Exception {
		Response response = new Response();
		response.setResult(invoiceServic.monthlySells());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("report");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/excel-sells")
	public void exportIntoExcelFile(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=monthly-selling" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<CustomerReport> listOfStudents = invoiceServic.monthlySells();

		ExcelGenerator generator = new ExcelGenerator(listOfStudents);

		generator.generateExcelFile(response);
	}

	@GetMapping("/excel-buying")
	public void exportIntoExcelFile2(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=monthly-buying" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<ReportOne> listOfStudents = invoiceServic.monthlyBuying();

		ExcelGenerator1 generator = new ExcelGenerator1(listOfStudents);

		generator.generateExcelFile(response);
	}
}
