package com.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SupplierDto {

    private Long supplierId;

    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    @NotBlank(message = "Contact person is required")
    private String contactPerson;

    @Email(message = "Invalid email")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone must be 10 digits")
    private String phone;

    private String address;

    private String gstNumber;
}