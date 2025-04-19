package com.client.clientproduct.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.client.clientproduct.dto.GetAllProductResponse;
import com.client.clientproduct.dto.GetProductByIdResponse;
import com.client.clientproduct.models.ProductEntity;
import com.client.clientproduct.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/sistema/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    
    @Operation(summary = "Obtener todos los productos", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos recuperados correctamente")
    })
    @GetMapping
    public ResponseEntity<List<GetAllProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Obtener un producto por su id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto recuperado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetProductByIdResponse> getById(@PathVariable Long id) {
        var response = productService.getProductById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Operation(summary = "Registrar un Producto", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Producto no ha podido ser registrado")
    })
    @PostMapping
    public ResponseEntity<ProductEntity> create(@RequestBody ProductEntity entity) {

        try {
            productService.createProduct(entity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entity.getId())
                    .toUri();

            return ResponseEntity.created(location).body(entity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @Operation(summary = "Actualizar Producto", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no ha podido ser actualizado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> update(@PathVariable Long id, @RequestBody ProductEntity entity) {
        var response = productService.updateProduct(id, entity);

        return response != null ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Operation(summary = "Eliminar Producto", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no ha podido ser eliminado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean hasRemoved = productService.deleteProduct(id);

        return hasRemoved ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Obtener los productos por pagina", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos recuperados correctamente")
    })
    @GetMapping("/paging")
    public ResponseEntity<Page<ProductEntity>> getProductWithPaging(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "1") int pageSize,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok(productService.getAllPageable(pageNumber, pageSize, sortOrder));
    }

}
