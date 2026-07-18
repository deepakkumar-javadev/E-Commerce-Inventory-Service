package com.deepak.inventoryService.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.deepak.inventoryService.DTO.InventoryReqDto;
import com.deepak.inventoryService.DTO.InventoryResDto;
import com.deepak.inventoryService.DTO.InventoryUpdateDto;
import com.deepak.inventoryService.DTO.InventoryUpdateId;

public interface InventoryService {

	// create Inventory
	public String createInventory(InventoryReqDto Req);
	
	// get Inventory Data using SkuCode
	public InventoryResDto getInventory(String skucode);
	
	// update inventory
	public ResponseEntity<String> updateInventory(InventoryUpdateDto req);
	
	// reduce quantity
	
	public ResponseEntity<String> reduceInventory( String skuCode,Integer quantity);
	
	// get all inventory...
	public List<InventoryResDto> getAllInventory();
	
	// update skucode(for admin)
	public String updateSku(String oldSku, String newSku);
	
	public String updateskuById(InventoryUpdateId req);
	
	public List<InventoryResDto> getInventoryBySkucode(List<String> skucodes);
	
	
}
