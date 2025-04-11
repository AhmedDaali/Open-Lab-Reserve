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

    // Constructors

    public Resource() {}

    public Resource(String specification, ResourceType type) {
        this.specification = specification;
        this.type = type;
    }

    // Getters and setters

    public Long getId() { return id; }

    public String getSpecification() { return specification; }

    public void setSpecification(String specification) { this.specification = specification; }

    public ResourceType getType() { return type; }

    public void setType(ResourceType type) { this.type = type; }
}
