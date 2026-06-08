package com.sena.database_connection.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sena.database_connection.entities.User;
import com.sena.database_connection.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> obtenerTodos() {
        return this.repository.findAll();
    }

    public Optional<User> porId(Long id) {
        return this.repository.findById(id);
    }

    public User crear(User user) {
        return this.repository.save(user);
    }

    public User actualizar(User user) {

        Optional<User> userFound = this.porId(user.getId());

        if (userFound.isEmpty()) {
            return null;
        }

        return this.repository.save(user);
    }

    public User eliminar(Long id) {
        Optional<User> userFound = this.porId(id);

        if (userFound.isEmpty()) {
            return null;
        }
        this.repository.delete(userFound.get());
            return userFound.get();
    }

}