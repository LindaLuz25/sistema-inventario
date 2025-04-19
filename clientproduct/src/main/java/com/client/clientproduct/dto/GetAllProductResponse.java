package com.client.clientproduct.dto;



import com.client.clientproduct.models.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GetAllProductResponse {
    private String name;
    private String description;

    public static GetAllProductResponse toGetAllProductResponse(ProductEntity product){
        return new GetAllProductResponse(product.getName(), product.getDescription());
    }
}
