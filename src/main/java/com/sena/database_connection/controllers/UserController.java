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

import com.sena.database_connection.dtos.UserDto;
import com.sena.database_connection.entities.Role;
import com.sena.database_connection.entities.User;
import com.sena.database_connection.services.RoleService;
import com.sena.database_connection.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> users = userService.obtenerTodos().stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return userService.porId(id).map(u -> ResponseEntity.ok(mapToDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto body) {
        // validate roles exist
        if (body.getRoleIds() != null) {
            for (Long roleId : body.getRoleIds()) {
                if (roleService.findById(roleId).isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        User user = mapToEntity(body);
        User created = userService.crear(user);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(mapToDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto body) {
        User user = mapToEntity(body);
        User updated = userService.actualizar(user);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.eliminar(id) != null;
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setPhone(user.getPhone());

        if (user.getProfile() != null) {
            dto.setProfileId(user.getProfile().getId());
        }

        if (user.getRoles() != null) {
            dto.setRoleIds(user.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        }

        return dto;
    }

    private User mapToEntity(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setPhone(dto.getPhone());

        if (dto.getRoleIds() != null) {
            List<Role> roles = dto.getRoleIds().stream()
                    .map(roleId -> roleService.findById(roleId).orElse(null))
                    .filter(r -> r != null)
                    .collect(Collectors.toList());
            user.setRoles(roles);
        }

        return user;
    }
}
