package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hkijena.olr.model.entities.Group;

public class GroupPayload {
    @JsonProperty
    private long id = -1;
    @JsonProperty
    private String name = "";
    @JsonProperty
    private String description = "";

    public GroupPayload() {

    }

    public GroupPayload(Group group) {
        this.id = group.getId() != null ? group.getId() : -1;
        this.name = group.getName();
        this.description = group.getDescription();
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
}
