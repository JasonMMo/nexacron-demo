package com.example.nexacron.service;

import com.example.nexacron.dto.CategoryRequest;
import com.example.nexacron.dto.CategoryResponse;
import com.example.nexacron.exception.ResourceAlreadyExistsException;
import com.example.nexacron.exception.ResourceNotFoundException;
import com.example.nexacron.model.Category;
import com.example.nexacron.model.User;
import com.example.nexacron.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CategoryService categoryService;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Work");
        testCategory.setDescription("Work related tasks");
        testCategory.setUser(testUser);
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        when(categoryRepository.findByUserIdOrderByNameAsc(testUser.getId()))
                .thenReturn(Arrays.asList(testCategory));

        List<CategoryResponse> result = categoryService.getAllCategories(testUser.getId());

        assertEquals(1, result.size());
        assertEquals("Work", result.get(0).getName());
        assertEquals("Work related tasks", result.get(0).getDescription());
    }

    @Test
    void getCategoryById_WhenCategoryExistsAndOwner_ShouldReturnCategory() {
        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));

        CategoryResponse result = categoryService.getCategoryById(testCategory.getId(), testUser.getId());

        assertNotNull(result);
        assertEquals("Work", result.getName());
        assertEquals(testUser.getId(), testCategory.getUser().getId());
    }

    @Test
    void getCategoryById_WhenCategoryNotFound_ShouldThrowException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(99L, testUser.getId());
        });
    }

    @Test
    void createCategory_ShouldCreateNewCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Personal");
        request.setDescription("Personal tasks");

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(categoryRepository.existsByUserIdAndName(testUser.getId(), "Personal")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryResponse result = categoryService.createCategory(request, testUser.getId());

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
    }

    @Test
    void createCategory_WhenNameExists_ShouldThrowException() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Work");

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(categoryRepository.existsByUserIdAndName(testUser.getId(), "Work")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            categoryService.createCategory(request, testUser.getId());
        });
    }

    @Test
    void updateCategory_ShouldUpdateCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Work");
        request.setDescription("Updated description");

        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));
        when(categoryRepository.existsByUserIdAndName(testUser.getId(), "Updated Work")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryResponse result = categoryService.updateCategory(testCategory.getId(), request, testUser.getId());

        assertNotNull(result);
        assertEquals("Updated Work", result.getName());
        assertEquals("Updated description", result.getDescription());
    }

    @Test
    void deleteCategory_WhenOwner_ShouldDeleteCategory() {
        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));

        assertDoesNotThrow(() -> {
            categoryService.deleteCategory(testCategory.getId(), testUser.getId());
        });

        verify(categoryRepository, times(1)).delete(testCategory);
    }
}