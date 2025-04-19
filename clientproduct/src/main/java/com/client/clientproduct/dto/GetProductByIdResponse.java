package com.client.clientproduct.dto;


import com.client.clientproduct.models.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetProductByIdResponse {
    private Long id;
    private String name;
    private double price;
    private String category;

    public static GetProductByIdResponse toDto(ProductEntity entity) {
        return new GetProductByIdResponse(entity.getId(), entity.getName(), entity.getPrice(), entity.getCategory());
    }
}