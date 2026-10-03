package com.inventory.service.impl;

import com.inventory.dto.SupplierDto;
import com.inventory.entity.Supplier;
import com.inventory.exception.DuplicateResourceException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.SupplierRepository;
import com.inventory.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;
    private final ModelMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Override
    public SupplierDto createSupplier(SupplierDto dto) {

        logger.info("Creating Supplier : {}", dto.getSupplierName());

        if (repository.existsByEmail(dto.getEmail())) {

            logger.error("Email Already Exists : {}", dto.getEmail());

            throw new DuplicateResourceException("Email already exists");
        }

        if (repository.existsByPhone(dto.getPhone())) {

            logger.error("Phone Already Exists : {}", dto.getPhone());

            throw new DuplicateResourceException("Phone already exists");
        }

        if (dto.getGstNumber() != null &&
                repository.existsByGstNumber(dto.getGstNumber())) {

            logger.error("GST Number Already Exists : {}", dto.getGstNumber());

            throw new DuplicateResourceException("GST Number already exists");
        }

        Supplier supplier = mapper.map(dto, Supplier.class);

        Supplier saved = repository.save(supplier);

        logger.info("Supplier Created Successfully : {}", saved.getSupplierId());

        return mapper.map(saved, SupplierDto.class);
    }

    @Override
    public Page<SupplierDto> getAllSuppliers(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching All Suppliers | Page : {} | Size : {} | SortBy : {} | Direction : {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Supplier> supplierPage = repository.findAll(pageable);

        logger.info("Total Suppliers Found : {}", supplierPage.getTotalElements());

        return supplierPage.map(
                supplier -> mapper.map(supplier, SupplierDto.class)
        );
    }

    @Override
    public SupplierDto getSupplierById(Long id) {

        logger.info("Fetching Supplier Id : {}", id);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Supplier Not Found : {}", id);

                    return new ResourceNotFoundException("Supplier not found");
                });

        logger.info("Supplier Found : {}", supplier.getSupplierName());

        return mapper.map(supplier, SupplierDto.class);
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto dto) {

        logger.info("Updating Supplier Id : {}", id);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Supplier Not Found : {}", id);

                    return new ResourceNotFoundException("Supplier not found");
                });

        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setGstNumber(dto.getGstNumber());

        Supplier updated = repository.save(supplier);

        logger.info("Supplier Updated Successfully : {}", updated.getSupplierId());

        return mapper.map(updated, SupplierDto.class);
    }

    @Override
    public void deleteSupplier(Long id) {

        logger.warn("Deleting Supplier Id : {}", id);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Supplier Not Found : {}", id);

                    return new ResourceNotFoundException("Supplier not found");
                });

        repository.delete(supplier);

        logger.info("Supplier Deleted Successfully : {}", id);
    }

    @Override
    public Page<SupplierDto> searchSupplier(
            String keyword,
            int page,
            int size) {

        logger.info("Searching Supplier With Keyword : {}", keyword);

        Pageable pageable = PageRequest.of(page, size);

        Page<SupplierDto> suppliers = repository
                .findBySupplierNameContainingIgnoreCase(keyword, pageable)
                .map(supplier -> mapper.map(supplier, SupplierDto.class));

        logger.info("Search Result Count : {}", suppliers.getTotalElements());

        return suppliers;
    }
}