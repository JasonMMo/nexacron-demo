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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReminderService reminderService;

    private User testUser;
    private Category testCategory;
    private Reminder testReminder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Work");
        testCategory.setUser(testUser);

        testReminder = new Reminder();
        testReminder.setId(1L);
        testReminder.setTitle("Test Reminder");
        testReminder.setUser(testUser);
        testReminder.setCategory(testCategory);
    }

    @Test
    void getAllReminders_ShouldReturnAllReminders() {
        when(reminderRepository.findByUserId(testUser.getId())).thenReturn(Arrays.asList(testReminder));

        List<ReminderResponse> result = reminderService.getAllReminders(testUser.getId());

        assertEquals(1, result.size());
        assertEquals("Test Reminder", result.get(0).getTitle());
    }

    @Test
    void getReminderById_WhenReminderExistsAndOwner_ShouldReturnReminder() {
        when(reminderRepository.findById(testReminder.getId())).thenReturn(Optional.of(testReminder));

        ReminderResponse result = reminderService.getReminderById(testReminder.getId(), testUser.getId());

        assertNotNull(result);
        assertEquals("Test Reminder", result.getTitle());
        assertEquals(testUser.getId(), testReminder.getUser().getId());
    }

    @Test
    void getReminderById_WhenReminderNotFound_ShouldThrowException() {
        when(reminderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reminderService.getReminderById(99L, testUser.getId());
        });
    }

    @Test
    void createReminder_ShouldCreateNewReminder() {
        ReminderRequest request = new ReminderRequest();
        request.setTitle("New Reminder");
        request.setDescription("Description");
        request.setPriority("HIGH");

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(testCategory));
        when(reminderRepository.save(any(Reminder.class))).thenReturn(testReminder);

        ReminderResponse result = reminderService.createReminder(request, testUser.getId());

        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals("HIGH", result.getPriority());
    }

    @Test
    void toggleReminderComplete_ShouldToggleStatus() {
        when(reminderRepository.findById(testReminder.getId())).thenReturn(Optional.of(testReminder));

        // First call - mark as complete
        ReminderResponse result1 = reminderService.toggleReminderComplete(testReminder.getId(), testUser.getId());
        assertTrue(result1.isCompleted());
        assertNotNull(result1.getCompletedAt());

        // Second call - mark as incomplete (create a new reminder instance)
        Reminder incompleteReminder = new Reminder();
        incompleteReminder.setId(testReminder.getId());
        incompleteReminder.setCompleted(true);
        incompleteReminder.setCompletedAt(testReminder.getCompletedAt());

        when(reminderRepository.findById(testReminder.getId())).thenReturn(Optional.of(incompleteReminder));

        ReminderResponse result2 = reminderService.toggleReminderComplete(testReminder.getId(), testUser.getId());
        assertFalse(result2.isCompleted());
        assertNull(result2.getCompletedAt());
    }

    @Test
    void deleteReminder_WhenOwner_ShouldDeleteReminder() {
        when(reminderRepository.findById(testReminder.getId())).thenReturn(Optional.of(testReminder));

        assertDoesNotThrow(() -> {
            reminderService.deleteReminder(testReminder.getId(), testUser.getId());
        });

        verify(reminderRepository, times(1)).delete(testReminder);
    }

    @Test
    void searchReminders_ShouldReturnMatchingReminders() {
        testReminder.setDescription("Important meeting");
        when(reminderRepository.findByUserId(testUser.getId())).thenReturn(Arrays.asList(testReminder));

        List<ReminderResponse> result = reminderService.searchReminders(testUser.getId(), "meeting");

        assertEquals(1, result.size());
        assertEquals("Important meeting", result.get(0).getDescription());
    }
}