package com.inventory.service.impl;

import com.inventory.dto.CategoryDto;
import com.inventory.entity.Category;
import com.inventory.exception.DuplicateResourceException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.CategoryRepository;
import com.inventory.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDto createCategory(CategoryDto dto) {

        logger.info("Creating Category : {}", dto.getCategoryName());

        if (repository.existsByCategoryName(dto.getCategoryName())) {

            logger.error("Category Already Exists : {}", dto.getCategoryName());

            throw new DuplicateResourceException("Category already exists");
        }

        Category category = mapper.map(dto, Category.class);

        Category saved = repository.save(category);

        logger.info("Category Created Successfully : {}", saved.getCategoryId());

        return mapper.map(saved, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        logger.info("Fetching All Categories");

        List<CategoryDto> categories = repository.findAll()
                .stream()
                .map(category -> mapper.map(category, CategoryDto.class))
                .toList();

        logger.info("Total Categories Found : {}", categories.size());

        return categories;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {

        logger.info("Fetching Category Id : {}", id);

        Category category = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Category Not Found : {}", id);

                    return new ResourceNotFoundException("Category not found");
                });

        logger.info("Category Found : {}", category.getCategoryName());

        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {

        logger.info("Updating Category Id : {}", id);

        Category category = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Category Not Found : {}", id);

                    return new ResourceNotFoundException("Category not found");
                });

        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());

        Category updated = repository.save(category);

        logger.info("Category Updated Successfully : {}", updated.getCategoryId());

        return mapper.map(updated, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {

        logger.warn("Deleting Category Id : {}", id);

        Category category = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Category Not Found : {}", id);

                    return new ResourceNotFoundException("Category not found");
                });

        repository.delete(category);

        logger.info("Category Deleted Successfully : {}", id);
    }
}