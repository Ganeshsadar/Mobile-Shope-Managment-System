package com.averta.inventory.service;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Customer;

public interface CustomerService {

	public void add(Customer customer) throws Exception;

	public Customer update(Long customerId, Customer customer) throws Exception;

	public Customer getById(Long customerId) throws Exception;

	public Response getCustomers(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception;

	public Long customerByMonth() throws Exception;

}
