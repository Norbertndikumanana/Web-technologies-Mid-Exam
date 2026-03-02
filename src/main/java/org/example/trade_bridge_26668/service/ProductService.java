package org.example.trade_bridge_26668.service;

import org.example.trade_bridge_26668.model.Product;
import org.example.trade_bridge_26668.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @SuppressWarnings("null")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    @SuppressWarnings("null")
    public Product updateProduct(UUID id, Product product) {
        if (id == null || !productRepository.existsById(id)) return null;
        product.setProductId(id);
        return productRepository.save(product);
    }
    
    public Page<Product> getAllProducts(Pageable pageable) {
        if (pageable == null) throw new IllegalArgumentException("Pageable cannot be null");
        return productRepository.findAll(pageable);
    }
    
    public Product getProductById(UUID id) {
        if (id == null) return null;
        return productRepository.findById(id).orElse(null);
    }
    
    public boolean existsByProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }
}
