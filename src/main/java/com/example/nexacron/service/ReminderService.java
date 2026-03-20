package com.example.nexacron.service;

import com.example.nexacron.dto.ReminderRequest;
import com.example.nexacron.dto.ReminderResponse;
import com.example.nexacron.exception.ResourceNotFoundException;
import com.example.nexacron.model.Category;
import com.example.nexacron.model.Reminder;
import com.example.nexacron.model.Reminder.Priority;
import com.example.nexacron.repository.CategoryRepository;
import com.example.nexacron.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;  // Assuming this service exists for user management

    public List<ReminderResponse> getAllReminders(Long userId) {
        List<Reminder> reminders = reminderRepository.findByUserId(userId);
        return reminders.stream()
                .map(ReminderResponse::from)
                .collect(Collectors.toList());
    }

    public ReminderResponse getReminderById(Long id, Long userId) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        if (!reminder.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Reminder not found or access denied");
        }

        return ReminderResponse.from(reminder);
    }

    public ReminderResponse createReminder(ReminderRequest request, Long userId) {
        Reminder reminder = new Reminder();
        reminder.setTitle(request.getTitle());
        reminder.setDescription(request.getDescription());
        reminder.setDueDate(request.getDueDate());

        // Set priority
        try {
            reminder.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));
        } catch (IllegalArgumentException e) {
            reminder.setPriority(Priority.MEDIUM); // Default
        }

        // Set user
        reminder.setUser(userService.getUserById(userId));

        // Set category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            reminder.setCategory(category);
        }

        reminder = reminderRepository.save(reminder);
        return ReminderResponse.from(reminder);
    }

    public ReminderResponse updateReminder(Long id, ReminderRequest request, Long userId) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        // Verify ownership
        if (!reminder.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Reminder not found or access denied");
        }

        // Update fields
        reminder.setTitle(request.getTitle());
        reminder.setDescription(request.getDescription());
        reminder.setDueDate(request.getDueDate());

        // Update priority
        try {
            reminder.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));
        } catch (IllegalArgumentException e) {
            // Keep current priority if invalid
        }

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            reminder.setCategory(category);
        } else {
            reminder.setCategory(null);
        }

        reminder = reminderRepository.save(reminder);
        return ReminderResponse.from(reminder);
    }

    public void deleteReminder(Long id, Long userId) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        // Verify ownership
        if (!reminder.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Reminder not found or access denied");
        }

        reminderRepository.delete(reminder);
    }

    public ReminderResponse toggleReminderComplete(Long id, Long userId) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        // Verify ownership
        if (!reminder.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Reminder not found or access denied");
        }

        if (reminder.isCompleted()) {
            reminder.markAsIncomplete();
        } else {
            reminder.markAsCompleted();
        }

        reminder = reminderRepository.save(reminder);
        return ReminderResponse.from(reminder);
    }

    public List<ReminderResponse> getRemindersByCategory(Long categoryId, Long userId) {
        List<Reminder> reminders = reminderRepository.findByUserIdAndCategoryId(userId, categoryId);
        return reminders.stream()
                .map(ReminderResponse::from)
                .collect(Collectors.toList());
    }

    public List<ReminderResponse> getOverdueReminders(Long userId) {
        List<Reminder> reminders = reminderRepository.findOverdueReminders(userId);
        return reminders.stream()
                .map(ReminderResponse::from)
                .collect(Collectors.toList());
    }

    public List<ReminderResponse> getRemindersByPriority(Long userId, String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            List<Reminder> reminders = reminderRepository.findByUserIdAndPriority(userId, priorityEnum);
            return reminders.stream()
                    .map(ReminderResponse::from)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority: " + priority);
        }
    }

    public List<ReminderResponse> searchReminders(Long userId, String keyword) {
        // Simple keyword search in title and description
        List<Reminder> reminders = reminderRepository.findByUserId(userId);

        return reminders.stream()
                .filter(r -> r.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        r.getDescription() != null && r.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .map(ReminderResponse::from)
                .collect(Collectors.toList());
    }
}