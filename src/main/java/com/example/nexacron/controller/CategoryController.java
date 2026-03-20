package com.example.nexacron.controller;

import com.example.nexacron.dto.CategoryRequest;
import com.example.nexacron.dto.CategoryResponse;
import com.example.nexacron.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestHeader("X-User-Id") Long userId) {
        List<CategoryResponse> categories = categoryService.getAllCategories(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        CategoryResponse category = categoryService.getCategoryById(id, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CategoryResponse category = categoryService.createCategory(request, userId);
        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CategoryResponse category = categoryService.updateCategory(id, request, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        categoryService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }
}