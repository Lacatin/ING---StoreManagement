package com.example.INGStoreManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Table(name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue()
    @UuidGenerator
    private UUID id;

    private String name;
    private Double price;
    private Integer quantity;
}
