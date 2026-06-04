package com.sena.database_connection.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private String description;

    @JsonBackReference(value = "user-profile")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
