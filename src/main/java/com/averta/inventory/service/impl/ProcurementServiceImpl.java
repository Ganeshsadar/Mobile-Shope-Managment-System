package com.averta.inventory.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Procurement;
import com.averta.inventory.entity.ProcurementProducts;
import com.averta.inventory.entity.Product;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.ProcurementProductRepository;
import com.averta.inventory.repository.ProcurementRepository;
import com.averta.inventory.repository.ProductRepository;
import com.averta.inventory.service.ProcurementService;
import com.averta.inventory.service.ProductService;
import com.averta.inventory.service.SellerService;
import com.averta.inventory.utility.ErrorConstants;

@Service
public class ProcurementServiceImpl implements ProcurementService {

	@Autowired
	private ProcurementRepository procurementRepository;

	@Autowired
	private ProcurementProductRepository procurementProductRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SellerService sellerService;

	@Override
	public void addProcurement(Procurement procurement) throws Exception {
		procurement.setSeller(sellerService.getSellerById(procurement.getSellerId()));
		List<ProcurementProducts> procurementProducts = procurement.getProcurementProducts();
		for (ProcurementProducts procurementProduct : procurementProducts) {

			procurementProduct.setProduct(productService.getProductById(procurementProduct.getProductId()));
			procurementProduct.setProductPrice(procurementProduct.getProductPrice());
			procurementProduct.setProductGstPer(BigDecimal.valueOf(procurementProduct.getProduct().getProductGst()));
			procurementProduct.setProcurement(procurement);

		}
		Double totalprocurementAmount = 0.0;
		Double discount = 0.0;
		Double totalamount = 0.0;
		Double totaltax = 0.0;
		Double productTaxAmount = 0.0;
		for (ProcurementProducts procurementProduct : procurementProducts) {
			procurementProduct.setProductPrice(procurementProduct.getProductPrice());
			totalprocurementAmount += procurementProduct.getProductPrice().doubleValue()
					* procurementProduct.getQuantity();
			discount = procurement.getDiscount().doubleValue();
			totaltax += procurementProduct.getProductPrice().doubleValue() * procurementProduct.getQuantity()
					* procurementProduct.getProduct().getProductGst() / 100;

			procurement.setTotalAmount(BigDecimal.valueOf(totalprocurementAmount));

			totalamount = totalprocurementAmount - discount;

			procurement.setDiscount(BigDecimal.valueOf(discount));

			procurement.setFinalAmount(BigDecimal.valueOf(totalamount + totaltax));

			procurement.setTotalTax(BigDecimal.valueOf(totaltax));

			procurementProduct.setProductGstPer(BigDecimal.valueOf(procurementProduct.getProduct().getProductGst()));

			procurement.setcGst(BigDecimal.valueOf(totaltax / 2));

			procurement.setsGst(BigDecimal.valueOf(totaltax / 2));

			productTaxAmount = procurementProduct.getProductPrice().doubleValue()
					* procurementProduct.getQuantity().doubleValue() * procurementProduct.getProduct().getProductGst()
					/ 100;

			procurementProduct.setProductTaxAmount(BigDecimal
					.valueOf(procurementProduct.getQuantity() * procurementProduct.getProductPrice().doubleValue()
							+ productTaxAmount));
			Product product = procurementProduct.getProduct();

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate now = LocalDate.now();
			procurementProduct.setBatchName(dtf.format(now));
//			procurementProduct.setQuantity(product.getQuantity() + procurementProduct.getQuantity());
			procurementProduct.setAvailableQuantity(procurementProduct.getQuantity() + procurementProduct.getQuantity());
			productRepository.save(product);
			procurementProduct.setAvailableQuantity(procurementProduct.getQuantity());
		}
		procurementRepository.save(procurement);
		for (ProcurementProducts procurementProduct : procurementProducts) {

			/*
			 * procurement.setGstinNumber(procurement.getSeller().getGstnNumber());
			 * ProductBatch productBatch = new ProductBatch();
			 * productBatch.setProduct(procurementProduct.getProduct());
			 * productBatch.setQuantity(procurementProduct.getQuantity());
			 * productBatch.setProcurement(procurement);
			 * productBatch.setProductPrice(procurementProduct.getProductPrice());
			 * DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			 * LocalDateTime now = LocalDateTime.now();
			 * productBatch.setBatchName(dtf.format(now));
			 * productBatchRepository.save(productBatch);
			 */

			procurementProductRepository.save(procurementProduct);
			procurementProduct.getProcurementProductsId();
		}
	}

	@Override
	public Procurement updateProcurement(Procurement procurement, Long procurementId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Procurement getProcurementById(Long procurementId) throws Exception {
		Procurement procurement = procurementRepository.findById(procurementId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Procurement not found"));
		List<ProcurementProducts> procurementProducts = procurementProductRepository.findByProcurement(procurement);
		for (ProcurementProducts procurementProduct : procurementProducts) {
			Double taxOnPro = procurementProduct.getProduct().getProductGst().doubleValue()
					* procurementProduct.getProductPrice().doubleValue() / 100;
			procurementProduct.setProductGstAmount(BigDecimal.valueOf(taxOnPro));
		}
		procurement.setProcurementProducts(procurementProducts);
		return procurement;
	}

	@Override
	public Response getProcurements(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder,
			String searchBy) throws Exception {
		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);
		Page<Procurement> page = null;
		if (searchBy != null && !searchBy.isEmpty()) {
			page = procurementRepository.searchProcurement(searchBy, pageable);

		} else {
			page = procurementRepository.findAll(pageable);
		}
		response.setStatus(ErrorConstants.SUCCESS);
		response.setListCount(page.getTotalElements());
		response.setMessage("Procurement Lise");
		response.setResult(page.getContent());
		return response;
	}

	@Override
	public void deleteByProcurementId(Long procurementId) throws Exception {
		Procurement procurement = procurementRepository.findById(procurementId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Procurment not fount"));
		List<ProcurementProducts> procurementProducts = procurementProductRepository.findByProcurement(procurement);
		for (ProcurementProducts procurementProduct : procurementProducts) {
			procurementProductRepository.delete(procurementProduct);
			
		}
		procurementRepository.delete(procurement);
	}

	@Override
	public Double monthNetProfit() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		System.out.println(startDate + " " + endDate);
		System.out.println(procurementRepository.netProfit(startDate, endDate));
		return procurementRepository.netProfit(startDate, endDate);
	}

	@Override
	public ProcurementProducts getProcurementProductById(Long procurementProductsId) throws Exception {

		ProcurementProducts procurementProducts = procurementProductRepository.findById(procurementProductsId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Procurement product not found"));
		return procurementProducts;
	}
}
