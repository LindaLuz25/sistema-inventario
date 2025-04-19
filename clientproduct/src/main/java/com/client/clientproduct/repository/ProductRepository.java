package com.client.clientproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.client.clientproduct.models.ProductEntity;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    
}
