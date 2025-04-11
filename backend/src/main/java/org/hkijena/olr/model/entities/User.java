package org.hkijena.olr.model.entities;

import jakarta.persistence.*;
import org.hkijena.olr.utils.StringUtils;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(320)")
    private String email = "";

    @Column(name = "first_name", columnDefinition = "TEXT")
    private String firstName = "";

    @Column(name = "last_name", columnDefinition = "TEXT")
    private String lastName = "";

    @Column(name = "affiliation", columnDefinition = "TEXT")
    private String affiliation = "";

    @Column(name = "password", columnDefinition = "TEXT")
    private String password = "";

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.User;

    @Column(name = "allow_login")
    private Boolean allowLogin = true;

    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups = new HashSet<>();

    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
        group.getMembers().add(this);
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
        group.getMembers().remove(this);
    }

    public Boolean getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(Boolean allowLogin) {
        this.allowLogin = allowLogin;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public boolean isAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(boolean locked) {
        this.allowLogin = locked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return StringUtils.nullToEmpty(email);
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return StringUtils.nullToEmpty(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return StringUtils.nullToEmpty(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return StringUtils.nullToEmpty(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role != null ? role : Role.User;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        User,
        Guest,
        Admin
    }
}
