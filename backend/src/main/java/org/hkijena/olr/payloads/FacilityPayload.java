package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hkijena.olr.model.entities.Facility;
import org.hkijena.olr.model.entities.Group;
import org.hkijena.olr.model.entities.PhysicalLab;

import java.util.HashSet;
import java.util.Set;

public class FacilityPayload {
    @JsonProperty
    private long id = -1;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private Set<Long> createBookingGroupIds = new HashSet<>();
    @JsonProperty
    private Set<Long> cancelBookingGroupIds = new HashSet<>();
    @JsonProperty
    private Set<Long> managerGroupIds = new HashSet<>();
    @JsonProperty
    private Set<Long> physicalLabIds = new HashSet<>();

    public FacilityPayload() {}

    public FacilityPayload(Facility facility) {
        this.id = facility.getId() != null ? facility.getId() : -1;
        this.name = facility.getName();
        this.description = facility.getDescription();
        for (Group group : facility.getCreateBookingGroups()) {
            if(group.getId() != null) {
                createBookingGroupIds.add(group.getId());
            }
        }
        for (Group group : facility.getCancelBookingGroups()) {
            if(group.getId() != null) {
                cancelBookingGroupIds.add(group.getId());
            }
        }
        for (Group group : facility.getManagerGroups()) {
            if(group.getId() != null) {
                managerGroupIds.add(group.getId());
            }
        }
        for (PhysicalLab physicalLab : facility.getPhysicalLabs()) {
            if(physicalLab.getId() != null) {
                physicalLabIds.add(physicalLab.getId());
            }
        }
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

    public Set<Long> getCreateBookingGroupIds() {
        return createBookingGroupIds;
    }

    public void setCreateBookingGroupIds(Set<Long> createBookingGroupIds) {
        this.createBookingGroupIds = createBookingGroupIds;
    }

    public Set<Long> getCancelBookingGroupIds() {
        return cancelBookingGroupIds;
    }

    public void setCancelBookingGroupIds(Set<Long> cancelBookingGroupIds) {
        this.cancelBookingGroupIds = cancelBookingGroupIds;
    }

    public Set<Long> getManagerGroupIds() {
        return managerGroupIds;
    }

    public void setManagerGroupIds(Set<Long> managerGroupIds) {
        this.managerGroupIds = managerGroupIds;
    }

    public Set<Long> getPhysicalLabIds() {
        return physicalLabIds;
    }

    public void setPhysicalLabIds(Set<Long> physicalLabIds) {
        this.physicalLabIds = physicalLabIds;
    }
}
