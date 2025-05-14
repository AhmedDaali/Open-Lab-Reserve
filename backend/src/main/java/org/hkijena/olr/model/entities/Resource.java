package org.hkijena.olr.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String specification;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private ResourceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private PhysicalLab physicalLab;

    // Constructors

    public Resource() {}

    public Resource(String specification, ResourceType type, PhysicalLab physicalLab) {
        this.specification = specification;
        this.type = type;
        this.physicalLab = physicalLab;
    }

    // Getters and setters

    public Long getId() { return id; }

    public String getSpecification() { return specification; }

    public void setSpecification(String specification) { this.specification = specification; }

    public ResourceType getType() { return type; }

    public void setType(ResourceType type) { this.type = type; }

    public PhysicalLab getPhysicalLab() {
        return physicalLab;
    }

    public void setPhysicalLab(PhysicalLab physicalLab) {
        this.physicalLab = physicalLab;
    }
}
