package com.averta.inventory.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Customer;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.CustomerRepository;
import com.averta.inventory.service.CustomerService;
import com.averta.inventory.utility.ErrorConstants;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public void add(Customer customer) throws Exception {
		Customer checkNumber = customerRepository.findByMobileNumber(customer.getMobileNumber());
		if (checkNumber != null) {
			throw new InventoryException(ErrorConstants.INVALID,
					"Mobile number " + customer.getMobileNumber() + " already exist");
		}

		if (null != customer.getEmail() && !customer.getEmail().isEmpty()) {
			Customer checkEmail = customerRepository.findByEmail(customer.getEmail());
			if (checkEmail != null) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Email Id" + customer.getEmail() + "already exist");
			}
		}
		customerRepository.save(customer);
	}

	@Override
	public Customer update(Long customerId, Customer customer) throws Exception {
		Customer checkcustomer = getById(customerId);

		if (!customer.getMobileNumber().toString().equalsIgnoreCase(checkcustomer.getMobileNumber().toString())) {
			Customer checkMobile = customerRepository.findByMobileNumber(customer.getMobileNumber());
			if (checkMobile != null) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Mobile number " + customer.getMobileNumber() + " already exist");
			}
		}

		if (!customer.getEmail().toString().equalsIgnoreCase(checkcustomer.getEmail().toString())) {
			Customer checkEmail = customerRepository.findByEmail(customer.getEmail());
			if (checkEmail != null) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Email id " + customer.getEmail() + " already exist");
			}
		}

		checkcustomer.setCustomerName(customer.getCustomerName());

		checkcustomer.setMobileNumber(customer.getMobileNumber());
		checkcustomer.setEmail(customer.getEmail());
		checkcustomer.setAddressLineOne(customer.getAddressLineOne());
		checkcustomer.setAddressLineTwo(customer.getAddressLineTwo());
		checkcustomer.setArea(customer.getArea());
		checkcustomer.setPincode(customer.getPincode());
		return customerRepository.save(checkcustomer);
	}

	@Override
	public Customer getById(Long customerId) throws Exception {
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Customer Not Found"));
	}

	@Override
	public Response getCustomers(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception {
		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);

		Page<Customer> page = null;
		if (searchBy != null && !searchBy.isEmpty()) {
			page = customerRepository.findByCustomerNameContainingOrMobileNumberContaining(searchBy, searchBy,
					pageable);
		} else {
			page = customerRepository.findAll(pageable);
		}
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Customer List is ");
		response.setResult(page.getContent());
		response.setListCount(page.getTotalElements());
		return response;
	}

	@Override
	public Long customerByMonth() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		System.out.println(startDate + "" + endDate);
		return customerRepository.getCountOfCustomers(startDate, endDate);
	}

}
