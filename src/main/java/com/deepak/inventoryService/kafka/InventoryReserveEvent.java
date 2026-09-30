package com.deepak.inventoryService.kafka;

import java.util.List;



import lombok.Data;

@Data
public class InventoryReserveEvent {
	private Long orderId;

	private List<InventoryItemEvent> items;

	

}
