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
import com.averta.inventory.entity.Seller;
import com.averta.inventory.service.SellerService;
import com.averta.inventory.utility.ErrorConstants;

@RestController
@RequestMapping("/v1/seller")
public class SellerController {

	@Autowired
	SellerService sellerService;

	@CrossOrigin
	@PostMapping
	public ResponseEntity<Response> add(@Valid @RequestBody Seller seller) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Seller Added Successfully");
		sellerService.add(seller);
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);
	}

	@CrossOrigin
	@GetMapping
	public ResponseEntity<Response> GetSellers(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "sellerId") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy) throws Exception {
		return new ResponseEntity<Response>(sellerService.listSellers(pageNum, numPerPage, sortBy, sortOrder, searchBy),
				HttpStatus.OK);
	}	

	@CrossOrigin
	@GetMapping("/{sellerId}")
	private ResponseEntity<Response> getSellerById(@PathVariable Long sellerId) throws Exception {
		Response response = new Response();
		response.setResult(sellerService.getSellerById(sellerId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Quotation Details");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@PutMapping("/{sellerId}")
	public ResponseEntity<Response> updateSeller(@Valid @RequestBody Seller seller, @PathVariable Long sellerId)
			throws Exception {
		Response response = new Response();
		response.setResult(sellerService.update(seller, sellerId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Seller updated successfully");
		return ResponseEntity.ok(response);

	}
}
