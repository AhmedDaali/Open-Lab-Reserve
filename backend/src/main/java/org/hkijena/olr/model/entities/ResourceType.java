package org.hkijena.olr.model.entities;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "resource_types")
public class ResourceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(320)")
    private String icon;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Resource> resources = new HashSet<>();

    public ResourceType() {}

    public ResourceType(String name, String description, String icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }

    public void setIcon(String icon) { this.icon = icon; }

    public Set<Resource> getResources() { return Collections.unmodifiableSet(resources); }

    public void addResource(Resource resource) {
        resource.setType(this);
        resources.add(resource);
    }

    public void removeResource(Resource resource) {
        resource.setType(null);
        resources.remove(resource);
    }
}

