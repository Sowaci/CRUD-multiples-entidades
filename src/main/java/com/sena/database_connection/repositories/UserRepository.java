package com.sena.database_connection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.database_connection.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	java.util.Optional<User> findByEmail(String email);
}