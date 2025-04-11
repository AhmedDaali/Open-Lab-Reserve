package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hkijena.olr.model.entities.ResourceType;

public class ResourceTypePayload {
    @JsonProperty
    private long id = -1;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private String icon;

    public ResourceTypePayload() {

    }

    public ResourceTypePayload(ResourceType resourceType) {
        this.id = resourceType.getId() != null ? resourceType.getId() : -1;
        this.name = resourceType.getName();
        this.description = resourceType.getDescription();
        this.icon = resourceType.getIcon();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
