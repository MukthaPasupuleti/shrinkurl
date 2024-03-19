package com.mukku.shrinkurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mukku.shrinkurl.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
