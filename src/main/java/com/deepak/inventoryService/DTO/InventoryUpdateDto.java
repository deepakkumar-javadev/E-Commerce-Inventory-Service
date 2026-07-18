package com.deepak.inventoryService.DTO;

import lombok.Data;

@Data
public class InventoryUpdateDto {
	
	private String skuCode;
	private int stockQuantity;
	
}
