package org.example.trade_bridge_26668.repository;

import org.example.trade_bridge_26668.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    @NonNull
    Page<Review> findAll(@NonNull Pageable pageable);
}
