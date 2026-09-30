package com.deepak.inventoryService.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.deepak.inventoryService.Service.InventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

	private final InventoryService inventoryService;

	// COD

	@KafkaListener(topics = "order.created", groupId = "inventory-service",containerFactory = "orderCreatedKafkaListenerContainerFactory")
	public void consumeOrderCreated(OrderCreatedEvent event) {

		 System.out.println("========== COD ORDER.CREATED RECEIVED ==========");
		    System.out.println("Order ID: " + event.getOrderId());
		    System.out.println("Order Number: " + event.getOrderNumber());
		    
		    
		inventoryService.reduceStock(event);
	}

	// ONLINE....

	@KafkaListener(topics = "inventory.reserve", groupId = "inventory-service")
	public void consumeInventoryReserve(InventoryReserveEvent event) {
		inventoryService.reserveStock(event);
	}

	//inventory commit after delivery + payment done

	@KafkaListener(topics = "inventory.commit", groupId = "inventory-service",containerFactory = "inventoryCommitKafkaListenerContainerFactory")
	public void consumeInventoryCommit(InventoryCommitEvent event) {

		System.out.println("Inventory commit received for order: " + event.getOrderNumber());

		inventoryService.commitInventory(event);
	}

}
