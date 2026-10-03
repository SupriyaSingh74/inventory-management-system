package com.inventory.repository;

import com.inventory.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Page<Customer> findByCustomerNameContainingIgnoreCase(
            String keyword,
            Pageable pageable);

}