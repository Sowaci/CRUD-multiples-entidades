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

import com.sena.database_connection.dtos.PostDto;
import com.sena.database_connection.entities.Post;
import com.sena.database_connection.entities.User;
import com.sena.database_connection.services.PostService;
import com.sena.database_connection.services.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll() {
        List<PostDto> posts = postService.obtenerTodos().stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable Long id) {
        return postService.porId(id).map(p -> ResponseEntity.ok(mapToDto(p)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostDto body) {
        Post post = mapToEntity(body);
        Post created = postService.crear(post);
        return ResponseEntity.created(URI.create("/api/posts/" + created.getId())).body(mapToDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto body) {
        Post post = mapToEntity(body);
        Post updated = postService.actualizar(id, post);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = postService.eliminar(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private PostDto mapToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setLikes(post.getLikes());
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getId());
        }
        return dto;
    }

    private Post mapToEntity(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setLikes(dto.getLikes());
        if (dto.getUserId() != null) {
            java.util.Optional<User> userOpt = userService.porId(dto.getUserId());
            if (userOpt.isPresent()) {
                post.setUser(userOpt.get());
            }
        }
        return post;
    }
}
