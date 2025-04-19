package com.authserve.authproduct.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authserve.authproduct.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long>{
    Optional<UserEntity> findByUsername(String username);
}
