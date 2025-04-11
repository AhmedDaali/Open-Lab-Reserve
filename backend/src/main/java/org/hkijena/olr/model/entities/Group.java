package org.hkijena.olr.model.entities;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, columnDefinition = "TEXT")
    private String name = "";

    @Column(name = "description", unique = true, columnDefinition = "TEXT")
    private String description = "";

    @ManyToMany(mappedBy = "groups")
    private Set<User> members = new HashSet<>();

    public Set<User> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public void addUser(User user) {
        this.members.add(user);
        user.getGroups().add(this);
    }

    public void removeUser(User user) {
        this.members.remove(user);
        user.getGroups().remove(this);
    }

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
}
