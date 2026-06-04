package com.sena.database_connection.dtos;

import lombok.Data;

@Data
public class PostDto {

    private Long id;
    private String title;
    private String description;
    private Integer likes;
    private Long userId;
}
