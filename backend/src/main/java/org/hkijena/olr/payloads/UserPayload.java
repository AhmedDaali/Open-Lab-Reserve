package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.hkijena.olr.model.entities.Group;
import org.hkijena.olr.model.entities.User;

import java.util.HashSet;
import java.util.Set;

public class UserPayload {

    private long id = -1;

    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String affiliation = "";
    private String newPassword;
    private String newPasswordConfirm;
    private boolean allowLogin = true;
    private User.Role role = User.Role.User;
    private Set<GroupPayload> groups = new HashSet<>();

    public UserPayload() {

    }

    public UserPayload(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.affiliation = user.getAffiliation();
        this.allowLogin = user.isAllowLogin();
        this.role = user.getRole();
        for (Group group : user.getGroups()) {
            this.groups.add(new GroupPayload(group));
        }
    }

    @JsonGetter("groups")
    public Set<GroupPayload> getGroups() {
        return groups;
    }

    @JsonSetter("groups")
    public void setGroups(Set<GroupPayload> groups) {
        this.groups = groups;
    }

    @JsonGetter("affiliation")
    public String getAffiliation() {
        return affiliation;
    }

    @JsonSetter("affiliation")
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @JsonGetter("allowLogin")
    public boolean isAllowLogin() {
        return allowLogin;
    }

    @JsonSetter("allowLogin")
    public void setAllowLogin(boolean allowLogin) {
        this.allowLogin = allowLogin;
    }

    @JsonGetter("email")
    public String getEmail() {
        return email;
    }

    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonGetter("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonSetter("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonGetter("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonSetter("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonGetter("newPassword")
    public String getNewPassword() {
        return newPassword;
    }

    @JsonSetter("newPassword")
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @JsonGetter("newPasswordConfirm")
    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    @JsonSetter("newPasswordConfirm")
    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonGetter("role")
    public User.Role getRole() {
        return role;
    }

    @JsonSetter("role")
    public void setRole(User.Role role) {
        this.role = role;
    }
}
