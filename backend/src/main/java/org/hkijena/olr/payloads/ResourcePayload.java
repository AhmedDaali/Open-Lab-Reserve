package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hkijena.olr.model.entities.Resource;

public class ResourcePayload {
    @JsonProperty
    private long id = -1;
    @JsonProperty
    private String specification = "";
    @JsonProperty
    private ResourceTypePayload type = new ResourceTypePayload();

    public ResourcePayload() {
    }

    public ResourcePayload(Resource resource) {
        this.id = resource.getId();
        this.specification = resource.getSpecification();
        this.type = new ResourceTypePayload(resource.getType());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public ResourceTypePayload getType() {
        return type;
    }

    public void setType(ResourceTypePayload type) {
        this.type = type;
    }
}
