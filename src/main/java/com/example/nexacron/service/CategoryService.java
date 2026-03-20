package com.example.nexacron.service;

import com.example.nexacron.dto.CategoryRequest;
import com.example.nexacron.dto.CategoryResponse;
import com.example.nexacron.exception.ResourceNotFoundException;
import com.example.nexacron.exception.ResourceAlreadyExistsException;
import com.example.nexacron.model.Category;
import com.example.nexacron.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public List<CategoryResponse> getAllCategories(Long userId) {
        List<Category> categories = categoryRepository.findByUserIdOrderByNameAsc(userId);
        return categories.stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found or access denied");
        }

        return CategoryResponse.from(category);
    }

    public CategoryResponse createCategory(CategoryRequest request, Long userId) {
        // Check if category already exists
        if (categoryRepository.existsByUserIdAndName(userId, request.getName())) {
            throw new ResourceAlreadyExistsException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUser(userService.getUserById(userId));

        category = categoryRepository.save(category);
        return CategoryResponse.from(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Verify ownership
        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found or access denied");
        }

        // Check if new name conflicts with existing category
        if (!category.getName().equals(request.getName()) &&
            categoryRepository.existsByUserIdAndName(userId, request.getName())) {
            throw new ResourceAlreadyExistsException("Category with name '" + request.getName() + "' already exists");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        category = categoryRepository.save(category);
        return CategoryResponse.from(category);
    }

    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Verify ownership
        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found or access denied");
        }

        // CategoryRepository has @PreRemove annotation to clean up references
        categoryRepository.delete(category);
    }
}