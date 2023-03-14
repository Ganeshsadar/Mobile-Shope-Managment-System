package com.averta.inventory.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.averta.inventory.bo.ProcurmentProductBo;
import com.averta.inventory.bo.Response;
import com.averta.inventory.entity.Product;
import com.averta.inventory.entity.Seller;
import com.averta.inventory.entity.SellerProduct;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.exception.ResourceNotFoundException;
import com.averta.inventory.repository.ProductRepository;
import com.averta.inventory.repository.SellerProductRepository;
import com.averta.inventory.repository.SellerRepository;
import com.averta.inventory.service.ProductService;
import com.averta.inventory.utility.ErrorConstants;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import com.averta.inventory.bo.ProductPage;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SellerProductRepository sellerProductRepository;

	@Autowired
	private Environment environment;

	@Autowired
	private SellerRepository sellerRepository;

	@Override
	public void addProduct(Product product) throws Exception {
		/*
		 * Product checkProduct =
		 * productRepository.findByProductName(product.getProductName()); if
		 * (checkProduct != null) { throw new InventoryException(ErrorConstants.INVALID,
		 * "Product name " + product.getProductName() + " already exist"); }
		 */
		product.setStatus(true);
		productRepository.save(product);
	}

	@Override
	public Product updateProduct(Product product, Long productId) throws Exception {
		Product checkproduct = getProductById(productId);
		checkproduct.setProductName(product.getProductName());
		checkproduct.setProductBrand(product.getProductBrand());
		checkproduct.setProductColor(product.getProductColor());
		checkproduct.setProductGst(product.getProductGst());
		checkproduct.setProductRate(product.getProductRate());
		checkproduct.setProductUnit(product.getProductUnit());
		return productRepository.save(checkproduct);
	}

	@Override
	public Product getProductById(Long productId) throws Exception {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOT_FOUND, "Product Not Found"));
	}

	@Override
	public Response getProducts(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception {
		Response response = new Response();
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage, sort);

		Page<ProductPage> page = null;
		page = productRepository.getProdctByName(searchBy, pageable);
		System.out.println(page);
		response.setStatus(ErrorConstants.SUCCESS);
		response.setMessage("Product List");
		response.setResult(page.getContent());
		response.setListCount(page.getTotalElements());
		return response;
	}

	@Override
	public List<ProcurmentProductBo> productList() throws Exception {
		List<ProcurmentProductBo> productList = productRepository.productList();
		return productList;
	}

	@Override
	public void setProductStatus(Long productId) throws Exception {
		Product product = getProductById(productId);
		if (product.getStatus()) {
			product.setStatus(false);
		} else {
			product.setStatus(true);
		}
		productRepository.save(product);
		Response response = new Response();
		response.setMessage("Stetus Changed to");
	}

	@Override
	public void importProducts(MultipartFile importFile) throws Exception {
		try {
			File convFile = new File(
					environment.getRequiredProperty("file.report.path") + importFile.getOriginalFilename());
			convFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(importFile.getBytes());
			fos.close();
			FileInputStream fis = new FileInputStream(convFile.getAbsolutePath());
			org.apache.poi.ss.usermodel.Workbook book = WorkbookFactory.create(fis);
			org.apache.poi.ss.usermodel.Sheet sheet = book.getSheetAt(0);
			Iterator<org.apache.poi.ss.usermodel.Row> itr = sheet.iterator();
			int i = 1;
			Cell c = null;
			Product product = null;
			List<Product> list = new ArrayList<Product>();

			while (itr.hasNext()) {
				Row row = itr.next();

				if (i == 1) {
					i++;
					continue;
				}
				product = new Product();
				c = row.getCell(1);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					product.setProductName(getCellString(c));
				} else {
					i++;
					continue;
				}

				c = row.getCell(2);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					product.setProductBrand(getCellString(c));
				} else
					product.setProductBrand("-");

				c = row.getCell(3);
				c = row.getCell(3);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					product.setProductColor(getCellString(c));
				} else {
					product.setProductColor("-");
				}

				c = row.getCell(5);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					String s = getCellString(c);
					if (s.matches("(.*)[0-9]+(.*)")) {
						throw new InventoryException(ErrorConstants.INVALID, "Unit value must be string in row =" + i);
					}
					product.setProductUnit(getCellString(c));
				} else {
					throw new InventoryException(ErrorConstants.INVALID, " Unit is missing at row =" + i);
				}

				c = row.getCell(6);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					String s = getCellString(c);
					try {
						Double.parseDouble(s);
					} catch (NumberFormatException e) {
						throw new InventoryException(ErrorConstants.INVALID, "Rate value must be numeric in row =" + i);
					}
					product.setProductRate(BigDecimal.valueOf(Double.valueOf(getCellString(c))));
				} else {
					throw new InventoryException(ErrorConstants.INVALID, "Rate is missing at row =" + i);
				}

//				c = row.getCell(11);
//				if (null != c && !getCellString(c).trim().isEmpty()) {
//					c.setCellType(Cell.CELL_TYPE_STRING);
//					String s = getCellString(c);
//					try {
//						Long.parseLong(s);
//					} catch (NumberFormatException e) {
//						throw new InventoryException(ErrorConstants.INVALID,
//								"Quantity value must be numeric in row =" + i);
//					}
//
//					product.setQuantity(Long.parseLong(s));
//				} else {
//					throw new InventoryException(ErrorConstants.INVALID, "Quantity is missing at row =" + i);
//				}

				c = row.getCell(11);
				if (null != c && getCellString(c) != null && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					String s = getCellString(c);
					if (s.length() > 2) {
						throw new InventoryException(ErrorConstants.INVALID, "Gst shud be only two digits = " + i);
					}
					try {
						Long.parseLong(s);
					} catch (NumberFormatException e) {
						throw new InventoryException(ErrorConstants.INVALID, "Gst value must be numeric in row =" + i);
					}
					product.setProductGst(Double.valueOf(s));
				} else {
					throw new InventoryException(ErrorConstants.INVALID, "Product GST missing at row = " + i);
				}

				product.setStatus(true);

				/*
				 * Product checkName =
				 * productRepository.findByProductName(product.getProductName()); if (checkName
				 * != null) { throw new InventoryException(ErrorConstants.INVALID, "Product " +
				 * product.getProductName() + " already exists"); }
				 */

				validateProduct(product, i);
				list.add(product);
				i++;

				productRepository.save(product);

				SellerProduct sellerProduct = null;
				Seller seller = null;
				c = row.getCell(13);
				if (null != c && !getCellString(c).trim().isEmpty()) {
					c.setCellType(Cell.CELL_TYPE_STRING);
					String sellerName = getCellString(c);
					System.out.println(sellerName);
					seller = sellerRepository.findByName(sellerName);
					if (null != seller) {
						sellerProduct = new SellerProduct();
						sellerProduct.setSeller(seller);
					} else {
						sellerProduct = new SellerProduct();
						seller = new Seller();
						seller.setName(sellerName.toString());
						c = row.getCell(13);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							seller.setName(getCellString(c));
						}
						c = row.getCell(14);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							String s = getCellString(c);
							seller.setMobileNumber(s);
						}
						c = row.getCell(15);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							seller.setGstnNumber(getCellString(c));
						}
						c = row.getCell(16);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							seller.setCompanyName(getCellString(c));
						}
						c = row.getCell(17);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							seller.setEmail(getCellString(c));
						}
						c = row.getCell(18);
						if (null != c && !getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							seller.setAddress(getCellString(c));
						}
						c = row.getCell(19);
						if (null != c && getCellString(c).trim().isEmpty()) {
							c.setCellType(Cell.CELL_TYPE_STRING);
							seller.setState(getCellString(c));
						}
						sellerRepository.save(seller);
						sellerProduct.setSeller(seller);
					}
					sellerProduct.setProduct(product);
					sellerProductRepository.save(sellerProduct);
				}
			}

			/*
			 * for (int l = 0; l < list.size(); l++) { for (int m = l + 1; m < list.size();
			 * m++) { if
			 * (list.get(l).getProductName().equalsIgnoreCase(list.get(m).getProductName()))
			 * { throw new InventoryException(ErrorConstants.INVALID, "Duplicate Product " +
			 * product.getProductName() + " in excel"); } } }
			 */

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

	@SuppressWarnings("unused")
	private Long getNumericValue(Cell cell) {
		return (long) cell.getNumericCellValue();
	}

	public Boolean validateProduct(Product product, int rowNo) throws Exception {
		Boolean flag = true;
		if (product.getProductName() == null) {
			flag = false;
			throw new InventoryException(ErrorConstants.INVALID, "Product name is missing at row = " + rowNo);
		}

		if (product.getProductGst() == null) {
			throw new InventoryException(ErrorConstants.INVALID, "Gst is missing at = " + rowNo);
		}

		if (product.getProductRate() == null) {
			throw new InventoryException(ErrorConstants.INVALID, "Rate is missing at = " + rowNo);
		}

		if (product.getProductUnit() == null) {
			throw new InventoryException(ErrorConstants.INVALID, "Unit is missing at = " + rowNo);
		}

//		if (product.getQuantity() == null) {
//			throw new InventoryException(ErrorConstants.INVALID, "Quantity is missing at = " + rowNo);
//		}

		return flag;
	}

}
