package org.hkijena.olr.controller;

import org.hkijena.olr.config.AccountConfig;
import org.hkijena.olr.model.entities.User;
import org.hkijena.olr.payloads.UserPayload;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class UserAdminController {

    private final AccountConfig accountConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public UserAdminController(AccountConfig accountConfig, UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.accountConfig = accountConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
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
            userRepository.save(user);

            return ResponseEntity.ok("User was successfully edited.");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
