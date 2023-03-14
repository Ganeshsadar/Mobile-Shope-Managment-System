package com.averta.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.averta.inventory.entity.Seller;
import com.averta.inventory.entity.SellerProduct;

public interface SellerProductRepository extends JpaRepository<SellerProduct, Long> {

	List<SellerProduct> findBySeller(Seller seller);
}
