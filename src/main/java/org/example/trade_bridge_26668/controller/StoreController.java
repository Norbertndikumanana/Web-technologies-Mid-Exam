package org.example.trade_bridge_26668.controller;

import org.example.trade_bridge_26668.dto.ApiResponse;
import org.example.trade_bridge_26668.model.Store;
import org.example.trade_bridge_26668.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    
    @Autowired
    private StoreService storeService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Store>> saveStore(@RequestBody Store store) {
        Store saved = storeService.saveStore(store);
        return ResponseEntity.ok(new ApiResponse<>("Store saved successfully", saved));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Store>> updateStore(@PathVariable UUID id, @RequestBody Store store) {
        Store updated = storeService.updateStore(id, store);
        return updated != null 
            ? ResponseEntity.ok(new ApiResponse<>("Store updated successfully", updated))
            : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<Page<Store>> getAllStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(storeService.getAllStores(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable UUID id) {
        Store store = storeService.getStoreById(id);
        return store != null ? ResponseEntity.ok(store) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/exists/{storeName}")
    public ResponseEntity<Boolean> existsByStoreName(@PathVariable String storeName) {
        return ResponseEntity.ok(storeService.existsByStoreName(storeName));
    }
}
