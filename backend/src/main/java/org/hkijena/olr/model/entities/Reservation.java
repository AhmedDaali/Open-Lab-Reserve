package org.hkijena.olr.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PhysicalLab physicalLab;

    @ManyToOne(optional = false)
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructors
    public Reservation() {
    }

    public Reservation(PhysicalLab physicalLab, User user, LocalDateTime startTime, LocalDateTime endTime) {
        this.physicalLab = physicalLab;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhysicalLab getPhysicalLab() {
        return physicalLab;
    }

    public void setPhysicalLab(PhysicalLab physicalLab) {
        this.physicalLab = physicalLab;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // ToString for debugging
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", physicalLab=" + physicalLab.getId() +
                ", user=" + user.getEmail() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
    
}
