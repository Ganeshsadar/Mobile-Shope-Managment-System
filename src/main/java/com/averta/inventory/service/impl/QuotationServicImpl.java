package com.averta.inventory.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.averta.inventory.bo.QuotationProductBo;
import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Quotation;
import com.averta.inventory.entity.QuotationProduct;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.ProductRepository;
import com.averta.inventory.repository.QuotationProductRepository;
import com.averta.inventory.repository.QuotationRepository;
import com.averta.inventory.service.CustomerService;
import com.averta.inventory.service.ProductService;
import com.averta.inventory.service.QuotationService;
import com.averta.inventory.utility.ErrorConstants;

@Service
public class QuotationServicImpl implements QuotationService {

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private QuotationProductRepository quotationProductRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private Environment environment;

	private static final String UTF_8 = "UTF-8";

	@Override
	public void addQuotation(Quotation quotation) throws Exception {
		quotation.setCustomer(customerService.getById(quotation.getCustomerId()));

		List<QuotationProduct> quotationProducts = quotation.getQuotationProducts();
		for (QuotationProduct quotationProduct : quotationProducts) {
			quotationProduct.setProduct(productService.getProductById(quotationProduct.getProductId()));
			quotationProduct.setProductPrice(quotationProduct.getProductPrice());
			quotationProduct.setProductGstPer(BigDecimal.valueOf(quotationProduct.getProduct().getProductGst()));
			quotationProduct.setQuotation(quotation);

		}
		Double totalQuatationAmount = 0.0;
		Double discount = 0.0;
		Double totalamount = 0.0;
		Double totaltax = 0.0;
		Double productTaxAmount = 0.0;

		for (QuotationProduct quotationProduct : quotationProducts) {
			quotationProduct.setProductPrice(quotationProduct.getProductPrice());
			totalQuatationAmount += quotationProduct.getProductPrice().doubleValue()
					* quotationProduct.getQuantity();
			discount = quotation.getDiscount().doubleValue();

			totaltax += quotationProduct.getProductPrice().doubleValue() * quotationProduct.getQuantity()
					* quotationProduct.getProduct().getProductGst() / 100;

			System.out.println("total" + totaltax);

			quotation.setTotalAmount(BigDecimal.valueOf(totalQuatationAmount));

			totalamount = totalQuatationAmount - discount;

			quotation.setDiscount(BigDecimal.valueOf(discount));

			quotation.setFinalAmount(BigDecimal.valueOf(totalamount + totaltax));

			quotation.setTotalTax(BigDecimal.valueOf(totaltax));

			quotationProduct.setProductGstPer(BigDecimal.valueOf(quotationProduct.getProduct().getProductGst()));

			quotation.setcGst(BigDecimal.valueOf(totaltax / 2));

			quotation.setsGst(BigDecimal.valueOf(totaltax / 2));

			productTaxAmount = quotationProduct.getProductPrice().doubleValue()
					* quotationProduct.getQuantity().doubleValue() * quotationProduct.getProduct().getProductGst()
					/ 100;
			quotationProduct.setProductTaxAmount(BigDecimal.valueOf(quotationProduct.getQuantity()*quotationProduct.getProductPrice().doubleValue()+ productTaxAmount ));
			quotation.setGstinNumber("27BCXPM3444A1ZH");

			quotationProduct.setProductTaxAmount(
					BigDecimal.valueOf(quotationProduct.getQuantity() * quotationProduct.getProductPrice().doubleValue()
							+ productTaxAmount));
			quotationProduct.setProductTaxAmount(
					BigDecimal.valueOf(quotationProduct.getQuantity() * quotationProduct.getProductPrice().doubleValue()
							+ productTaxAmount));

		}

		quotationRepository.save(quotation);

		for (QuotationProduct quotationProduct : quotationProducts) {
			quotationProductRepository.save(quotationProduct);
			quotationProduct.getQuotationProductId();
		}
		quotation.setQuotationNumber(quotationNumber(quotation.getQuotationId()));
		quotationRepository.save(quotation);
	}

	private String quotationNumber(Long quotationId) {
		String quotationNum = "";
		Calendar calendar = Calendar.getInstance();
		String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
		String nextYear = String.valueOf(calendar.get(Calendar.YEAR) + 1);
		String monthString = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String id = "";
		if (quotationId < 1000) {
			if (String.valueOf(quotationId).length() == 1) {
				id += "000" + quotationId;
			} else if (String.valueOf(quotationId).length() == 2) {
				id += "00" + quotationId;
			} else if (String.valueOf(quotationId).length() == 3) {
				id += "0" + quotationId;
			}
		}
		quotationNum += id + "/" + currentYear.substring(2) + "-" + nextYear.substring(2) + "/" + monthString + "/"
				+ "SAJ";
		return quotationNum;
	}

	@Override
	public Quotation updateQuotation(Quotation quotation, Long quotationId) throws Exception {
		Quotation checkquotation = getQuotationById(quotationId);
		checkquotation.setDiscount(quotation.getDiscount());
		return quotationRepository.save(checkquotation);
	}

	@Override
	public Response listQuotation(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy,
			Long customerId) throws Exception {

		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);

		Page<Quotation> page = null;
		if (searchBy != null && !searchBy.isEmpty()) {
			page = quotationRepository.searchQuotation(searchBy,pageable);
		} else {
			page = quotationRepository.findAll(pageable);
		}
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Quotations");
		response.setResult(page.getContent());
		response.setListCount(page.getTotalElements());
		return response;
	}

	@Override
	public Quotation getQuotationById(Long quotationId) throws Exception {
		Quotation quotation = quotationRepository.findById(quotationId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Quotation not found"));
		List<QuotationProduct> quotationProducts = quotationProductRepository.findByQuotation(quotation);
		for (QuotationProduct quotationProduct : quotationProducts) {
			Double TaxOnProduct = quotationProduct.getProduct().getProductGst().doubleValue()
					* quotationProduct.getProductPrice().doubleValue() / 100;
			quotationProduct.setProductGstAmount(BigDecimal.valueOf(TaxOnProduct));
		}

		quotation.setQuotationProducts(quotationProducts);
		return quotation;
	}

	@Override
	public void DeletByInvoiceId(Long quotationId) throws Exception {
		Quotation quotation = quotationRepository.findById(quotationId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "quotation not found"));
		List<QuotationProduct> quotationProducts = quotationProductRepository.findByQuotation(quotation);
		for (QuotationProduct quotationProduct : quotationProducts) {
			quotationProductRepository.delete(quotationProduct);
//			Product product = quotationProduct.getProduct();
//			product.setQuantity(product.getQuantity() + quotationProduct.getQuantity());
//			productRepository.save(product);
		}
		quotationRepository.delete(quotation);
	}

	@Override
	public String generateQuotationPdf(Long quotationId) throws Exception {
		Quotation quotation = quotationRepository.findById(quotationId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Quotation not found"));
		List<QuotationProduct> quotationProducts = quotationProductRepository.findByQuotation(quotation);

		List<QuotationProductBo> productList = new ArrayList<QuotationProductBo>();
		Double finalAmount = 0.0;

		QuotationProductBo quotationProductBo = null;
		for (QuotationProduct quotationProduct : quotationProducts) {
			quotationProductBo = new QuotationProductBo();
			quotationProductBo.setProductName(quotationProduct.getProduct().getProductName());
			quotationProductBo.setProductRate(quotationProduct.getProductPrice().toString());
			quotationProductBo.setQuantity(quotationProduct.getQuantity().toString());
			quotationProductBo.setProductGstPer(quotationProduct.getProductGstPer().toString());
			quotationProductBo.setProductTaxAmount(quotationProduct.getProductTaxAmount().toString());
			Double totalPriceWithOutGst = quotationProduct.getProductPrice().doubleValue()
					* quotationProduct.getQuantity().doubleValue();
			quotationProductBo.setTotalPriceWithOutGst(totalPriceWithOutGst.toString());
			finalAmount += totalPriceWithOutGst;
			productList.add(quotationProductBo);
		}

		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding(UTF_8);
		templateResolver.setOrder(0);
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Context context = new Context();
		context.setVariable("customerName", quotation.getCustomer().getCustomerName());
		context.setVariable("customerAddress",
				quotation.getCustomer().getAddressLineOne() + " " + quotation.getCustomer().getAddressLineTwo());
		context.setVariable("customerId", quotation.getCustomer().getCustomerId());
		context.setVariable("productList", productList);
		context.setVariable("finalAmount", finalAmount);
		context.setVariable("gstTax", quotation.getTotalTax());
		context.setVariable("discount", quotation.getDiscount());
		context.setVariable("grandTotal", quotation.getFinalAmount());
		context.setVariable("cGst", quotation.getcGst());
		context.setVariable("sGst", quotation.getsGst());
		context.setVariable("quotationNumber", quotation.getQuotationNumber());
		context.setVariable("totalAmount", quotation.getTotalAmount());
		context.setVariable("gstinNumber", quotation.getGstinNumber());
		context.setVariable("gstinNumber", quotation.getGstinNumber());
		context.setVariable("quotationDate", dateFormat.format(quotation.getQuotationDate()));
		String message = templateEngine.process("quotation", context);

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(message);
		renderer.setScaleToFit(true);
		renderer.layout();

		String fileName = "Quotation_" + quotation.getCustomer().getCustomerName() + ".pdf";
		String path = environment.getRequiredProperty("file.report.path");

		System.out.println(path + fileName);

		File pdfFile = new File(path + fileName);
		pdfFile.createNewFile();
		OutputStream outputStream = new FileOutputStream(pdfFile);
		renderer.createPDF(outputStream);
		outputStream.close();
		return fileName;
	}

	@Override
	public String generateQuotationPrint(Long quotationId) throws Exception {
		Quotation quotation = quotationRepository.findById(quotationId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Quotation not found"));
		List<QuotationProduct> quotationProducts = quotationProductRepository.findByQuotation(quotation);

		List<QuotationProductBo> productList = new ArrayList<QuotationProductBo>();
		Double finalAmount = 0.0;

		QuotationProductBo quotationProductBo = null;
		for (QuotationProduct quotationProduct : quotationProducts) {
			quotationProductBo = new QuotationProductBo();
			quotationProductBo.setProductName(quotationProduct.getProduct().getProductName());
			quotationProductBo.setProductRate(quotationProduct.getProductPrice().toString());
			quotationProductBo.setQuantity(quotationProduct.getQuantity().toString());
			quotationProductBo.setProductTaxAmount(quotationProduct.getProductTaxAmount().toString());
			quotationProductBo.setProductGstPer(quotationProduct.getProductGstPer().toString());
			Double totalPriceWithOutGst = quotationProduct.getProductPrice().doubleValue()
					
					* quotationProduct.getQuantity().doubleValue();
			quotationProductBo.setTotalPriceWithOutGst(totalPriceWithOutGst.toString());
			finalAmount += totalPriceWithOutGst;
			productList.add(quotationProductBo);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding(UTF_8);
		templateResolver.setOrder(0);
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context();
		context.setVariable("customerName", quotation.getCustomer().getCustomerName());
		context.setVariable("customerAddress",
				quotation.getCustomer().getAddressLineOne() + " " + quotation.getCustomer().getAddressLineTwo());
		context.setVariable("customerId", quotation.getCustomer().getCustomerId());
		context.setVariable("productList", productList);
		context.setVariable("finalAmount", finalAmount);
		context.setVariable("gstTax", quotation.getTotalTax());
		context.setVariable("discount", quotation.getDiscount());
		context.setVariable("grandTotal", quotation.getFinalAmount());
		context.setVariable("cGst", quotation.getcGst());
		context.setVariable("sGst", quotation.getsGst());
		context.setVariable("quotationNumber", quotation.getQuotationNumber());
		context.setVariable("totalAmount", quotation.getTotalAmount());
		context.setVariable("gstinNumber", quotation.getGstinNumber());
		context.setVariable("quotationDate", dateFormat.format(quotation.getQuotationDate()));
		return templateEngine.process("quotation", context);
	}

	@Override
	public void importQuotations(MultipartFile importFile) throws Exception {
	}

}
