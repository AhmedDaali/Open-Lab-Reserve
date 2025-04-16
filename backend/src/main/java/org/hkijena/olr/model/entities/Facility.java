package org.hkijena.olr.model.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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

    // One facility has many physical labs
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhysicalLab> physicalLabs = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Group> getCreateBookingGroups() {
        return createBookingGroups;
    }

    public void setCreateBookingGroups(Set<Group> createBookingGroups) {
        this.createBookingGroups = createBookingGroups;
    }

    public Set<Group> getCancelBookingGroups() {
        return cancelBookingGroups;
    }

    public void setCancelBookingGroups(Set<Group> cancelBookingGroups) {
        this.cancelBookingGroups = cancelBookingGroups;
    }

    public Set<PhysicalLab> getPhysicalLabs() {
        return physicalLabs;
    }

    public void setPhysicalLabs(Set<PhysicalLab> physicalLabs) {
        this.physicalLabs = physicalLabs;
    }

    public Set<Group> getManagerGroups() {
        return managerGroups;
    }

    public void setManagerGroups(Set<Group> managerGroups) {
        this.managerGroups = managerGroups;
    }

    public void addPhysicalLab(PhysicalLab lab) {
        physicalLabs.add(lab);
        lab.setFacility(this);
    }

    public void removePhysicalLab(PhysicalLab lab) {
        physicalLabs.remove(lab);
        lab.setFacility(null);
    }

    public void eraseGroup(Group group) {
        createBookingGroups.remove(group);
        cancelBookingGroups.remove(group);
        managerGroups.remove(group);
    }
}

