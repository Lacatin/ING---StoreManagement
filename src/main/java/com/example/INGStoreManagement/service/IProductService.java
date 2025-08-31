package com.example.INGStoreManagement.service;

import com.example.INGStoreManagement.dto.ProductDto;
import com.example.INGStoreManagement.dto.UpdateProductDto;

import java.util.List;

public interface IProductService {
    List<ProductDto> findAllProducts();
    ProductDto findProduct(String productId);
    ProductDto createProduct(ProductDto product);
    void deleteProduct(String id);
    ProductDto updateProduct(String productId, UpdateProductDto product);
}
