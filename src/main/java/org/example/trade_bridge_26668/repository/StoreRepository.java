package org.example.trade_bridge_26668.repository;

import org.example.trade_bridge_26668.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    @NonNull
    Page<Store> findAll(@NonNull Pageable pageable);
    boolean existsByStoreName(String storeName);
}
