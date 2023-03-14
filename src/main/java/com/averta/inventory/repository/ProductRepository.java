package com.averta.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.averta.inventory.bo.ProcurmentProductBo;
import com.averta.inventory.bo.ProductPage;
import com.averta.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public Product findByProductName(String productName) throws Exception;

	public Product findByProductBrand(String productBrand) throws Exception;

	public List<Product> findByStatus(Boolean status) throws Exception;

	public Page<Product> findByProductNameContainingOrProductBrandContaining(String productName, String productBrand,
			Pageable pageable) throws Exception;

	@Query(value = "SELECT p.product_id as productId, p.product_name as productName,p.product_gst AS productGst, pp.available_quantity AS availableQuantity,\r\n"
			+ "pp.product_price AS productPrice, pp.batch_name AS batchName,pp.procurement_products_id AS procurementProductId\r\n"
			+ "FROM product p\r\n" + "INNER JOIN procurement_products pp ON p.product_id = pp.product_id\r\n"
			+ "WHERE p.`status`=TRUE \r\n" + "HAVING availableQuantity > 0", nativeQuery = true)
	public List<ProcurmentProductBo> productList() throws Exception;

	@Query(value = "SELECT p.product_id AS productId, p.product_name as productName, p.product_gst AS productGst, "
			+ "p.product_color AS color, p.product_brand AS brand, p.product_grade AS grade, p.product_unit AS unit, "
			+ "pp.batch_name AS batchName, p.product_type AS type, p.product_rate AS productRate, p.product_base AS base, p.product_volume AS volume, p.status AS status, "
			+ "SUM(pp.available_quantity) AS aveQua FROM product p "
			+ "LEFT JOIN procurement_products pp ON pp.product_id = p.product_id WHERE pp.product_id IS NOT NULL GROUP BY p.product_id ", nativeQuery = true)
	public Page<ProductPage> productPage(Pageable pageable) throws Exception;

	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, p.product_gst AS productGst,"
			+ " p.product_color AS color, p.product_brand AS brand, p.product_unit AS unit,"
			+ " pp.batch_name AS batchName, p.product_rate AS productRate,SUM(pp.available_quantity) AS aveQua"
			+ " FROM product p LEFT JOIN procurement_products pp ON p.product_id = pp.product_id"
			+ " WHERE (:productName IS NULL OR p.product_name LIKE %:productName%) GROUP BY p.product_id", countQuery = "SELECT COUNT(p.product_id) FROM product p "
			+ "LEFT JOIN procurement_products pp ON p.product_id = pp.product_id"
					+ " WHERE (:productName IS NULL OR p.product_name LIKE %:productName%) GROUP BY p.product_id", nativeQuery = true)
	public Page<ProductPage> getProdctByName(@Param("productName") String productName, Pageable pageable)
			throws Exception;

}
