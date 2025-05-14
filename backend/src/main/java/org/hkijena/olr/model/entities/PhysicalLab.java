package org.hkijena.olr.model.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "physical_labs")
public class PhysicalLab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Reference to owning Facility
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    // A physical laboratory can have multiple resources
    @OneToMany(mappedBy = "physicalLab", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resource> resources = new HashSet<>();

    // ----- Getters & Setters -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    // ----- Helper methods to synchronize both sides -----
    public void addResource(Resource resource) {
        resources.add(resource);
        resource.setPhysicalLab(this);
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
        resource.setPhysicalLab(null);
    }
}