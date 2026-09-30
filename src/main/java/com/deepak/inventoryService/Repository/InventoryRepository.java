package com.deepak.inventoryService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deepak.inventoryService.entity.Inventory;

@Repository
public interface InventoryRepository  extends JpaRepository<Inventory,Long>{

	public Optional<Inventory> findBySkuCode(String skuCode);
	
	public boolean existsBySkuCode(String newSku );
	
	public Optional<Inventory> findById(Long id);
	
}
