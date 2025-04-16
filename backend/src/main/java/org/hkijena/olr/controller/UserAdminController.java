package org.hkijena.olr.controller;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import org.hkijena.olr.config.AccountConfig;
import org.hkijena.olr.model.entities.Group;
import org.hkijena.olr.model.entities.User;
import org.hkijena.olr.payloads.GroupPayload;
import org.hkijena.olr.payloads.UserPayload;
import org.hkijena.olr.repositories.GroupRepository;
import org.hkijena.olr.repositories.UserRepository;
import org.hkijena.olr.services.UserService;
import org.hkijena.olr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class UserAdminController {

    private final AccountConfig accountConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final GroupRepository groupRepository;

    @Autowired
    public UserAdminController(AccountConfig accountConfig, UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, GroupRepository groupRepository) {
        this.accountConfig = accountConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.groupRepository = groupRepository;
    }

    @GetMapping("/api/admin/list-users")
    public ResponseEntity<List<UserPayload>> listUsers(Authentication authentication) {
        userService.validateIsAdmin(authentication);
        List<UserPayload> result = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            result.add(new UserPayload(user));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/admin/edit-user")
    public ResponseEntity<String> editUser(Authentication authentication, @RequestBody UserPayload userPayload) {
        userService.validateIsAdmin(authentication);
        Optional<User> byId = userRepository.findById(userPayload.getId());
        if (byId.isPresent()) {

            // Fetch groups
            Set<Group> groupsWant = new HashSet<>();
            for (GroupPayload groupPayload : userPayload.getGroups()) {
                Optional<Group> group_ = groupRepository.findById(groupPayload.getId());
                if(group_.isPresent()) {
                    groupsWant.add(group_.get());
                }
                else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid group");
                }
            }

            User user = byId.get();
            if (!Objects.equals(user.getEmail(), userPayload.getEmail())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Inconsistent email. Cancelling!");
            }
            if (!StringUtils.isNullOrEmpty(userPayload.getNewPassword())) {
                if (!Objects.equals(userPayload.getNewPasswordConfirm(), userPayload.getNewPassword())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Inconsistent password confirmation.");
                }
                user.setPassword(passwordEncoder.encode(userPayload.getNewPassword()));
            }

            user.setAffiliation(userPayload.getAffiliation());
            user.setAllowLogin(userPayload.isAllowLogin());
            user.setFirstName(userPayload.getFirstName());
            user.setLastName(userPayload.getLastName());
            user.setRole(userPayload.getRole());

            // Edit group affiliations
            for (Group group : ImmutableList.copyOf(user.getGroups())) {
                if(!groupsWant.contains(group)) {
                    group.removeMember(user);
                    groupRepository.save(group);
                }
            }
            for (Group group : groupsWant) {
                if(!user.getGroups().contains(group)) {
                    group.addMember(user);
                    groupRepository.save(group);
                }
            }

            userRepository.save(user);

            return ResponseEntity.ok("User was successfully edited.");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
