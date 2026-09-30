package com.deepak.inventoryService.kafka;

import lombok.Data;

@Data
public class InventoryItemEvent {
	
	private String skuCode;
	private Integer quantity;
	
}
