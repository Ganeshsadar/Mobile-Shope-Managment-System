package com.averta.inventory.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.averta.inventory.bo.CustomerReport;
import com.averta.inventory.bo.ReportOne;
import com.averta.inventory.bo.TopProductBo;
import com.averta.inventory.entity.Customer;
import com.averta.inventory.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	public Invoice findByInvoiceId(Long invoiceId) throws Exception;

	public Page<Invoice> findByInvoiceId(String searchBy, Pageable pageable) throws Exception;

	public Page<Invoice> findByCustomer(Customer customer, Pageable pageable) throws Exception;

	@Query("SELECT i FROM Invoice i, Customer c WHERE i.customer.customerId = c.customerId"
			+ " AND (c.customerName LIKE %?1% OR c.mobileNumber LIKE %?1% OR i.invoiceNumber LIKE %?1%)")
	public Page<Invoice> searchInvoice(String searchBy, Pageable pageable);

	@Query(value = "SELECT SUM(total_amount) FROM invoice i WHERE DATE(i.created_at) >=:startDate AND DATE(i.created_at) <=:endDate ", nativeQuery = true)
	public Double getRevenue(@Param("startDate") Date startDate, @Param("endDate") Date endDate) throws Exception;

	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, SUM(ip.quantity) AS quantity \r\n"
			+ "FROM invoice_product ip\r\n"
			+ "INNER JOIN product p ON p.product_id = ip.product_id\r\n"
			+ "WHERE DATE(ip.created_at) >=:startDate AND DATE(ip.created_at) <=:endDate\r\n"
			+ "GROUP BY p.product_id\r\n"
			+ "ORDER BY quantity DESC LIMIT 5\r\n"
			+ "", nativeQuery = true)
	public List<TopProductBo> topSellingProduct(@Param("startDate") Date startDate, @Param("endDate") Date endDate)
			throws Exception;

	@Query(value = "SELECT SUM(i.quantity) FROM invoice_product i\r\n"
			+ "WHERE DATE(i.created_at) >=:startDate AND DATE(i.created_at) <=:endDate", nativeQuery = true)
	public Double totalProducts(@Param("startDate") Date startDate, @Param("endDate") Date endDate) throws Exception;
	
	@Query(value ="SELECT(SELECT SUM(ip.quantity )*SUM(pp.product_price) AS total\r\n"
			+ "FROM invoice i, invoice_product ip, procurement_products pp\r\n"
			+ "WHERE i.invoice_id = ip.invoice_id AND ip.product_id = pp.product_id\r\n"
			+ "AND DATE(i.created_at) >=:sDate AND DATE(i.created_at) <=:endDate)\r\n"
			+ "-\r\n"
			+ "(SELECT SUM(i.total_amount) AS total FROM invoice i \r\n"
			+ "WHERE DATE(i.created_at) >=:sDate AND DATE(i.created_at) <=:endDate)\r\n"
			+ "UNION \r\n"
			+ "(SELECT SUM(i.total_amount) AS total FROM invoice i \r\n"
			+ "WHERE DATE(i.created_at) >=:startDate AND DATE(i.created_at) <=endDate)\r\n"
			+ "",nativeQuery = true)
	public Double netProfitVsRev (@Param("startDate") int sDate,@Param("endDate") Date endDate) throws Exception;
	
	@Query(value ="SELECT p.product_id AS productId,product_name AS productName,\r\n"
			+ "pp.available_quantity AS availableQuantity,pp.quantity AS orderedQuantity,pp.product_price AS productRate,s.name AS sellerName\r\n"
			+ "FROM procurement_products pp\r\n"
			+ "INNER JOIN  product p  ON p.product_id = pp.product_id \r\n"
			+ "INNER JOIN procurement pr ON pr.procurement_id = pp.procurement_id\r\n"
			+ "INNER JOIN seller s ON s.seller_id =pr.seller_id\r\n"
			+ "WHERE DATE(pp.created_at) >=:startDate AND DATE(pp.created_at) <= :endDate\r\n"
			+ "ORDER BY pp.available_quantity DESC LIMIT 30"
			+ "",nativeQuery = true)
	public List<ReportOne> monthlyBuying(@Param("startDate") Date startDate, @Param("endDate") Date endDate)
			throws Exception;
	
	
	@Query(value = "SELECT p.product_id AS productId,product_name AS productName,\r\n"
			+ "pp.available_quantity AS availableQuantity,ip.quantity AS soldProducts ,ip.product_price AS productRate,customer_name AS customerName\r\n"
			+ "FROM invoice_product ip \r\n"
			+ "INNER JOIN procurement_products pp  ON pp.product_id = ip.product_id\r\n"
			+ "INNER JOIN product p  ON p.product_id = ip.product_id\r\n"
			+ "INNER JOIN invoice i ON i.invoice_id = ip.invoice_id\r\n"
			+ "INNER JOIN customer c ON c.customer_id=i.customer_id\r\n"
			+ "WHERE DATE(ip.created_at) >=:startDate AND DATE(ip.created_at) <=:endDate\r\n"
			+ "ORDER BY pp.available_quantity DESC LIMIT 30",nativeQuery = true)
	public List<CustomerReport> monthlySells(@Param("startDate") Date startDate, @Param("endDate") Date endDate)
			throws Exception;
	
}
