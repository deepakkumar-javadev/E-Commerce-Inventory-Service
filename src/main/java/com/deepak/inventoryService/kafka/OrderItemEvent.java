package com.deepak.inventoryService.kafka;

import lombok.Data;

@Data
public class OrderItemEvent {

	private Long productId;
	private String productName;
	private String skuCode;
	private Integer quantity;
	private Double price;
}
