package com.averta.inventory.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.averta.inventory.bo.Response;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.utility.ErrorConstants;

import io.jsonwebtoken.SignatureException;

@RestControllerAdvice
public class InventoryExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Response> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		Response response = new Response();
		response.setStatus(ex.getErrorCode());
		response.setMessage(ex.getMessage());
		return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {

		Map<String, String> map = new HashMap<>();
		ex.getAllErrors().forEach(e -> {
			map.put(((FieldError) e).getField(), ((FieldError) e).getDefaultMessage());
		});
		Response response = new Response();
		response.setStatus(ErrorConstants.BAD_REQUEST);
		response.setMessage("Invalid Request");
		response.setResult(map);
		return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InventoryException.class)
	public ResponseEntity<Response> inventoryExceptionHandler(InventoryException ex) {
		Response response = new Response();
		response.setStatus(ex.getErrorCode());
		response.setMessage(ex.getMessage());
		response.setResult(ex.getUniqueNo());
		return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<Response> signatureExceptionHandler(SignatureException ex) {
		Response response = new Response();
		response.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
		response.setMessage("Token expired or wrong token send");
		return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
	}
}
