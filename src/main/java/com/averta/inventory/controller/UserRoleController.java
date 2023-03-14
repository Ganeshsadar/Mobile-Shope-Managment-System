package com.averta.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.averta.inventory.bo.Response;
import com.averta.inventory.repository.UserRoleRepository;

@RestController
@RequestMapping(value = "/v1/user-role")
@CrossOrigin
public class UserRoleController {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@GetMapping
	public ResponseEntity<Response> getAllRoles() {
		Response response = new Response();
		response.setStatus(String.valueOf(HttpStatus.OK.value()));
		response.setMessage("Role dropdown");
		response.setResult(userRoleRepository.findByStatus("Active"));
		return ResponseEntity.ok(response);
	}

}
