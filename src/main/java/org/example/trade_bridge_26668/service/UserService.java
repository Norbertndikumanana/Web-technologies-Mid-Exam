package org.example.trade_bridge_26668.service;

import org.example.trade_bridge_26668.model.User;
import org.example.trade_bridge_26668.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Save user
    @SuppressWarnings("null")
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    // Update user
    @SuppressWarnings("null")
    public User updateUser(UUID id, User user) {
        if (id == null || !userRepository.existsById(id)) return null;
        user.setUserId(id);
        return userRepository.save(user);
    }
    
    // Get all users with pagination and sorting
    public Page<User> getAllUsers(Pageable pageable) {
        if (pageable == null) throw new IllegalArgumentException("Pageable cannot be null");
        return userRepository.findAll(pageable);
    }
    
    // Get user by ID
    public User getUserById(UUID id) {
        if (id == null) return null;
        return userRepository.findById(id).orElse(null);
    }
    
    // Check if user exists by email - demonstrates existBy()
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // Get users by province code
    public List<User> getUsersByProvinceCode(String provinceCode) {
        return userRepository.findUsersByProvinceCode(provinceCode);
    }
    
    // Get users by province name
    public List<User> getUsersByProvinceName(String provinceName) {
        return userRepository.findUsersByProvinceName(provinceName);
    }
    
    // Get users by province ID
    public List<User> getUsersByProvinceId(UUID provinceId) {
        return userRepository.findUsersByProvinceId(provinceId);
    }
}
