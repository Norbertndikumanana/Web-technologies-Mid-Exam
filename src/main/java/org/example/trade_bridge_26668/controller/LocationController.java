package org.example.trade_bridge_26668.controller;

import org.example.trade_bridge_26668.dto.ApiResponse;
import org.example.trade_bridge_26668.model.Location;
import org.example.trade_bridge_26668.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    // Save location
    @PostMapping
    public ResponseEntity<ApiResponse<Location>> saveLocation(@RequestBody Location location) {
        Location saved = locationService.saveLocation(location);
        return ResponseEntity.ok(new ApiResponse<>("Location saved successfully", saved));
    }
    
    // Get all locations
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
    
    // Get location by ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable UUID id) {
        Location location = locationService.getLocationById(id);
        return location != null ? ResponseEntity.ok(location) : ResponseEntity.notFound().build();
    }
    
    // Get all provinces
    @GetMapping("/provinces")
    public ResponseEntity<List<Location>> getProvinces() {
        return ResponseEntity.ok(locationService.getProvinces());
    }
    
    // Check if location exists by code
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(locationService.existsByCode(code));
    }
}
