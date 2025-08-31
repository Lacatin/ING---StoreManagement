package com.example.INGStoreManagement.mapper;

import com.example.INGStoreManagement.dto.ProductDto;
import com.example.INGStoreManagement.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity toEntity(ProductDto dto);
    ProductDto toDto(ProductEntity product);
}
