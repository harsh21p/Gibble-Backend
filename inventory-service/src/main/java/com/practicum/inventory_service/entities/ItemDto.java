package com.practicum.inventory_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String departmentId;
    private String regionId;
}
