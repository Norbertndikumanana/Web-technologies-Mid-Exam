package org.example.trade_bridge_26668.service;

import org.example.trade_bridge_26668.model.Location;
import org.example.trade_bridge_26668.model.LocationEnum;
import org.example.trade_bridge_26668.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    // Save location - demonstrates location saving
    @SuppressWarnings("null")
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
    
    // Get all locations
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    // Get location by ID
    public Location getLocationById(UUID id) {
        if (id == null) return null;
        return locationRepository.findById(id).orElse(null);
    }
    
    // Get provinces
    public List<Location> getProvinces() {
        return locationRepository.findByStructureType(LocationEnum.PROVINCE);
    }
    
    // Check if location exists by code - demonstrates existBy()
    public boolean existsByCode(String code) {
        return locationRepository.existsByStructureCode(code);
    }
}
