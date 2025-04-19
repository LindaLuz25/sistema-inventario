package com.client.clientproduct.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.client.clientproduct.dto.GetAllProductResponse;
import com.client.clientproduct.dto.GetProductByIdResponse;
import com.client.clientproduct.models.ProductEntity;
import com.client.clientproduct.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @Transactional
    public ProductEntity updateProduct(Long id,ProductEntity product) {

        var response = productRepository.findById(id).orElse(null);

        if (response == null) {
            return null;
        }
        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setPrice(product.getPrice());
        product.setStock(product.getStock());
        product.setCategory(product.getCategory());
    

        productRepository.saveAndFlush(response);

        return response;
    }

    public GetProductByIdResponse getProductById(Long id) {
        var response = productRepository.findById(id).orElse(null);
        if (response == null) {
            return null;
        }
        return GetProductByIdResponse.toDto(response);
    }

    public List<GetAllProductResponse> getAllProducts(){
        return productRepository.findAll().stream()
                .map(GetAllProductResponse::toGetAllProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        var response = productRepository.findById(id).orElse(null);
        if (response == null) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
    public Page<ProductEntity> getAllPageable(int pageNumber, int pageSize, String sortOrder) {
        Sort sorting = Sort.by(sortOrder.equals("asc") ? Direction.ASC : Direction.DESC, "id");

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);

        return productRepository.findAll(pageable);
    }



}
