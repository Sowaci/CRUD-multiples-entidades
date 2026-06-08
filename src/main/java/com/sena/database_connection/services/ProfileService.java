package com.sena.database_connection.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sena.database_connection.entities.Profile;
import com.sena.database_connection.repositories.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository repository;

    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public List<Profile> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Profile> porId(Long id) {
        return repository.findById(id);
    }

    public Profile crear(Profile profile) {
        return repository.save(profile);
    }

    public Profile actualizar(Long id, Profile profile) {
        Optional<Profile> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        Profile p = existing.get();
        p.setUsername(profile.getUsername());
        p.setDescription(profile.getDescription());
        if (profile.getUser() != null) {
            p.setUser(profile.getUser());
        }
        return repository.save(p);
    }

    public boolean eliminar(Long id) {
        Optional<Profile> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        repository.delete(existing.get());
        return true;
    }
}
