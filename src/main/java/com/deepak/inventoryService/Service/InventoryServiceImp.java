package com.deepak.inventoryService.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deepak.inventoryService.DTO.InventoryReqDto;
import com.deepak.inventoryService.DTO.InventoryResDto;
import com.deepak.inventoryService.DTO.InventoryUpdateDto;
import com.deepak.inventoryService.DTO.InventoryUpdateId;
import com.deepak.inventoryService.Repository.InventoryRepository;
import com.deepak.inventoryService.entity.Inventory;
import com.deepak.inventoryService.kafka.InventoryCommitEvent;
import com.deepak.inventoryService.kafka.InventoryCommitItem;
import com.deepak.inventoryService.kafka.InventoryItemEvent;
import com.deepak.inventoryService.kafka.InventoryReserveEvent;
import com.deepak.inventoryService.kafka.InventroyEventProducer;
import com.deepak.inventoryService.kafka.OrderCreatedEvent;
import com.deepak.inventoryService.kafka.OrderItemEvent;
import com.deepak.inventoryService.kafka.OrderReservedEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InventoryServiceImp implements InventoryService {

	private final InventoryRepository invRepo;
	private final InventroyEventProducer inventroyEventProducer;

	// user by warehouse
	@Override
	public String createInventory(InventoryReqDto req) {

		// create entity class object
		Inventory inv = new Inventory();
		System.out.println(req.getSkuCode());
		inv.setSkuCode(req.getSkuCode());
		inv.setAvailableQuantity(req.getStockQuantity());
		inv.setReservedQuantity(0);
		inv.setCreatedAt(LocalDateTime.now());
		inv.setAvailablitiyStatus(req.getStockQuantity() > 0 ? "IN_STOCK" : "OUT_OF_STOCK");

		invRepo.save(inv);

		// return here response dto....

		// ....

		return "Inventory created successfully..";
	}

	// get inventory
	@Override
	public InventoryResDto getInventory(String skucode) {

		Inventory inventory = invRepo.findBySkuCode(skucode)
				.orElseThrow(() -> new RuntimeException("Inventory not found.."));

		// binding entity to dto
		InventoryResDto invresDto = new InventoryResDto();
		invresDto.setId(inventory.getId());
		invresDto.setSkuCode(inventory.getSkuCode());
		invresDto.setStockQuantity(inventory.getAvailableQuantity());
		invresDto.setAvailablityStatus(inventory.getAvailablitiyStatus());

		return invresDto;

	}

	// Warehouse Admin / update inventory....
	@Override
	public ResponseEntity<String> updateInventory(InventoryUpdateDto req) {
		Inventory inventory = invRepo.findBySkuCode(req.getSkuCode())
				.orElseThrow(() -> new RuntimeException("Inventory not found"));

		// update quantity.
		inventory.setAvailableQuantity(req.getStockQuantity());

		invRepo.save(inventory);
		return ResponseEntity.ok("Stock updated Successfully..");
	}

	// reduce stock Order Service
	@Override // we have skuCode to identify the inventory & quantity how much quantity would
				// be decreased..
	public ResponseEntity<String> reduceInventory(String skuCode, Integer quantity) {
		Inventory inv = invRepo.findBySkuCode(skuCode).orElseThrow(() -> new RuntimeException("inventory not found"));

		// check inventory available stock
		if (inv.getAvailableQuantity() < quantity) {
			return ResponseEntity.badRequest().body("bad request insufficient stock...");

		}

		// reduce quantity after stock available
		inv.setAvailableQuantity(inv.getAvailableQuantity() - quantity);

		invRepo.save(inv);
		return ResponseEntity.ok("Quantity reduce succesfully.... updated quantity : " + inv.getAvailableQuantity());
	}

	// reduce Stocks using kafka [COD]

	public void reduceStock(OrderCreatedEvent event) {

		for (OrderItemEvent item : event.getItems()) {

			Inventory inventory = invRepo.findBySkuCode(item.getSkuCode())
					.orElseThrow(() -> new RuntimeException("Inventory not found: " + item.getSkuCode()));

			// Check availability
			if (inventory.getAvailableQuantity() < item.getQuantity()) {

				throw new RuntimeException("Insufficient stock for SKU: " + item.getSkuCode());
			}

			// Reduce available stock
			inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.getQuantity());

			// Reserve stock
			inventory.setReservedQuantity(inventory.getReservedQuantity() + item.getQuantity());

			invRepo.save(inventory);
		}
		
		// Inventory successfully reserved
		
		OrderReservedEvent reservedEvent = new OrderReservedEvent();

		reservedEvent.setOrderId(event.getOrderId());
		reservedEvent.setStatus("RESERVED");

		inventroyEventProducer.publishOrderReserved(reservedEvent);
	}

	// kafka ONLINE
	@Override
	public void reserveStock(InventoryReserveEvent event) {

		for (InventoryItemEvent item : event.getItems()) {

			Inventory inventory = invRepo.findBySkuCode(item.getSkuCode())
					.orElseThrow(() -> new RuntimeException("Inventory not found for SKU: " + item.getSkuCode()));

			if (inventory.getAvailableQuantity() < item.getQuantity()) {

				throw new RuntimeException("Insufficient stock for SKU: " + item.getSkuCode());
			}

			inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.getQuantity());

			inventory.setReservedQuantity(inventory.getReservedQuantity() + item.getQuantity());

			invRepo.save(inventory);
		}

		// All items reserved successfully

		OrderReservedEvent reservedEvent = new OrderReservedEvent();

		reservedEvent.setOrderId(event.getOrderId());
		reservedEvent.setStatus("RESERVED");

		// ✅ Correct
		inventroyEventProducer.publishOrderReserved(reservedEvent);
	}

	// Admin
	@Override
	public List<InventoryResDto> getAllInventory() {
		List<Inventory> inventoryList = invRepo.findAll();

		List<InventoryResDto> list = inventoryList.stream().map((p) -> {
			InventoryResDto invResDto = new InventoryResDto();
			invResDto.setId(p.getId());
			invResDto.setSkuCode(p.getSkuCode());
			invResDto.setStockQuantity(p.getAvailableQuantity());
			invResDto.setAvailablityStatus(p.getAvailableQuantity() > 0 ? "In_stock" : "Out_of_Stock");

			return invResDto;

			// convert list of stream to List
		}).collect(Collectors.toList());

		return list;
	}

	@Override
	public String updateSku(String oldSku, String newSku) {

		// check inventory by oldsku
		Inventory inventory = invRepo.findBySkuCode(oldSku)
				.orElseThrow(() -> new RuntimeException("Inventory not found...."));

		// check inventory by newsku
		if (invRepo.existsBySkuCode(newSku)) {
			throw new RuntimeException("New SKU already exists");
		}

		// if newSku not present then set newSku place of old
		inventory.setSkuCode(newSku);

		invRepo.save(inventory);

		return "inventory skucode  updated succesfully ";
	}

	@Override
	public String updateskuById(InventoryUpdateId req) {

		// check inventory by id exist or not
		Inventory inventoryById = invRepo.findById(req.getId())
				.orElseThrow(() -> new RuntimeException(" inventory not found ..."));

		// check any skucode for this inventory present or not
		if (invRepo.existsBySkuCode(inventoryById.getSkuCode())) {
			new RuntimeException("Sku already exists...");

		}

		// set new skucode for inventory

		System.out.println(req.getNewSku());
		inventoryById.setSkuCode(req.getNewSku());

		invRepo.save(inventoryById);
		return "inventoryById updated successfully...";

	}

	@Override
	public List<InventoryResDto> getInventoryBySkucode(List<String> skuCodes) {

		List<InventoryResDto> inventoryList = new ArrayList<>();

		for (String sku : skuCodes) {

			Inventory inventory = invRepo.findBySkuCode(sku)
					.orElseThrow(() -> new RuntimeException("Inventory not found for SKU : " + sku));

			InventoryResDto dto = new InventoryResDto();

			dto.setId(inventory.getId());
			dto.setSkuCode(inventory.getSkuCode());
			dto.setStockQuantity(inventory.getAvailableQuantity());
			dto.setAvailablityStatus(inventory.getAvailableQuantity() > 0 ? "In_Stock" : "Out_Of_Stock");

			inventoryList.add(dto);
		}

		return inventoryList;
	}

	@Override
	@Transactional
	public void commitInventory(InventoryCommitEvent event) {

		for (InventoryCommitItem item : event.getItems()) {

			Inventory inventory = invRepo.findBySkuCode(item.getSkuCode())
					.orElseThrow(() -> new RuntimeException("Inventory not found for SKU: " + item.getSkuCode()));

			int quantity = item.getQuantity();

			// Safety check
			if (inventory.getReservedQuantity() < quantity) {

				throw new RuntimeException(
						"Reserved quantity is less than commit quantity for SKU: " + item.getSkuCode());
			}

			// Remove from reserved stock
			inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);

			invRepo.save(inventory);
		}

	}

}