package com.inventory.service;

import com.inventory.dto.SupplierDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierService {

    SupplierDto createSupplier(SupplierDto supplierDto);

    Page<SupplierDto> getAllSuppliers(
            int page,
            int size,
            String sortBy,
            String sortDir);

    SupplierDto getSupplierById(Long id);

    SupplierDto updateSupplier(Long id, SupplierDto supplierDto);

    void deleteSupplier(Long id);


    Page<SupplierDto> searchSupplier(
            String keyword,
            int page,
            int size);

}