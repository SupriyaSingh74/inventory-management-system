package com.inventory.service.impl;

import com.inventory.dto.CustomerDto;
import com.inventory.entity.Customer;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.CustomerRepository;
import com.inventory.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {

        logger.info("Creating Customer : {}", dto.getCustomerName());

        if (dto.getEmail() != null &&
                customerRepository.existsByEmail(dto.getEmail())) {

            logger.error("Email Already Exists : {}", dto.getEmail());

            throw new IllegalArgumentException("Email already exists");
        }

        if (dto.getPhone() != null &&
                customerRepository.existsByPhone(dto.getPhone())) {

            logger.error("Phone Already Exists : {}", dto.getPhone());

            throw new IllegalArgumentException("Phone already exists");
        }

        Customer customer = mapper.map(dto, Customer.class);

        Customer saved = customerRepository.save(customer);

        logger.info("Customer Created Successfully : {}", saved.getCustomerId());

        return mapper.map(saved, CustomerDto.class);
    }

    @Override
    public Page<CustomerDto> getAllCustomers(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching All Customers | Page : {} | Size : {} | SortBy : {} | Direction : {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CustomerDto> customers = customerRepository.findAll(pageable)
                .map(customer -> mapper.map(customer, CustomerDto.class));

        logger.info("Total Customers Found : {}", customers.getTotalElements());

        return customers;
    }

    @Override
    public CustomerDto getCustomerById(Long id) {

        logger.info("Fetching Customer Id : {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Customer Not Found : {}", id);

                    return new ResourceNotFoundException("Customer not found");
                });

        logger.info("Customer Found : {}", customer.getCustomerName());

        return mapper.map(customer, CustomerDto.class);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {

        logger.info("Updating Customer Id : {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Customer Not Found : {}", id);

                    return new ResourceNotFoundException("Customer not found");
                });

        if (dto.getEmail() != null
                && !dto.getEmail().equals(customer.getEmail())
                && customerRepository.existsByEmail(dto.getEmail())) {

            logger.error("Email Already Exists : {}", dto.getEmail());

            throw new IllegalArgumentException("Email already exists");
        }

        if (dto.getPhone() != null
                && !dto.getPhone().equals(customer.getPhone())
                && customerRepository.existsByPhone(dto.getPhone())) {

            logger.error("Phone Already Exists : {}", dto.getPhone());

            throw new IllegalArgumentException("Phone already exists");
        }

        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setGstNumber(dto.getGstNumber());

        Customer updated = customerRepository.save(customer);

        logger.info("Customer Updated Successfully : {}", updated.getCustomerId());

        return mapper.map(updated, CustomerDto.class);
    }

    @Override
    public void deleteCustomer(Long id) {

        logger.warn("Deleting Customer Id : {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Customer Not Found : {}", id);

                    return new ResourceNotFoundException("Customer not found");
                });

        customerRepository.delete(customer);

        logger.info("Customer Deleted Successfully : {}", id);
    }

    @Override
    public Page<CustomerDto> searchCustomer(
            String keyword,
            int page,
            int size) {

        logger.info("Searching Customer with Keyword : {}", keyword);

        Pageable pageable = PageRequest.of(page, size);

        Page<CustomerDto> customers = customerRepository
                .findByCustomerNameContainingIgnoreCase(keyword, pageable)
                .map(customer -> mapper.map(customer, CustomerDto.class));

        logger.info("Search Result Count : {}", customers.getTotalElements());

        return customers;
    }

}
