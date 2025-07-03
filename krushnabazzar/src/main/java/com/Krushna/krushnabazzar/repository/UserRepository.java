package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email); // For login or user fetch by email
}
