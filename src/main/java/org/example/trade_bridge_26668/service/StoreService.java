package org.example.trade_bridge_26668.service;

import org.example.trade_bridge_26668.model.Store;
import org.example.trade_bridge_26668.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;
    
    public Store saveStore(Store store) {
        if (store == null) throw new IllegalArgumentException("Store cannot be null");
        return storeRepository.save(store);
    }
    
    public Store updateStore(UUID id, Store store) {
        if (id == null || !storeRepository.existsById(id)) return null;
        store.setStoreId(id);
        return storeRepository.save(store);
    }
    
    public Page<Store> getAllStores(Pageable pageable) {
        if (pageable == null) throw new IllegalArgumentException("Pageable cannot be null");
        return storeRepository.findAll(pageable);
    }
    
    public Store getStoreById(UUID id) {
        if (id == null) return null;
        return storeRepository.findById(id).orElse(null);
    }
    
    public boolean existsByStoreName(String storeName) {
        return storeRepository.existsByStoreName(storeName);
    }
}
