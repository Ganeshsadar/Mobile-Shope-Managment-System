package com.averta.inventory.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Procurement;
import com.averta.inventory.service.ProcurementService;
import com.averta.inventory.utility.ErrorConstants;

@RestController
@RequestMapping("/v1/procurement")
public class ProcurementController {

	@Autowired
	ProcurementService procurementService;

	@CrossOrigin
	@PostMapping
	public ResponseEntity<Response> add(@Valid @RequestBody Procurement procurement) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Procurement added successfully");
		procurementService.addProcurement(procurement);
		return new ResponseEntity<Response>(response, HttpStatus.CREATED);

	}

	@CrossOrigin
	@GetMapping("/{procurementId}")
	private ResponseEntity<Response> getProcurement(@PathVariable Long procurementId) throws Exception {
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Procurement");
		response.setResult(procurementService.getProcurementById(procurementId));
		return ResponseEntity.ok(response);

	}

	@CrossOrigin
	@GetMapping
	private ResponseEntity<Response> ProcurementList(
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "numPerPage", required = false, defaultValue = "10") Integer numPerPage,
			@RequestParam(value = "sortBy", required = false, defaultValue = "procurementId") String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
			@RequestParam(value = "searchBy", required = false) String searchBy) throws Exception {
		return new ResponseEntity<Response>(
				procurementService.getProcurements(pageNum, numPerPage, sortBy, sortOrder, searchBy), HttpStatus.OK);
	}

	@CrossOrigin
	@DeleteMapping("/{procurementId}")
	public ResponseEntity<Response> deleteProcurement(@PathVariable Long procurementId) throws Exception {
		procurementService.deleteByProcurementId(procurementId);
		Response response = new Response();
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Procurement deleted");
		return ResponseEntity.ok(response);

	}

	@CrossOrigin
	@GetMapping("/net-profit")
	private ResponseEntity<Response> getCustomersOfMonth() throws Exception {
		Response response = new Response();
		response.setResult(procurementService.monthNetProfit());
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Profit in this month");
		return ResponseEntity.ok(response);
	}

}
