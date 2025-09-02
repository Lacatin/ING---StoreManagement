package com.example.INGStoreManagement.service;

import com.example.INGStoreManagement.dto.UpdateProductDto;
import com.example.INGStoreManagement.entity.ProductEntity;
import com.example.INGStoreManagement.exception.ProductAlreadyExistsException;
import com.example.INGStoreManagement.exception.ProductNotFoundException;
import com.example.INGStoreManagement.mapper.ProductMapper;
import com.example.INGStoreManagement.repository.ProductRepository;
import com.example.INGStoreManagement.util.ProductUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productMapper, productRepository);
    }

    @Test
    public void whenFindAllProducts_thenReturnsAListOfProducts() {
        List<ProductEntity> products = List.of(
                ProductUtils.getRandomProductEntity(),
                ProductUtils.getRandomProductEntity(),
                ProductUtils.getRandomProductEntity(),
                ProductUtils.getRandomProductEntity()
        );

        when(productRepository.findAll()).thenReturn(products);

        var result = productService.findAllProducts();

        assertEquals(products.size(), result.size());
        assertEquals(productMapper.toDto(products), result);
    }

    @Test
    void whenFindProductExists_thenReturnDto() {
        ProductEntity product = ProductUtils.getRandomProductEntity();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        var result = productService.findProduct(product.getId().toString());

        assertEquals(product.getId().toString(), result.getId());
        assertEquals(product.getName(), result.getName());
    }

    @Test
    void whenFindProductNotExists_thenThrow() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        var result = assertThrows(ProductNotFoundException.class,
                        () -> productService.findProduct(id.toString()));

        assertEquals("Product with ID " + id + " was not found", result.getMessage());
    }

    @Test
    void whenCreateValidProduct_thenSaveAndReturnDto() {
        var entity = ProductUtils.getRandomProductEntity();
        var dto = productMapper.toDto(entity);
        entity.setId(UUID.randomUUID());

        when(productRepository.findByName(entity.getName())).thenReturn(Optional.empty());
        when(productRepository.save(any(ProductEntity.class))).thenReturn(entity);

        var result = productService.createProduct(dto);

        assertEquals(entity.getName(), result.getName());
        assertNotNull(result.getId());
    }

    @Test
    void whenCreateProductAlreadyExists_thenThrow() {
        var entity = ProductUtils.getRandomProductEntity();
        var dto = productMapper.toDto(entity);

        when(productRepository.findByName(entity.getName())).thenReturn(Optional.of(entity));

        var result = assertThrows(ProductAlreadyExistsException.class,
                        () -> productService.createProduct(dto));
        assertEquals("Product with name '" + entity.getName() + "' already exists", result.getMessage());
    }

    @Test
    void whenDeleteProduct_thenRDeleteCalled() {
        var entity = ProductUtils.getRandomProductEntity();

        when(productRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        productService.deleteProduct(entity.getId().toString());

        verify(productRepository, times(1)).delete(entity);
    }

    @Test
    void whenDeleteProductNotExists_thenThrow() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        var result = assertThrows(ProductNotFoundException.class,
                        () -> productService.deleteProduct(id.toString()));
        assertEquals("Product with ID " + id + " was not found", result.getMessage());
    }

    @Test
    void whenUpdateProductValid_thenSaveWithUpdatedFields() {
        var entity = ProductUtils.getRandomProductEntity();
        var updateDto = ProductUtils.getRandomUpdateProductDto();

        when(productRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(productRepository.findByName(updateDto.getName())).thenReturn(Optional.empty());
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));

        var result = productService.updateProduct(entity.getId().toString(), updateDto);

        assertEquals(updateDto.getName(), result.getName());
        assertEquals(updateDto.getPrice(), result.getPrice());
        assertEquals(updateDto.getQuantity(), result.getQuantity());
    }

    @Test
    void whenUpdateProductNameAlreadyExists_thenThrow() {
        var entity = ProductUtils.getRandomProductEntity();
        var updateDto = ProductUtils.getRandomUpdateProductDto();

        var conflictingEntity = ProductUtils.getRandomProductEntity();
        conflictingEntity.setName(updateDto.getName());

        when(productRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(productRepository.findByName(updateDto.getName())).thenReturn(Optional.of(conflictingEntity));

        var result = assertThrows(ProductAlreadyExistsException.class,
                        () -> productService.updateProduct(entity.getId().toString(), updateDto));

        assertEquals("Product with name '" + updateDto.getName() + "' already exists", result.getMessage());
    }

    @ParameterizedTest
    @MethodSource("updateProductProvider")
    public void whenUpdateOnlyOneProductParameter_thenOnlyOneParameterIsChanged(UpdateProductDto updateProductDto, ProductEntity expected) {
        when(productRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        if (updateProductDto.getName() != null) {
            when(productRepository.findByName(updateProductDto.getName()))
                    .thenReturn(Optional.empty());
        }

        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));

        var result = productService.updateProduct(expected.getId().toString(), updateProductDto);

        assertEquals(result.getId(), expected.getId().toString());
        assertEquals(result.getName(), expected.getName());
        assertEquals(result.getPrice(), expected.getPrice());
        assertEquals(result.getQuantity(), expected.getQuantity());
    }

    static Stream<Arguments> updateProductProvider() {
        var random = new Random();

        UpdateProductDto onlyName = new UpdateProductDto();
        onlyName.setName("NewName_" + random.nextInt());

        ProductEntity expectedNameResult = new ProductEntity();
        expectedNameResult.setId(UUID.randomUUID());
        expectedNameResult.setName(onlyName.getName());
        expectedNameResult.setPrice(random.nextDouble());
        expectedNameResult.setQuantity(random.nextInt());

        UpdateProductDto onlyPrice = new UpdateProductDto();
        onlyPrice.setPrice(random.nextDouble());

        ProductEntity expectedPriceResult = new ProductEntity();
        expectedPriceResult.setId(UUID.randomUUID());
        expectedPriceResult.setName("Name_" + random.nextInt());
        expectedPriceResult.setPrice(onlyPrice.getPrice());
        expectedPriceResult.setQuantity(random.nextInt());

        UpdateProductDto onlyQuantity = new UpdateProductDto();
        onlyQuantity.setQuantity(3);

        ProductEntity expectedQuantityResult = new ProductEntity();
        expectedQuantityResult.setId(UUID.randomUUID());
        expectedQuantityResult.setName("Name_" + random.nextInt());
        expectedQuantityResult.setPrice(onlyPrice.getPrice());
        expectedQuantityResult.setQuantity(onlyQuantity.getQuantity());

        return Stream.of(
                Arguments.of(onlyName, expectedNameResult),
                Arguments.of(onlyPrice, expectedPriceResult),
                Arguments.of(onlyQuantity, expectedQuantityResult)
        );
    }
}
