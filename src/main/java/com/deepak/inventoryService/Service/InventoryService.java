package com.deepak.inventoryService.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.deepak.inventoryService.DTO.InventoryReqDto;
import com.deepak.inventoryService.DTO.InventoryResDto;
import com.deepak.inventoryService.DTO.InventoryUpdateDto;
import com.deepak.inventoryService.DTO.InventoryUpdateId;
import com.deepak.inventoryService.kafka.InventoryCommitEvent;
import com.deepak.inventoryService.kafka.InventoryReserveEvent;
import com.deepak.inventoryService.kafka.OrderCreatedEvent;

public interface InventoryService {

	// create Inventory
	public String createInventory(InventoryReqDto Req);
	
	// get Inventory Data using SkuCode
	public InventoryResDto getInventory(String skucode);
	
	// update inventory
	public ResponseEntity<String> updateInventory(InventoryUpdateDto req);
	
	// reduce stock by order service
	public ResponseEntity<String> reduceInventory( String skuCode,Integer quantity);
	
	// reduce Stock by kafka (COD)
	public void  reduceStock(OrderCreatedEvent event);
	
	//reduce stock by kafka (ONLINE)
	public void reserveStock(InventoryReserveEvent event);
	
	// get all inventory...
	public List<InventoryResDto> getAllInventory();
	
	// update skucode(for admin)
	public String updateSku(String oldSku, String newSku);
	
	public String updateskuById(InventoryUpdateId req);
	
	public List<InventoryResDto> getInventoryBySkucode(List<String> skucodes);
	
	//COMMIT RESERVED INVENTORY
	
	public void commitInventory(InventoryCommitEvent event);
	
	
}
