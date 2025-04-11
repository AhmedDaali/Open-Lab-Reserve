package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hkijena.olr.model.entities.Group;
import org.hkijena.olr.model.entities.PhysicalLab;

import java.util.HashSet;
import java.util.Set;

public class PhysicalLabPayload {
    @JsonProperty
    private long id = -1;
    @JsonProperty
    private String name;
    @JsonProperty
    private long facilityId;
    @JsonProperty
    private Set<Long> createBookingGroupIds = new HashSet<>();
    @JsonProperty
    private Set<Long> cancelBookingGroupIds = new HashSet<>();
    @JsonProperty
    private Set<Long> managerGroupIds = new HashSet<>();

    public PhysicalLabPayload() {
    }

    public PhysicalLabPayload(PhysicalLab physicalLab) {
        this.id = physicalLab.getId() != null ? physicalLab.getId() : -1;
        this.name = physicalLab.getName();
        this.facilityId = physicalLab.getFacility() != null ? (physicalLab.getFacility().getId() != null ? physicalLab.getFacility().getId() : -1) : -1;
        for (Group group : physicalLab.getCreateBookingGroups()) {
            if(group.getId() != null) {
                createBookingGroupIds.add(group.getId());
            }
        }
        for (Group group : physicalLab.getCancelBookingGroups()) {
            if(group.getId() != null) {
                cancelBookingGroupIds.add(group.getId());
            }
        }
        for (Group group : physicalLab.getManagerGroups()) {
            if(group.getId() != null) {
                managerGroupIds.add(group.getId());
            }
        }

    }
}
