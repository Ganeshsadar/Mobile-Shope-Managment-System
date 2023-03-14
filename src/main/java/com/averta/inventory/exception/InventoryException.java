package com.averta.inventory.exception;

public class InventoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;

	private String message;

	private String uniqueNo;
	
	public InventoryException(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public InventoryException(String errorCode, String message, String uniqueNo) {
		super();
		this.errorCode = errorCode;
		this.message = message;
		this.uniqueNo = uniqueNo;
	}

	public String getUniqueNo() {
		return uniqueNo;
	}

	public void setUniqueNo(String uniqueNo) {
		this.uniqueNo = uniqueNo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
