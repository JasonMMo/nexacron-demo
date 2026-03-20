package com.example.nexacron.service;

import com.example.nexacron.dto.ReminderRequest;
import com.example.nexacron.dto.ReminderResponse;
import com.example.nexacron.exception.ResourceNotFoundException;
import com.example.nexacron.model.Category;
import com.example.nexacron.model.Reminder;
import com.example.nexacron.model.User;
import com.example.nexacron.repository.CategoryRepository;
import com.example.nexacron.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReminderServiceSpringBootTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    private User testUser;
    private Category testCategory;
    private Reminder testReminder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("encoded_password");

        testUser = userService.createUser(testUser);

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Work");
        testCategory.setDescription("Work related tasks");
        testCategory.setUser(testUser);
        testCategory = categoryRepository.save(testCategory);

        testReminder = new Reminder();
        testReminder.setId(1L);
        testReminder.setTitle("Test Reminder");
        testReminder.setDescription("Initial description");
        testReminder.setUser(testUser);
        testReminder.setCategory(testCategory);
        testReminder = reminderRepository.save(testReminder);
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        List<ReminderResponse> reminders = reminderService.getAllReminders(testUser.getId());

        assertNotNull(reminders);
        assertEquals(1, reminders.size());
        assertEquals("Test Reminder", reminders.get(0).getTitle());
        assertEquals("Initial description", reminders.get(0).getDescription());
    }

    @Test
    void getReminderById_WhenCategoryExistsAndOwner_ShouldReturnCategory() {
        ReminderResponse result = reminderService.getReminderById(testReminder.getId(), testUser.getId());

        assertNotNull(result);
        assertEquals("Test Reminder", result.getTitle());
        assertEquals(testUser.getId(), testReminder.getUser().getId());
    }

    @Test
    void getReminderById_WhenCategoryNotFound_ShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            reminderService.getReminderById(999L, testUser.getId());
        });
    }

    @Test
    void createReminder_ShouldCreateNewReminder() {
        ReminderRequest request = new ReminderRequest();
        request.setTitle("New Reminder");
        request.setDescription("Description");
        request.setPriority("HIGH");

        ReminderResponse result = reminderService.createReminder(request, testUser.getId());

        assertNotNull(result);
        assertEquals("New Reminder", result.getTitle());
        assertEquals("Description", result.getDescription());
        assertEquals("HIGH", result.getPriority());
    }

    @Test
    void updateReminder_ShouldUpdateCategory() {
        ReminderRequest request = new ReminderRequest();
        request.setTitle("Updated Reminder");
        request.setDescription("Updated description");
        request.setPriority("LOW");

        ReminderResponse result = reminderService.updateReminder(testReminder.getId(), request, testUser.getId());

        assertNotNull(result);
        assertEquals("Updated Reminder", result.getTitle());
        assertEquals("Updated description", result.getDescription());
        assertEquals("LOW", result.getPriority());
    }

    @Test
    void deleteReminder_WhenOwner_ShouldDeleteCategory() {
        assertDoesNotThrow(() -> {
            reminderService.deleteReminder(testReminder.getId(), testUser.getId());
        });

        assertFalse(reminderRepository.existsById(testReminder.getId()));
    }

    @Test
    void toggleReminderComplete_ShouldToggleStatus() {
        // First call - mark as complete
        ReminderResponse result1 = reminderService.toggleReminderComplete(testReminder.getId(), testUser.getId());
        assertTrue(result1.isCompleted());
        assertNotNull(result1.getCompletedAt());

        // Second call - mark as incomplete
        ReminderResponse result2 = reminderService.toggleReminderComplete(testReminder.getId(), testUser.getId());
        assertFalse(result2.isCompleted());
        assertNull(result2.getCompletedAt());
    }

    @Test
    void getRemindersByCategory_ShouldReturnReminders() {
        List<ReminderResponse> reminders = reminderService.getRemindersByCategory(testCategory.getId(), testUser.getId());

        assertNotNull(reminders);
        assertEquals(1, reminders.size());
        assertEquals("Test Reminder", reminders.get(0).getTitle());
    }

    @Test
    void getRemindersByPriority_ShouldReturnReminders() {
        List<ReminderResponse> reminders = reminderService.getRemindersByPriority(testUser.getId(), "MEDIUM");

        assertNotNull(reminders);
        assertEquals(1, reminders.size());
        assertEquals("Test Reminder", reminders.get(0).getTitle());
    }

    @Test
    void searchReminders_ShouldReturnMatchingReminders() {
        testReminder.setDescription("Important meeting");
        reminderRepository.save(testReminder);

        List<ReminderResponse> reminders = reminderService.searchReminders(testUser.getId(), "meeting");

        assertNotNull(reminders);
        assertEquals(1, reminders.size());
        assertEquals("Important meeting", reminders.get(0).getDescription());
    }
}