package com.sena.database_connection.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sena.database_connection.entities.Role;
import com.sena.database_connection.repositories.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Role> porId(Long id) {
        return repository.findById(id);
    }

    public Role create(Role role) {
        return repository.save(role);
    }

    public Role actualizar(Long id, Role role) {
        Optional<Role> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        Role r = existing.get();
        r.setName(role.getName());
        return repository.save(r);
    }

    public boolean eliminar(Long id) {
        Optional<Role> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        repository.delete(existing.get());
        return true;
    }
}
