package org.example.trade_bridge_26668.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "administrative_structure")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID structureId;
    
    private String structureCode;
    private String structureName;
    
    @ManyToOne
    @JoinColumn(name="parent_id")
    @JsonIgnore
    private Location parent;
    
    @Enumerated(EnumType.STRING)
    private LocationEnum structureType;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> user;

    public Location() {}

    public Location(UUID structureId, String structureCode, String structureName, Location parent, LocationEnum structureType) {
        this.structureId = structureId;
        this.structureCode = structureCode;
        this.structureName = structureName;
        this.parent = parent;
        this.structureType = structureType;
    }

    public UUID getStructureId() {
        return structureId;
    }

    public void setStructureId(UUID structureId) {
        this.structureId = structureId;
    }

    public String getStructureCode() {
        return structureCode;
    }

    public void setStructureCode(String structureCode) {
        this.structureCode = structureCode;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public LocationEnum getStructureType() {
        return structureType;
    }

    public void setStructureType(LocationEnum structureType) {
        this.structureType = structureType;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
