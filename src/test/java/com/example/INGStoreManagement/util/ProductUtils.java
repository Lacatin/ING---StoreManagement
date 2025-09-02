package com.example.INGStoreManagement.util;

import com.example.INGStoreManagement.dto.ProductDto;
import com.example.INGStoreManagement.dto.UpdateProductDto;
import com.example.INGStoreManagement.entity.ProductEntity;
import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.UUID;

@UtilityClass
public class ProductUtils {
    private static final Random RANDOM = new Random();

    public static ProductEntity getRandomProductEntity() {
        var id = String.valueOf(RANDOM.nextInt(10000, 99999));
        return ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("NAME_" + id)
                .price(RANDOM.nextDouble(1000))
                .quantity(RANDOM.nextInt(0, 100))
                .build();
    }

    public static ProductDto getRandomProductDtoWithoutId() {
        var id = String.valueOf(RANDOM.nextInt(10000, 99999));
        return ProductDto.builder()
                .name("NAME_" + id)
                .price(RANDOM.nextDouble(1000))
                .quantity(RANDOM.nextInt(0, 100))
                .build();
    }

    public static UpdateProductDto getRandomUpdateProductDto() {
        var id = String.valueOf(RANDOM.nextInt(10000, 99999));
        return UpdateProductDto.builder()
                .name("NAME_" + id)
                .price(RANDOM.nextDouble(1000))
                .quantity(RANDOM.nextInt(0, 100))
                .build();
    }
}
