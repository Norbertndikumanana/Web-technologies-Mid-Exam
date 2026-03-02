package org.example.trade_bridge_26668.controller;

import org.example.trade_bridge_26668.dto.ApiResponse;
import org.example.trade_bridge_26668.model.User;
import org.example.trade_bridge_26668.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Save user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> saveUser(@RequestBody User user) {
        User saved = userService.saveUser(user);
        return ResponseEntity.ok(new ApiResponse<>("User saved successfully", saved));
    }
    
    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID id, @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return updated != null 
            ? ResponseEntity.ok(new ApiResponse<>("User updated successfully", updated))
            : ResponseEntity.notFound().build();
    }
    
    // Get all users with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    
    // Check if user exists by email
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
    
    // Get users by province code
    @GetMapping("/province/code/{provinceCode}")
    public ResponseEntity<List<User>> getUsersByProvinceCode(@PathVariable String provinceCode) {
        return ResponseEntity.ok(userService.getUsersByProvinceCode(provinceCode));
    }
    
    // Get users by province name
    @GetMapping("/province/name/{provinceName}")
    public ResponseEntity<List<User>> getUsersByProvinceName(@PathVariable String provinceName) {
        return ResponseEntity.ok(userService.getUsersByProvinceName(provinceName));
    }
    
    // Get users by province ID
    @GetMapping("/province/id/{provinceId}")
    public ResponseEntity<List<User>> getUsersByProvinceId(@PathVariable UUID provinceId) {
        return ResponseEntity.ok(userService.getUsersByProvinceId(provinceId));
    }
}
