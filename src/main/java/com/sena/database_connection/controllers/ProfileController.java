package com.sena.database_connection.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.dtos.ProfileDto;
import com.sena.database_connection.entities.Profile;
import com.sena.database_connection.entities.User;
import com.sena.database_connection.services.ProfileService;
import com.sena.database_connection.services.UserService;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileDto>> getAll() {
        List<ProfileDto> profiles = profileService.obtenerTodos().stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getById(@PathVariable Long id) {
        return profileService.porId(id).map(p -> ResponseEntity.ok(mapToDto(p)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfileDto> create(@RequestBody ProfileDto body) {
        Profile profile = mapToEntity(body);
        Profile created = profileService.crear(profile);
        return ResponseEntity.created(URI.create("/api/profiles/" + created.getId())).body(mapToDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> update(@PathVariable Long id, @RequestBody ProfileDto body) {
        Profile profile = mapToEntity(body);
        Profile updated = profileService.actualizar(id, profile);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = profileService.eliminar(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private ProfileDto mapToDto(Profile profile) {
        ProfileDto dto = new ProfileDto();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setDescription(profile.getDescription());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
        }
        return dto;
    }

    private Profile mapToEntity(ProfileDto dto) {
        Profile profile = new Profile();
        profile.setUsername(dto.getUsername());
        profile.setDescription(dto.getDescription());
        if (dto.getUserId() != null) {
            java.util.Optional<User> userOpt = userService.porId(dto.getUserId());
            if (userOpt.isPresent()) {
                profile.setUser(userOpt.get());
            }
        }
        return profile;
    }
}
