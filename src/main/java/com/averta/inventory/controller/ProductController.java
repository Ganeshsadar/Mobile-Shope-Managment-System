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
import org.springframework.web.multipart.MultipartFile;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Product;
import com.averta.inventory.service.ProductService;
import com.averta.inventory.utility.ErrorConstants;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@CrossOrigin
	@PostMapping
	private ResponseEntity<Response> addproduct(@Valid @RequestBody Product product) throws Exception {
		productService.addProduct(product);
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Product Added Successfully");
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);
	}

	@CrossOrigin
	@GetMapping
	private ResponseEntity<Response> getProducts(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "product_id") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy) throws Exception {
		return new ResponseEntity<Response>(
				productService.getProducts(pageNum, numPerPage, sortBy, sortOrder, searchBy), HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping("/activeList")
	private ResponseEntity<Response> getProducts() throws Exception {
		Response response = new Response();
		response.setResult(productService.productList());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("products");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@GetMapping("/{productId}")
	private ResponseEntity<Response> getProductById(@PathVariable Long productId) throws Exception {
		Response response = new Response();
		response.setResult(productService.getProductById(productId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Product details");
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@PutMapping(value = "/{productId}")
	private ResponseEntity<Response> updateProduct(@Valid @RequestBody Product product, @PathVariable Long productId)
			throws Exception {
		Response response = new Response();
		response.setResult(productService.updateProduct(product, productId));
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Product updated successfully");
		return ResponseEntity.ok(response);

	}

	@CrossOrigin
	@PutMapping(value = "change/{productId}")
	private ResponseEntity<Response> setStatus(@PathVariable Long productId) throws Exception {
		Response response = new Response();
		productService.setProductStatus(productId);
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Status changed");
		return ResponseEntity.ok(response);

	}

	@CrossOrigin
	@PostMapping("/import-products")
	public ResponseEntity<Response> importProducts(@RequestParam("importFile") MultipartFile importFile)
			throws Exception {
		Response response = new Response();
		productService.importProducts(importFile);
		response.setStatus("200");
		response.setMessage("Product successfully imported");
		return ResponseEntity.ok(response);
	}
}
