package com.averta.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.averta.inventory.entity.QuotationProduct;
import com.averta.inventory.entity.Quotation;
import java.util.List;

public interface QuotationProductRepository extends JpaRepository<QuotationProduct,Long>{
	
	List<QuotationProduct> findByQuotation(Quotation quotation);

}
