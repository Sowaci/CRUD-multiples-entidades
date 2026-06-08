package com.sena.database_connection.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sena.database_connection.entities.Post;
import com.sena.database_connection.repositories.PostRepository;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Post> porId(Long id) {
        return repository.findById(id);
    }

    public List<Post> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Post crear(Post post) {
        return repository.save(post);
    }

    public Post actualizar(Long id, Post post) {
        Optional<Post> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        Post p = existing.get();
        p.setTitle(post.getTitle());
        p.setDescription(post.getDescription());
        p.setLikes(post.getLikes());
        if (post.getUser() != null) {
            p.setUser(post.getUser());
        }
        return repository.save(p);
    }

    public boolean eliminar(Long id) {
        Optional<Post> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        repository.delete(existing.get());
        return true;
    }
}
