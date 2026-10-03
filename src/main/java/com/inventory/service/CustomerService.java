package com.inventory.service;

import com.inventory.dto.CustomerDto;
import org.springframework.data.domain.Page;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto dto);

    Page<CustomerDto> getAllCustomers(
            int page,
            int size,
            String sortBy,
            String sortDir);

    CustomerDto getCustomerById(Long id);

    CustomerDto updateCustomer(Long id,
                               CustomerDto dto);

    void deleteCustomer(Long id);

    Page<CustomerDto> searchCustomer(
            String keyword,
            int page,
            int size);

}