package com.deepak.inventoryService.DTO;

import lombok.Data;

@Data
public class InventoryReqDto {

	private String skuCode;
	private Integer stockQuantity;
	
}
