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
import com.averta.inventory.entity.Customer;

import com.averta.inventory.service.CustomerService;
import com.averta.inventory.utility.ErrorConstants;

@RestController
@RequestMapping("/v1/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@CrossOrigin
	@PostMapping
	private ResponseEntity<Response> addcustomer(@Valid @RequestBody Customer customer) throws Exception {
		customerService.add(customer);
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Customer Added Successfully");
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);
	}

	@CrossOrigin
	@GetMapping
	public ResponseEntity<Response> getAll(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "customerId") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy) throws Exception {
		return new ResponseEntity<Response>(
				customerService.getCustomers(pageNum, numPerPage, sortBy, sortOrder, searchBy), HttpStatus.OK);
	}

	@CrossOrigin
	@PutMapping("/{customerId}")
	private ResponseEntity<Response> updatecustomer(@Valid @RequestBody Customer customer,
			@PathVariable Long customerId) throws Exception {
		Response response = new Response();
		response.setResult(customerService.update(customerId, customer));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Profile updated successfully");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@GetMapping("/{customerId}")
	private ResponseEntity<Response> getInvoiceById(@PathVariable Long customerId) throws Exception {
		Response response = new Response();
		response.setResult(customerService.getById(customerId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice Details");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@GetMapping("/customers")
	private ResponseEntity<Response> getCustomersOfMonth() throws Exception {
		Response response = new Response();
		response.setResult(customerService.customerByMonth());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Customers");
		return ResponseEntity.ok(response);
	}

}
