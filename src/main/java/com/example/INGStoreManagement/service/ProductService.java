package com.example.INGStoreManagement.service;

import com.example.INGStoreManagement.dto.ProductDto;
import com.example.INGStoreManagement.dto.UpdateProductDto;
import com.example.INGStoreManagement.entity.ProductEntity;
import com.example.INGStoreManagement.exception.ProductAlreadyExistsException;
import com.example.INGStoreManagement.exception.ProductNotFoundException;
import com.example.INGStoreManagement.mapper.ProductMapper;
import com.example.INGStoreManagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findProduct(String productId) {
        Assert.notNull(productId, "ID must not be null!");

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Assert.notNull(productDto, "Product DTO must not be null!");
        Assert.hasText(productDto.getName(), "Product name must not be empty!");
        Assert.isTrue(productDto.getPrice() > 0, "Price must be greater than 0!");
        Assert.isTrue(productDto.getQuantity() >= 0, "Quantity cannot be negative!");

        var existingProduct = productRepository.findByName(productDto.getName());
        if (existingProduct.isPresent()) throw new ProductAlreadyExistsException(productDto.getName());

        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String productId, UpdateProductDto updateProductDto) {
        Assert.notNull(productId, "ID must not be null!");

        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        /*
           Checking if updating the name.
          If this is the case, making sure that no other product has the same name.
         */
        Optional.ofNullable(updateProductDto.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(newName -> {
                    productRepository
                            .findByName(newName)
                            .filter(product -> !product.getId().equals(productId))
                            .ifPresent(product -> {throw new ProductAlreadyExistsException(newName); });

                    existingProduct.setName(newName);
                });

        Optional.ofNullable(updateProductDto.getPrice())
                .filter(price -> price >= 0)
                .ifPresent(existingProduct::setPrice);

        Optional.ofNullable(updateProductDto.getQuantity())
                .filter(qty -> qty >= 0)
                .ifPresent(existingProduct::setQuantity);

        return productMapper.toDto(productRepository.save(existingProduct));
    }
}
