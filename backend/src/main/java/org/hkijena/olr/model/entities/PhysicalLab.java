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

    // Permissions
    @ManyToMany
    @JoinTable(
            name = "lab_booking_group",
            joinColumns = @JoinColumn(name = "lab_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> createBookingGroups = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "lab_cancel_group",
            joinColumns = @JoinColumn(name = "lab_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> cancelBookingGroups = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "lab_manage_group",
            joinColumns = @JoinColumn(name = "lab_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> managerGroups = new HashSet<>();

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

    public Set<Group> getCreateBookingGroups() {
        return createBookingGroups;
    }

    public void setCreateBookingGroups(Set<Group> bookingGroups) {
        this.createBookingGroups = bookingGroups;
    }

    public Set<Group> getCancelBookingGroups() {
        return cancelBookingGroups;
    }

    public void setCancelBookingGroups(Set<Group> cancelGroups) {
        this.cancelBookingGroups = cancelGroups;
    }

    public Set<Group> getManagerGroups() {
        return managerGroups;
    }

    public void setManagerGroups(Set<Group> managerGroups) {
        this.managerGroups = managerGroups;
    }
}