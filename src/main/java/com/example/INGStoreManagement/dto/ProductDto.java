package com.example.INGStoreManagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String id;

    @NotNull(message = "Name must not be null!")
    private String name;

    @NotNull(message = "Price must not be null!")
    @Min(value = 0, message = "Price must be at least 0!")
    private Double price;

    @NotNull(message = "Quantity must not be null!")
    @Min(value = 0, message = "Quantity must be at least 0!")
    private Integer quantity;
}
