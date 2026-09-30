package com.deepak.inventoryService.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventroyEventProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	// COD
	public void publishInventoryReserved(InventoryUpdatedEvent event) {

		 // produce event for Notification service
		kafkaTemplate.send("inventory.updated", event);

	}

	// ONLINE
	public void publishOrderReserved(OrderReservedEvent event) {

		// produce event for Order Service and Notification Service
		kafkaTemplate.send("inventory.reserved", event.getOrderId().toString(), event);

	}
}
