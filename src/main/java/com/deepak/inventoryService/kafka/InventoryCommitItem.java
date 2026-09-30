package com.deepak.inventoryService.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCommitItem {

	private String skuCode;
    private Integer quantity;
}
