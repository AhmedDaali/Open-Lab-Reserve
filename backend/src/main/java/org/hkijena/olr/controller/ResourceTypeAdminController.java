package org.hkijena.olr.controller;

import org.hkijena.olr.model.entities.PhysicalLab;
import org.hkijena.olr.model.entities.ResourceType;
import org.hkijena.olr.payloads.ResourceTypePayload;
import org.hkijena.olr.repositories.PhysicalLabRepository;
import org.hkijena.olr.repositories.ResourceTypeRepository;
import org.hkijena.olr.services.UserService;
import org.hkijena.olr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ResourceTypeAdminController {

    private final UserService userService;
    private final ResourceTypeRepository resourceTypeRepository;
    private final PhysicalLabRepository physicalLabRepository;

    @Autowired
    public ResourceTypeAdminController(UserService userService, ResourceTypeRepository resourceTypeRepository, PhysicalLabRepository physicalLabRepository) {
        this.userService = userService;
        this.resourceTypeRepository = resourceTypeRepository;
        this.physicalLabRepository = physicalLabRepository;
    }

    @GetMapping("/api/admin/list-resource-types")
    public ResponseEntity<List<ResourceTypePayload>> listResourceTypes() {
        List<ResourceTypePayload> result = new ArrayList<>();
        for (var resourceType : resourceTypeRepository.findAll()) {
            result.add(new ResourceTypePayload(resourceType));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/admin/create-resource-type")
    public ResponseEntity<String> createResourceType(Authentication authentication, @RequestBody ResourceTypePayload resourceTypePayload) {
        userService.validateIsAdmin(authentication);
        if (StringUtils.isNullOrEmpty(resourceTypePayload.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is null or empty");
        }
        ResourceType resourceType = new ResourceType();
        resourceType.setName(resourceTypePayload.getName());
        resourceType.setDescription(StringUtils.nullToEmpty(resourceTypePayload.getDescription()));
        resourceType.setIcon(StringUtils.orElse(resourceTypePayload.getIcon(), "fa-solid fa-tag"));
        resourceType = resourceTypeRepository.save(resourceType);

        return ResponseEntity.ok().body("Created new resource type with ID " + resourceType.getId());
    }

    @PostMapping("/api/admin/edit-resource-type")
    public ResponseEntity<String> editResourceType(Authentication authentication, @RequestBody ResourceTypePayload resourceTypePayload) {
        userService.validateIsAdmin(authentication);

        Optional<ResourceType> resourceType_ = resourceTypeRepository.findById(resourceTypePayload.getId());
        if (resourceType_.isPresent()) {
            ResourceType resourceType = resourceType_.get();
            resourceType.setName(StringUtils.orElse(resourceTypePayload.getName(), "Unnamed"));
            resourceType.setDescription(StringUtils.nullToEmpty(resourceTypePayload.getDescription()));
            resourceType.setIcon(StringUtils.orElse(resourceTypePayload.getIcon(), "fa-solid fa-tag"));
            resourceType = resourceTypeRepository.save(resourceType);

            return ResponseEntity.ok().body("Edited resource type with ID " + resourceType.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource type does not exist");
        }
    }

    @PostMapping("/api/admin/delete-resource-type")
    public ResponseEntity<String> deleteResourceType(Authentication authentication, @RequestBody ResourceTypePayload resourceTypePayload) {
        userService.validateIsAdmin(authentication);

        Optional<ResourceType> resourceType_ = resourceTypeRepository.findById(resourceTypePayload.getId());
        if (resourceType_.isPresent()) {
            ResourceType resourceType = resourceType_.get();

            // Delete from physical labs TODO
            for (PhysicalLab physicalLab : physicalLabRepository.findAll()) {
            }

            resourceTypeRepository.delete(resourceType);

            return ResponseEntity.ok().body("Deleted resource type with ID " + resourceType.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource type does not exist");
        }
    }
}
