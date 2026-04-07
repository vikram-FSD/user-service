package com.demo.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, UUID> {

    @Query(value = "SELECT id FROM roles WHERE name = ? ", nativeQuery = true)
    UUID getRoleIdByName(String roleName);

    @Query(value = "SELECT EXISTS(SELECT id FROM users WHERE email = ? ) ", nativeQuery = true)
    boolean emailExists(String email);

    @Query(value = "SELECT * FROM users WHERE email = ?", nativeQuery = true)
    Users getUserByEmail(String email);

}
