package com.authserve.authproduct.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authserve.authproduct.models.RoleEntity;
import com.authserve.authproduct.models.RoleEnum;


public interface RoleRepository  extends JpaRepository<RoleEntity, Long>{
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
