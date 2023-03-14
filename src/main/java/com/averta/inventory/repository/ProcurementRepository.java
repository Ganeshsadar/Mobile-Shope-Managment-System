package com.averta.inventory.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.averta.inventory.entity.Procurement;

public interface ProcurementRepository extends JpaRepository<Procurement, Long> {

	public Procurement findByProcurementId(Long procurementId) throws Exception;

	public Page<Procurement> findByProcurementId(String searchBy, Pageable pageable) throws Exception;

	public Page<Procurement> findByBillNumberContainingOrGstinNumberContaining(String billNumber, String gstinNumber,
			Pageable pageable) throws Exception;

	@Query("SELECT i FROM Procurement i, Seller s WHERE i.seller.sellerId = s.sellerId"
			+ " AND (s.name LIKE %?1% OR s.mobileNumber LIKE %?1% OR s.companyName LIKE %?1% OR s.email LIKE %?1% OR s.gstnNumber LIKE %?1% OR i.billDate LIKE %?1% OR i.billNumber LIKE %?1%)")
	public Page<Procurement> searchProcurement(String searchBy, Pageable pageable) throws Exception;

	@Query(value = "SELECT(SELECT SUM(total_amount)  FROM invoice i WHERE DATE(i.created_at) >=:startDate AND DATE(i.created_at) <=:endDate)-"
			+ " (SELECT SUM(ip.quantity * pp.product_price) AS total_amount"
			+ " FROM invoice i, invoice_product ip, procurement_products pp"
			+ " WHERE i.invoice_id = ip.invoice_id AND ip.product_id = pp.product_id"
			+ " AND i.created_at >=:startDate AND i.created_at <=:endDate)", nativeQuery = true)
	public Double netProfit(@Param("startDate") Date startDate, @Param("endDate") Date endDate) throws Exception;

}
