package org.example.trade_bridge_26668.service;

import org.example.trade_bridge_26668.model.Review;
import org.example.trade_bridge_26668.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @SuppressWarnings("null")
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
    
    @SuppressWarnings("null")
    public Review updateReview(UUID id, Review review) {
        if (id == null || !reviewRepository.existsById(id)) return null;
        review.setReviewId(id);
        return reviewRepository.save(review);
    }
    
    public Page<Review> getAllReviews(Pageable pageable) {
        if (pageable == null) throw new IllegalArgumentException("Pageable cannot be null");
        return reviewRepository.findAll(pageable);
    }
    
    public Review getReviewById(UUID id) {
        if (id == null) return null;
        return reviewRepository.findById(id).orElse(null);
    }
}
