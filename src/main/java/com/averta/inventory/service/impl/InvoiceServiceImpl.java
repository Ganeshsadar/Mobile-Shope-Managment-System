
package com.averta.inventory.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.averta.inventory.bo.CustomerReport;
import com.averta.inventory.bo.InvoiceProductBo;
import com.averta.inventory.bo.ReportOne;
import com.averta.inventory.bo.Response;
import com.averta.inventory.bo.TopProductBo;
import com.averta.inventory.entity.Customer;
import com.averta.inventory.entity.Invoice;
import com.averta.inventory.entity.InvoiceProduct;
import com.averta.inventory.entity.ProcurementProducts;
import com.averta.inventory.entity.Product;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.CustomerRepository;
import com.averta.inventory.repository.InvoiceProductRepository;
import com.averta.inventory.repository.InvoiceRepository;
import com.averta.inventory.repository.ProcurementProductRepository;
import com.averta.inventory.repository.ProductRepository;
import com.averta.inventory.service.CustomerService;
import com.averta.inventory.service.InvoiceServic;
import com.averta.inventory.service.ProcurementService;
import com.averta.inventory.service.ProductService;
import com.averta.inventory.utility.ErrorConstants;

@Service
public class InvoiceServiceImpl implements InvoiceServic {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private InvoiceProductRepository invoiceProductRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProcurementService procurementService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private Environment environment;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProcurementProductRepository procurementProductRepository;

	private static final String UTF_8 = "UTF-8";

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void addInvoice(Invoice invoice) throws Exception {
		invoice.setCustomer(customerService.getById(invoice.getCustomerId()));
		List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			invoiceProduct.setProduct(productService.getProductById(invoiceProduct.getProductId()));
			invoiceProduct.setProcurementProducts(
					procurementService.getProcurementProductById(invoiceProduct.getProcurementProductsId()));
			invoiceProduct.setProductGstPer(BigDecimal.valueOf(invoiceProduct.getProduct().getProductGst()));
			invoiceProduct.setInvoice(invoice);
		}
		
		Date date = new Date();
		invoice.setInvoiceDate(date);

		Double totalInvoiceAmount = 0.0;
		Double discount = 0.0;
		Double totalamount = 0.0;
		Double totaltax = 0.0;
		Double productTaxAmount = 0.0;
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			invoiceProduct.setProductPrice(invoiceProduct.getProductPrice());

			totalInvoiceAmount += invoiceProduct.getProductPrice().doubleValue() * invoiceProduct.getQuantity();
			discount = invoice.getDiscount().doubleValue();

			if (invoice.getWithGst().equals(true)) {

				totaltax += invoiceProduct.getProductPrice().doubleValue() * invoiceProduct.getQuantity()
						* invoiceProduct.getProductGstPer().doubleValue() / 100;
			} else {
				invoiceProduct.setProductGstPer(BigDecimal.ZERO);
				totaltax += invoiceProduct.getProductPrice().doubleValue() * invoiceProduct.getQuantity()
						* invoiceProduct.getProductGstPer().doubleValue() / 100;

			}
			System.out.println("total" + totaltax);

			invoice.setTotalAmount(BigDecimal.valueOf(totalInvoiceAmount));
			
			totalamount = totalInvoiceAmount - discount;

			invoice.setDiscount(BigDecimal.valueOf(discount));

			invoice.setFinalAmount(BigDecimal.valueOf(totalamount + totaltax));

			invoice.setTotalTax(BigDecimal.valueOf(totaltax));

			if (invoice.getWithGst().equals(true)) {
				invoiceProduct.setProductGstPer(BigDecimal.valueOf(invoiceProduct.getProduct().getProductGst()));
			} else {
				invoiceProduct.setProductGstPer(BigDecimal.ZERO);
				invoiceProduct.setProductGstPer(BigDecimal.valueOf(invoiceProduct.getProduct().getProductGst()));
			}
			invoice.setcGst(BigDecimal.valueOf(totaltax / 2));

			invoice.setsGst(BigDecimal.valueOf(totaltax / 2));
			invoice.setGstinNumber("27BCXPM3444A1ZH");

			if (invoice.getWithGst().equals(true)) {
				
				productTaxAmount = invoiceProduct.getProductPrice().doubleValue()
						* invoiceProduct.getQuantity().doubleValue() * invoiceProduct.getProductGstPer().doubleValue()
						/ 100;
			} else {
				invoiceProduct.setProductGstPer(BigDecimal.ZERO);
				productTaxAmount = invoiceProduct.getProductPrice().doubleValue()
						* invoiceProduct.getQuantity().doubleValue() * invoiceProduct.getProductGstPer().doubleValue()
						/ 100;
			}
			invoiceProduct.setProductTaxAmount(BigDecimal.valueOf(
					invoiceProduct.getQuantity() * invoiceProduct.getProductPrice().doubleValue() + productTaxAmount));

//			Product product = invoiceProduct.getProduct();
//			if (invoiceProduct.getQuantity().equals(0)) {
//				throw new InventoryException(ErrorConstants.INVALID,
//						"Insufficient quantity of " + product.getProductName());
//			}
//			product.setQuantity(product.getQuantity() - invoiceProduct.getQuantity());
//			productRepository.save(product);
			ProcurementProducts procurementProducts = invoiceProduct.getProcurementProducts();
			if (procurementProducts.getAvailableQuantity() < invoiceProduct.getQuantity()) {
				throw new InventoryException(ErrorConstants.INVALID,
						"Insufficient quantity of " + procurementProducts.getProduct().getProductName());
			}
			procurementProducts
					.setAvailableQuantity(procurementProducts.getAvailableQuantity() - invoiceProduct.getQuantity());
			procurementProductRepository.save(procurementProducts);

		}

		invoiceRepository.save(invoice);
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			invoiceProductRepository.save(invoiceProduct);
			invoiceProduct.getInvoiceProductId();
		}
		invoice.setInvoiceNumber(getInvoiceNumber(invoice.getInvoiceId()));

		invoiceRepository.save(invoice);
	}

	private String getInvoiceNumber(Long invoiceId) {
		String invoiceNum = "";
		Calendar calendar = Calendar.getInstance();
		String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
		String nextYear = String.valueOf(calendar.get(Calendar.YEAR) + 1);
		String monthString = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String id = "";
		if (invoiceId < 1000) {
			if (String.valueOf(invoiceId).length() == 1) {
				id += "000" + invoiceId;
			} else if (String.valueOf(invoiceId).length() == 2) {
				id += "00" + invoiceId;
			} else if (String.valueOf(invoiceId).length() == 3) {
				id += "0" + invoiceId;
			}
		}
		invoiceNum += id + "/" + currentYear.substring(2) + "-" + nextYear.substring(2) + "/" + monthString + "/"
				+ "SAJ";

		return invoiceNum;
	}

	@Override
	public Invoice updateInvoice(Invoice invoice, Long invoiceId) throws Exception {
		Invoice checkinvoice = getInvoiceById(invoiceId);
		checkinvoice.setDiscount(invoice.getDiscount());
		return invoiceRepository.save(checkinvoice);
	}

	@Override
	public Response listInvoice(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception {
		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);
		Page<Invoice> page = null;
		if (searchBy != null && !searchBy.isEmpty()) {
			page = invoiceRepository.searchInvoice(searchBy, pageable);
		} else {
			page = invoiceRepository.findAll(pageable);
		}
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Invoice List ");
		response.setResult(page.getContent());
		response.setListCount(page.getTotalElements());
		return response;
	}

	@Override
	public Invoice getInvoiceById(Long invoiceId) throws Exception {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Invoice not found"));
		List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoice(invoice);
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			Double taxOnProduct = invoiceProduct.getProduct().getProductGst().doubleValue()
					* invoiceProduct.getProductPrice().doubleValue() / 100;
			invoiceProduct.setProductGstAmount(BigDecimal.valueOf(taxOnProduct));
		}
		invoice.setInvoiceProducts(invoiceProducts);
		return invoice;
	}

	@Override
	public void DeletByInvoiceId(Long invoiceId) throws Exception {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Invoice not found"));
		List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoice(invoice);
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			invoiceProductRepository.delete(invoiceProduct);
			ProcurementProducts procurementPro = invoiceProduct.getProcurementProducts();
			procurementPro.setAvailableQuantity(procurementPro.getAvailableQuantity() + invoiceProduct.getQuantity());
			procurementProductRepository.save(procurementPro);
		}
		invoiceRepository.delete(invoice);
	}

	@Override
	public String generateInvoicePdf(Long invoiceId) throws Exception {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Invoice not found"));
		List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoice(invoice);

		List<InvoiceProductBo> productList = new ArrayList<InvoiceProductBo>();
		Double finalAmount = 0.0;

		InvoiceProductBo invoiceProductBo = null;
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			invoiceProductBo = new InvoiceProductBo();
			invoiceProductBo.setProductName(invoiceProduct.getProduct().getProductName());
			invoiceProductBo.setProductRate(invoiceProduct.getProductPrice().toString());
			invoiceProductBo.setQuantity(invoiceProduct.getQuantity().toString());
			invoiceProductBo.setProductGstPer(invoiceProduct.getProductGstPer().toString());
			invoiceProductBo.setProductTaxAmount(invoiceProduct.getProductTaxAmount().toString());
			Double totalPriceWithOutGst = invoiceProduct.getProductPrice().doubleValue()
					* invoiceProduct.getQuantity().doubleValue();
			invoiceProductBo.setTotalPriceWithOutGst(totalPriceWithOutGst.toString());
			finalAmount += totalPriceWithOutGst;
			productList.add(invoiceProductBo);

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
		context.setVariable("customerName", invoice.getCustomer().getCustomerName());
		context.setVariable("customerAddress",
				invoice.getCustomer().getAddressLineOne() + " " + invoice.getCustomer().getAddressLineTwo());
		context.setVariable("customerId", invoice.getCustomer().getCustomerId());
		context.setVariable("productList", productList);
		context.setVariable("finalAmount", finalAmount);
		context.setVariable("gstTax", invoice.getTotalTax());
		context.setVariable("discount", invoice.getDiscount());
		context.setVariable("grandTotal", invoice.getFinalAmount());
		context.setVariable("cGst", invoice.getcGst());
		context.setVariable("sGst", invoice.getsGst());
		context.setVariable("invoiceNumber", invoice.getInvoiceNumber());
		context.setVariable("totalAmount", invoice.getTotalAmount());
		context.setVariable("gstinNumber", invoice.getGstinNumber());
		context.setVariable("invoiceDate", dateFormat.format(invoice.getInvoiceDate()));
		String message = templateEngine.process("invoice", context);

		System.out.println(message);

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(message);
		renderer.setScaleToFit(true);
		renderer.layout();

		String fileName = "Invoice_" + invoice.getCustomer().getCustomerName() + ".pdf";
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
	public String generateInvoicePrint(Long invoiceId) throws Exception {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Invoice not found"));
		List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoice(invoice);

		List<InvoiceProductBo> productList = new ArrayList<InvoiceProductBo>();
		Double finalAmount = 0.0;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		InvoiceProductBo invoiceProductBo = null;
		for (InvoiceProduct invoiceProduct : invoiceProducts) {
			invoiceProductBo = new InvoiceProductBo();
			invoiceProductBo.setProductName(invoiceProduct.getProduct().getProductName());
			invoiceProductBo.setProductRate(invoiceProduct.getProductPrice().toString());
			invoiceProductBo.setQuantity(invoiceProduct.getQuantity().toString());
			invoiceProductBo.setProductGstPer(invoiceProduct.getProductGstPer().toString());
			invoiceProductBo.setProductTaxAmount(invoiceProduct.getProductTaxAmount().toString());
			Double totalPriceWithOutGst = invoiceProduct.getProductPrice().doubleValue()
					* invoiceProduct.getQuantity().doubleValue();
			invoiceProductBo.setTotalPriceWithOutGst(totalPriceWithOutGst.toString());
			finalAmount += totalPriceWithOutGst;
			productList.add(invoiceProductBo);

		}

		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding(UTF_8);
		templateResolver.setOrder(0);
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		Context context = new Context();
		context.setVariable("customerName", invoice.getCustomer().getCustomerName());
		context.setVariable("customerAddress",
				invoice.getCustomer().getAddressLineOne() + " " + invoice.getCustomer().getAddressLineTwo());
		context.setVariable("customerId", invoice.getCustomer().getCustomerId());
		context.setVariable("productList", productList);
		context.setVariable("finalAmount", finalAmount);
		context.setVariable("gstTax", invoice.getTotalTax());
		context.setVariable("discount", invoice.getDiscount());
		context.setVariable("grandTotal", invoice.getFinalAmount());
		context.setVariable("cGst", invoice.getcGst());
		context.setVariable("sGst", invoice.getsGst());
		context.setVariable("invoiceNumber", invoice.getInvoiceNumber());
		context.setVariable("totalAmount", invoice.getTotalAmount());
		context.setVariable("gstinNumber", invoice.getGstinNumber());
		context.setVariable("invoiceDate", dateFormat.format(invoice.getInvoiceDate()));
		return templateEngine.process("invoice", context);
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void importInvoices(MultipartFile importFile) throws Exception {
		try {
			File convFile = new File(importFile.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(importFile.getBytes());
			fos.close();
			FileInputStream fis = new FileInputStream(convFile.getAbsolutePath());
			Workbook book = WorkbookFactory.create(fis);
			Sheet sheet = book.getSheetAt(0);
			Iterator<org.apache.poi.ss.usermodel.Row> itr = sheet.iterator();
			int i = 1;
			Cell c = null;
			Invoice invoice = null;
			List<Invoice> list = new ArrayList<Invoice>();

			Map<String, Invoice> invoiceMap = new HashMap<>();

			while (itr.hasNext()) {
				Row row = itr.next();

				if (i == 1) {
					i++;
					continue;
				}

				c = row.getCell(3);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					if (invoiceMap.containsKey(getCellString(c))) {
						invoice = invoiceMap.get(getCellString(c));

						Product product = null;
						InvoiceProduct invoiceProduct = null;
						c = row.getCell(5);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							String productname = getCellString(c);
							Product checkProduct = productRepository.findByProductName(productname);
							if (null != checkProduct) {
								invoiceProduct = new InvoiceProduct();
								invoiceProduct.setProduct(checkProduct);
								invoiceProduct.setInvoice(invoice);
								invoice.getInvoiceProduct().add(invoiceProduct);
							} else {
								product = new Product();
								product.setProductName(productname);
								productRepository.save(product);
								invoiceProduct = new InvoiceProduct();
								invoiceProduct.setProduct(product);
								invoiceProduct.setInvoice(invoice);
								invoice.getInvoiceProduct().add(invoiceProduct);
							}
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " product is missing at row =" + i);
						}

						c = row.getCell(6);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							if (product != null)
								product.setProductRate(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
							invoiceProduct.setProductPrice(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID,
									" product Price is missing at row =" + i);
						}

						c = row.getCell(7);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoiceProduct.setProductTaxAmount(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " CGST is missing at row =" + i);
						}

						c = row.getCell(8);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							String s = getCellString(c);
							try {
								Long.parseLong(s);
							} catch (NumberFormatException e) {
								throw new InventoryException(ErrorConstants.INVALID,
										"Quantity value must be numeric in row =" + i);
							}
							invoiceProduct.setQuantity(Long.parseLong(s));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, "Quantity is missing at row =" + i);
						}

						if (product != null) {
							product.setProductUnit("unit");
							product.setProductRate(BigDecimal.ONE);
//							product.setQuantity(5l);
							product.setProductGst(12d);
							productRepository.save(product);
						}
					} else {

						invoice = new Invoice();

						Customer customer = null;
						c = row.getCell(1);
						if (null != c && getCellString(c) != null && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_NUMERIC);
							Long mobile = getNumericValue(c);
							customer = customerRepository.findByMobileNumber(mobile.toString());
							if (null != customer) {
								invoice.setCustomer(customer);
							} else {
								customer = new Customer();
								customer.setMobileNumber(mobile.toString());
								c = row.getCell(2);
								if (null != c && !getCellString(c).trim().isEmpty()) {
									customer.setCustomerName(getCellString(c));
								}
								customerRepository.save(customer);
								invoice.setCustomer(customer);
							}
						} else {
							throw new InventoryException(ErrorConstants.INVALID,
									"customer mobile number is missing = " + i);
						}

						c = row.getCell(3);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setInvoiceNumber(getCellString(c));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, "invoice Number missing = " + i);
						}

						c = row.getCell(4);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							String s = getCellString(c);
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
							invoice.setInvoiceDate(formatter.parse(s));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, "Invoise Date missing at = " + i);
						}

						Product product = null;
						InvoiceProduct invoiceProduct = null;
						c = row.getCell(5);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							String productname = getCellString(c);
							Product checkProduct = productRepository.findByProductName(productname);
							if (null != checkProduct) {
								invoiceProduct = new InvoiceProduct();
								invoiceProduct.setProduct(checkProduct);
								invoiceProduct.setInvoice(invoice);
								invoice.getInvoiceProduct().add(invoiceProduct);
							} else {
								product = new Product();
								product.setProductName(productname);

								invoiceProduct = new InvoiceProduct();
								invoiceProduct.setProduct(product);
								invoiceProduct.setInvoice(invoice);
								invoice.getInvoiceProduct().add(invoiceProduct);
							}
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " product is missing at row =" + i);
						}

						c = row.getCell(6);
						if (null != c && getCellString(c) != null && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							if (product != null)
								product.setProductRate(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
							invoiceProduct.setProductPrice(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID,
									" product Price is missing at row =" + i);
						}

						c = row.getCell(7);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoiceProduct.setProductTaxAmount(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " CGST is missing at row =" + i);
						}

						c = row.getCell(8);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							String s = getCellString(c);
							try {
								Long.parseLong(s);
							} catch (NumberFormatException e) {
								throw new InventoryException(ErrorConstants.INVALID,
										"Quantity value must be numeric in row =" + i);
							}
							invoiceProduct.setQuantity(Long.parseLong(s));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, "Quantity is missing at row =" + i);
						}

						c = row.getCell(9);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setcGst(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " CGST is missing at row =" + i);
						}

						c = row.getCell(10);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setsGst(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " SGST is missing at row =" + i);
						}
						c = row.getCell(11);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setDiscount(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID, " Discount is missing at row =" + i);
						}

						c = row.getCell(12);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setTotalTax(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
							;
						} else {
							throw new InventoryException(ErrorConstants.INVALID,
									" Total amount is missing at row =" + i);
						}

						c = row.getCell(13);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setTotalAmount(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID,
									" Total amount is missing at row =" + i);
						}

						c = row.getCell(14);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							invoice.setFinalAmount(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
						} else {
							throw new InventoryException(ErrorConstants.INVALID,
									" Final Amount is missing at row =" + i);
						}
						if (product != null) {
							product.setProductUnit("unit");
							product.setProductRate(BigDecimal.ONE);
//							product.setQuantity(5l);
							product.setProductGst(12d);
							productRepository.save(product);
						}

						invoice.setStatus("New");
					}
				} else {
					throw new InventoryException(ErrorConstants.INVALID, "invoice Number missing = " + i);
				}

				invoiceMap.put(invoice.getInvoiceNumber(), invoice);
				list.add(invoice);
				i++;
			}
			invoiceRepository.saveAll(list);
		} catch (Exception e) {
			throw e;
		}

	}

	private String getCellString(Cell cell) {
		String s = "";
		long l = 0;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			s = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			l = (long) cell.getNumericCellValue();
			s = l + "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			s = cell.getBooleanCellValue() + "";
			break;
		default:
		}

		return s;
	}

	private Long getNumericValue(Cell cell) {
		return (long) cell.getNumericCellValue();
	}

	@Override
	public Double monthRevenue() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		return invoiceRepository.getRevenue(startDate, endDate);
	}

	@Override
	public List<TopProductBo> topProducts() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		List<TopProductBo> list = invoiceRepository.topSellingProduct(startDate, endDate);
		return list;
	}

	@Override
	public Double totalProducts() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		return invoiceRepository.totalProducts(startDate, endDate);
	}

	@Override
	public Double netVsRev() throws Exception {
		// int sDate = 1;
		Calendar cal = Calendar.getInstance();
		LocalDateTime startMonth = LocalDateTime.now();
		System.out.println(startMonth);
		LocalDateTime endMonth = startMonth.minusMonths(6);
		DateTimeFormatter dateFormatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for (LocalDateTime date = startMonth; date.isAfter(endMonth); date = date.minusMonths(1)) {
			System.out.println(date.format(dateFormatter3) + "dates");
			cal.set(Calendar.DAY_OF_MONTH, -1);
			Date startDate = cal.getTime();
			System.out.println(startDate);
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
			Date endDate = cal.getTime();
			System.out.println(endDate);
		}
		return null;

	}

	@Override
	public List<ReportOne> monthlyBuying() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		List<ReportOne> list = invoiceRepository.monthlyBuying(startDate, endDate);
		return list;
	}

	@Override
	public List<CustomerReport> monthlySells() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		System.out.println("start date" + startDate);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date endDate = cal.getTime();
		System.out.println("end date" + endDate);
		List<CustomerReport> list = invoiceRepository.monthlySells(startDate, endDate);
		return list;
	}

}
