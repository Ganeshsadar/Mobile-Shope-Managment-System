package com.averta.inventory.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.averta.inventory.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public Customer findByCustomerName(String customerName) throws Exception;

	public Customer findByMobileNumber(String mobileNumber) throws Exception;

	public Customer findByEmail(String email) throws Exception;

	public Page<Customer> findByCustomerNameContainingOrMobileNumberContaining(String customerName, String mobileNumber,
			Pageable pageable) throws Exception;

	@Query(value = "SELECT count(*) FROM customer i WHERE DATE(i.created_at) >=:startDate AND DATE(i.created_at) <=:endDate", nativeQuery = true)
	public long getCountOfCustomers(@Param("startDate") Date startDate, @Param("endDate") Date endDate)
			throws Exception;

}
