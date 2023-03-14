package com.averta.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.averta.inventory.entity.ProductBatch;

public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {

	
}
