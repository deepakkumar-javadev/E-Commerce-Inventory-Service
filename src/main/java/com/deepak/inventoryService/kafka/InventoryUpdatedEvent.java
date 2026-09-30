package com.deepak.inventoryService.kafka;

import lombok.Data;

@Data
public class InventoryUpdatedEvent {

	private Long orderId;
	private Long userId;
	private String orderNumber;
	private String message;

	// getters and setters
}
