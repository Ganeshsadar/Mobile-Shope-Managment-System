package com.averta.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Seller;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.SellerRepository;
import com.averta.inventory.service.SellerService;
import com.averta.inventory.utility.ErrorConstants;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;

	@Override
	public void add(Seller seller) throws Exception {

		Seller checkNumber = sellerRepository.findByMobileNumber(seller.getMobileNumber());
		if (checkNumber != null) {
			throw new InventoryException(ErrorConstants.INVALID,
					"Mobile number " + seller.getMobileNumber() + " already exist");
		}

		if (null != seller.getGstnNumber() && !seller.getGstnNumber().isEmpty()) {
			Seller checkGstn = sellerRepository.findByGstnNumber(seller.getGstnNumber());
			if (checkGstn != null) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Gstn number " + seller.getGstnNumber() + " already exist");
			}
		}
		if (null != seller.getEmail() && !seller.getEmail().isEmpty()) {
			Seller checkEmail = sellerRepository.findByEmail(seller.getEmail());
			if (checkEmail != null) {
				throw new InventoryException(ErrorConstants.INVALID, "Email Id" + seller.getEmail() + "already exist");
			}
		}
		sellerRepository.save(seller);
	}

	@Override
	public Seller update(Seller seller, Long sellerId) throws Exception {
		Seller checkSeller = getSellerById(sellerId);

		if (!seller.getMobileNumber().equalsIgnoreCase(checkSeller.getMobileNumber())) {
			Seller checkMobile = sellerRepository.findByMobileNumber(seller.getMobileNumber());
			if (checkMobile != null) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Mobile number " + seller.getMobileNumber() + " already exist");
			}
		}
		if (null != seller.getGstnNumber() && !seller.getGstnNumber().isEmpty()) {
			Seller checkGstn = sellerRepository.findByGstnNumberAndSellerIdNot(seller.getGstnNumber(),
					checkSeller.getSellerId());
			if (checkGstn != null && !checkGstn.toString().isEmpty()) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Gstn number " + seller.getGstnNumber() + " already exist");
			}
		}

		if (null != seller.getEmail() && !seller.getEmail().isEmpty()) {
			Seller checkMobile = sellerRepository.findByEmailAndSellerIdNot(seller.getEmail(),
					checkSeller.getSellerId());
			if (checkMobile != null  && !checkMobile.toString().isEmpty()) {
				throw new InventoryException(ErrorConstants.INVALID, "Email" + seller.getEmail() + " already exist");
			}
		}

		checkSeller.setName(seller.getName());
		checkSeller.setAddress(seller.getAddress());
		checkSeller.setCompanyName(seller.getCompanyName());
		checkSeller.setEmail(seller.getEmail());
		checkSeller.setGstnNumber(seller.getGstnNumber());
		checkSeller.setMobileNumber(seller.getMobileNumber());
		checkSeller.setState(seller.getState());

		return sellerRepository.save(checkSeller);
	}

	@Override
	public Seller getSellerById(Long sellerId) throws Exception {
		return sellerRepository.findById(sellerId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Seller Not Found"));
	}

	@Override
	public Response listSellers(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception {
		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);
		Page<Seller> page = null;
		if (searchBy != null && !searchBy.isEmpty()) {
			page = sellerRepository
					.findByNameContainingOrMobileNumberContainingOrEmailContainingOrStateContainingOrCompanyNameContainingOrGstnNumberContainingOrAddressContaining(
							searchBy, searchBy, searchBy, searchBy, searchBy, searchBy, searchBy, pageable);
		} else {
			page = sellerRepository.findAll(pageable);
		}
		response.setStatus(ErrorConstants.SUCCESS);
		response.setListCount(page.getTotalElements());
		response.setMessage("Seller List");
		response.setResult(page.getContent());
		return response;
	}

}
