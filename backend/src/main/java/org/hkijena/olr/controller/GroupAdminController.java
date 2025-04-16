package org.hkijena.olr.controller;

import org.apache.coyote.BadRequestException;
import org.hkijena.olr.model.entities.Facility;
import org.hkijena.olr.model.entities.Group;
import org.hkijena.olr.model.entities.User;
import org.hkijena.olr.payloads.GroupPayload;
import org.hkijena.olr.payloads.UserPayload;
import org.hkijena.olr.repositories.FacilityRepository;
import org.hkijena.olr.repositories.GroupRepository;
import org.hkijena.olr.repositories.UserRepository;
import org.hkijena.olr.services.UserService;
import org.hkijena.olr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class GroupAdminController {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final FacilityRepository  facilityRepository;

    @Autowired
    public GroupAdminController(UserService userService, GroupRepository groupRepository, UserRepository userRepository, FacilityRepository facilityRepository) {
        this.userService = userService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
    }

    @GetMapping("/api/admin/list-groups")
    public ResponseEntity<List<GroupPayload>> listGroups(Authentication authentication) {
        userService.validateIsAdmin(authentication);
        List<GroupPayload> result = new ArrayList<>();
        for (var group : groupRepository.findAll()) {
            result.add(new GroupPayload(group));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/admin/create-group")
    public ResponseEntity<String> createGroup(Authentication authentication, @RequestBody GroupPayload groupPayload) {
        userService.validateIsAdmin(authentication);
        if (StringUtils.isNullOrEmpty(groupPayload.getName())) {
            throw new IllegalArgumentException("Name is null or empty");
        }
        Group group = new Group();
        group.setName(groupPayload.getName());
        group.setDescription(StringUtils.nullToEmpty(groupPayload.getDescription()));
        group = groupRepository.save(group);

        return ResponseEntity.ok().body("Created new group with ID " + group.getId());
    }

    @PostMapping("/api/admin/edit-group")
    public ResponseEntity<String> editGroup(Authentication authentication, @RequestBody GroupPayload groupPayload) {
        userService.validateIsAdmin(authentication);

        Optional<Group> group_ = groupRepository.findById(groupPayload.getId());
        if (group_.isPresent()) {
            Group group = group_.get();
            group.setName(StringUtils.orElse(groupPayload.getName(), "Unnamed group"));
            group.setDescription(StringUtils.nullToEmpty(groupPayload.getDescription()));
            group = groupRepository.save(group);

            return ResponseEntity.ok().body("Edited group with ID " + group.getId());
        } else {
            throw new IllegalArgumentException("Group does not exist");
        }
    }

    @PostMapping("/api/admin/delete-group")
    public ResponseEntity<String> deleteGroup(Authentication authentication, @RequestBody GroupPayload groupPayload) {
        userService.validateIsAdmin(authentication);

        Optional<Group> group_ = groupRepository.findById(groupPayload.getId());
        if (group_.isPresent()) {
            Group group = group_.get();

            // Delete from users
            for (User member : group.getMembers()) {
                group.removeMember(member);
                userRepository.save(member);
            }

            // Delete from facilities
            for (Facility facility : facilityRepository.findAll()) {
                facility.eraseGroup(group);
                facilityRepository.save(facility);
            }

            groupRepository.delete(group);

            return ResponseEntity.ok().body("Deleted group with ID " + group.getId());
        } else {
            throw new IllegalArgumentException("Group does not exist");
        }
    }
}
