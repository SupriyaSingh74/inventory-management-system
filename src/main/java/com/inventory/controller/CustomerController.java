package com.inventory.controller;

import com.inventory.dto.CustomerDto;
import com.inventory.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomerDto createCustomer(
            @Valid @RequestBody CustomerDto dto) {

        return customerService.createCustomer(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public Page<CustomerDto> getAllCustomers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "customerName") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        return customerService.getAllCustomers(
                page,
                size,
                sortBy,
                sortDir);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    public CustomerDto getCustomerById(
            @PathVariable Long id) {

        return customerService.getCustomerById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CustomerDto updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto dto) {

        return customerService.updateCustomer(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return "Customer Deleted Successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/search")
    public Page<CustomerDto> searchCustomer(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return customerService.searchCustomer(
                keyword,
                page,
                size);
    }
}