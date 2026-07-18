package com.deepak.inventoryService.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.inventoryService.DTO.InventoryReqDto;
import com.deepak.inventoryService.DTO.InventoryResDto;
import com.deepak.inventoryService.DTO.InventoryUpdateDto;
import com.deepak.inventoryService.DTO.InventoryUpdateId;
import com.deepak.inventoryService.DTO.InventoryUpdateSku;
import com.deepak.inventoryService.Service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class InventryController {

	private final InventoryService inventoryService;

	// create inventory api
	@PostMapping("/create")
	public ResponseEntity<String> createInventory(@RequestBody InventoryReqDto request) {
			System.out.println(request.getSkuCode());
		 String inventory = inventoryService.createInventory(request);

		return ResponseEntity.ok().body(inventory);
	}

	// get inventory api
	@GetMapping("/getstock/{skuCode}")
	public InventoryResDto getInventorystock(@PathVariable String skuCode) {

		return inventoryService.getInventory(skuCode);
	}

	// update api
	@PutMapping("/update")
	public ResponseEntity<String> updateInventory(@RequestBody InventoryUpdateDto req) {

		inventoryService.updateInventory(req);

		return ResponseEntity.ok("Inventory Updated");
	}

	// reduce api..

	@PutMapping("/reduce/{skuCode}")
	public ResponseEntity<String> reduceInventory(@PathVariable String skuCode, @RequestParam Integer quantity) {

		inventoryService.reduceInventory(skuCode, quantity);

		return ResponseEntity.ok("Inventory Reduced");
	}

	// get all inventory

	@GetMapping("/getAllStocks")
	public ResponseEntity<List<InventoryResDto>> getAllInventory() {
		List<InventoryResDto> allInventory = inventoryService.getAllInventory();
		return ResponseEntity.ok().body(allInventory);
	}
	
	
	
	//Note api only for ADMIN....
	
	
	// skucode update for inventory (only access to admin account )
	@PutMapping("/updatesku")
	public ResponseEntity<String> updateSku(@RequestBody InventoryUpdateSku  req) {

			String updateSku = inventoryService.updateSku(req.getOldSku(), req.getNewSku());
			
			return ResponseEntity.ok(updateSku);
	}
	
	@PutMapping("/updateskuByid")
	public ResponseEntity<String> updateSkuById(@RequestBody InventoryUpdateId  req) {

			String updateskuById = inventoryService.updateskuById(req);
			
			return ResponseEntity.ok().body(updateskuById);
	}
	
	@PostMapping("/getInventories")
	public ResponseEntity<List<InventoryResDto>> getInventories(@RequestBody List<String> skucodes) {
		List<InventoryResDto> allInventory = inventoryService.getInventoryBySkucode(skucodes);
		return ResponseEntity.ok().body(allInventory);
	}
	
}
