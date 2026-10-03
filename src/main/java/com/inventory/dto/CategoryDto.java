package com.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {

    private Long categoryId;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100)
    private String categoryName;

    @Size(max = 500)
    private String description;
}