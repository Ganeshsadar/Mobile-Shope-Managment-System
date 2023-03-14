package com.averta.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.averta.inventory.entity.Customer;
import com.averta.inventory.entity.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

	public Quotation findByQuotationId(Long quotationId) throws Exception;

	public Page<Quotation> findByQuotationId(String searchBy, Pageable pageable) throws Exception;

	public Page<Quotation> findByCustomer(Customer customer, Pageable pageable) throws Exception;

	@Query("SELECT i FROM Quotation i, Customer c WHERE i.customer.customerId = c.customerId"
			+ " AND (c.customerName LIKE %?1% OR c.mobileNumber LIKE %?1% OR i.quotationNumber LIKE %?1%)")
	public Page<Quotation> searchQuotation(String searchBy, Pageable pageable);

}
