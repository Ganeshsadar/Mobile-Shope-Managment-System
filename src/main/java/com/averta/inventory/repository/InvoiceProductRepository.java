package com.averta.inventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.averta.inventory.entity.Invoice;
import com.averta.inventory.entity.InvoiceProduct;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

	public List<InvoiceProduct> findByInvoice(Invoice invoice);

}
