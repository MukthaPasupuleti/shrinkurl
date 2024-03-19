package com.mukku.shrinkurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mukku.shrinkurl.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
