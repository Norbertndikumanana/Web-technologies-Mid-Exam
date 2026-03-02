package org.example.trade_bridge_26668.repository;

import org.example.trade_bridge_26668.model.Location;
import org.example.trade_bridge_26668.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    // existBy() method - checks if user exists by email
    boolean existsByEmail(String email);
    
    // Retrieve users by province code (includes all locations within the province)
    @Query("SELECT DISTINCT u FROM User u JOIN u.location loc " +
           "WHERE loc.structureCode = :provinceCode " +
           "OR EXISTS (SELECT 1 FROM Location l WHERE l = loc AND " +
           "(l.parent.structureCode = :provinceCode OR " +
           "l.parent.parent.structureCode = :provinceCode OR " +
           "l.parent.parent.parent.structureCode = :provinceCode OR " +
           "l.parent.parent.parent.parent.structureCode = :provinceCode))")
    List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);
    
    // Retrieve users by province name (includes all locations within the province)
    @Query("SELECT DISTINCT u FROM User u JOIN u.location loc " +
           "WHERE loc.structureName = :provinceName " +
           "OR EXISTS (SELECT 1 FROM Location l WHERE l = loc AND " +
           "(l.parent.structureName = :provinceName OR " +
           "l.parent.parent.structureName = :provinceName OR " +
           "l.parent.parent.parent.structureName = :provinceName OR " +
           "l.parent.parent.parent.parent.structureName = :provinceName))")
    List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);
    
    // Retrieve users by province ID (includes all locations within the province)
    @Query("SELECT DISTINCT u FROM User u JOIN u.location loc " +
           "WHERE loc.structureId = :provinceId " +
           "OR EXISTS (SELECT 1 FROM Location l WHERE l = loc AND " +
           "(l.parent.structureId = :provinceId OR " +
           "l.parent.parent.structureId = :provinceId OR " +
           "l.parent.parent.parent.structureId = :provinceId OR " +
           "l.parent.parent.parent.parent.structureId = :provinceId))")
    List<User> findUsersByProvinceId(@Param("provinceId") UUID provinceId);
    
    // Pagination and sorting support
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
    
    // Find users by location
    List<User> findByLocation(Location location);
}
