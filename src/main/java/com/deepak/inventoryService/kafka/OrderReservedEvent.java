package com.deepak.inventoryService.kafka;

import lombok.Data;

@Data
public class OrderReservedEvent {

	private Long orderId;
	private Long userId;
	
    private String status;
    
}
