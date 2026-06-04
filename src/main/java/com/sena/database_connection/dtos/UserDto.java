package com.sena.database_connection.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private int age;

    private String phone;

    private Long profileId;

    private List<Long> roleIds;
}
