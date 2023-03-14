package com.averta.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.averta.inventory.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {

	public Page<Seller> findByNameContainingOrMobileNumberContainingOrEmailContainingOrStateContainingOrCompanyNameContainingOrGstnNumberContainingOrAddressContaining(
			String name, String mobileNumber, String email, String state, String companyName, String gstnNumber,
			String address, Pageable pageable) throws Exception;

	public Seller findByName(String name) throws Exception;

	public Seller findByGstnNumber(String gstnNumber) throws Exception;

	public Seller findByMobileNumber(String mobileNumber) throws Exception;

	public Seller findByEmail(String email) throws Exception;

	public Seller findByGstnNumberAndSellerIdNot(String gstnNumber, Long sellerId) throws Exception;

	public Seller findByEmailAndSellerIdNot(String email, Long sellerId) throws Exception;

}