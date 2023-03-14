package com.averta.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.averta.inventory.entity.Procurement;
import com.averta.inventory.entity.ProcurementProducts;

public interface ProcurementProductRepository extends JpaRepository<ProcurementProducts, Long> {

	public List<ProcurementProducts> findByProcurement(Procurement procurement);
}
