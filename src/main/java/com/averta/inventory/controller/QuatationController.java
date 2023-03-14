package com.averta.inventory.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Quotation;
import com.averta.inventory.service.QuotationService;
import com.averta.inventory.utility.ErrorConstants;

@RestController
@RequestMapping("/v1/quotation")
@CrossOrigin()
public class QuatationController {

	@Autowired
	private QuotationService quotationService;

	@CrossOrigin
	@PostMapping
	public ResponseEntity<Response> addQuotation(@Valid @RequestBody Quotation quotation) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Quotation saved");
		quotationService.addQuotation(quotation);
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);
	}
	@CrossOrigin
    @PutMapping(value = "/{quotationId}")
    private ResponseEntity<Response> updateQuotation(@Valid @RequestBody Quotation quotation, @PathVariable Long quotationId)
            throws Exception {
        Response response = new Response();
        response.setResult(quotationService.updateQuotation(quotation, quotationId));
        response.setStatus(ErrorConstants.SUCCESS);
        response.setMessage("Quotation Updated SucessFully");

        return ResponseEntity.ok(response);
    }
    

	@CrossOrigin
	@GetMapping
	private ResponseEntity<Response> getQuotations(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "quotationId") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy,
			@RequestParam(value = "customerId", required = false) Long customerId) throws Exception {

		return ResponseEntity
				.ok(quotationService.listQuotation(pageNum, numPerPage, sortBy, sortOrder, searchBy, customerId));
	}

	
	@GetMapping("/{quotationId}")
	private ResponseEntity<Response> getQuotationById(@PathVariable Long quotationId)throws Exception{
		Response response = new Response();
		response.setResult(quotationService.getQuotationById(quotationId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Quotation Details");
		return ResponseEntity.ok(response);		
	}
	@GetMapping(value ="/pdf/{quotationId}")
	private ResponseEntity<Response> generateQuotationPdf(@PathVariable Long quotationId)throws Exception{
		Response response = new Response();
		response.setResult(quotationService.generateQuotationPdf(quotationId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Quotation Details");
		return ResponseEntity.ok(response);		
	}
	
	 @GetMapping(value = "/pdf/print/{quotationId}")
	    public ResponseEntity<Response> generateQuotationPrint(@PathVariable Long quotationId) throws Exception {
	        Response response = new Response();
	        response.setResult(quotationService.generateQuotationPrint(quotationId));
	        response.setStatus(ErrorConstants.SUCCESS);
	        response.setMessage("Quotation Details");
	        return ResponseEntity.ok(response);
	    }

	    
	
}
