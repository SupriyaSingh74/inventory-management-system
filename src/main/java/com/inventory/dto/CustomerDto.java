package com.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDto {

    private Long customerId;

    @NotBlank(message = "Customer Name is required")
    private String customerName;

    @Email(message = "Invalid Email")
    private String email;

    private String phone;

    private String address;

    private String gstNumber;

}