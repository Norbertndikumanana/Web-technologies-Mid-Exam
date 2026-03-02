package org.example.trade_bridge_26668.controller;

import org.example.trade_bridge_26668.dto.ApiResponse;
import org.example.trade_bridge_26668.model.Review;
import org.example.trade_bridge_26668.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Review>> saveReview(@RequestBody Review review) {
        Review saved = reviewService.saveReview(review);
        return ResponseEntity.ok(new ApiResponse<>("Review saved successfully", saved));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Review>> updateReview(@PathVariable UUID id, @RequestBody Review review) {
        Review updated = reviewService.updateReview(id, review);
        return updated != null 
            ? ResponseEntity.ok(new ApiResponse<>("Review updated successfully", updated))
            : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<Page<Review>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(reviewService.getAllReviews(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable UUID id) {
        Review review = reviewService.getReviewById(id);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable UUID id) {
        boolean deleted = reviewService.deleteReview(id);
        return deleted 
            ? ResponseEntity.ok(new ApiResponse<>("Review deleted successfully", null))
            : ResponseEntity.notFound().build();
    }
}
