package org.example.trade_bridge_26668.repository;

import org.example.trade_bridge_26668.model.Location;
import org.example.trade_bridge_26668.model.LocationEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByStructureCodeAndStructureType(String code, LocationEnum type);
    List<Location> findByStructureType(LocationEnum type);
    boolean existsByStructureCode(String code);
}
