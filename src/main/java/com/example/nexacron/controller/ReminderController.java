package com.example.nexacron.controller;

import com.example.nexacron.dto.ReminderRequest;
import com.example.nexacron.dto.ReminderResponse;
import com.example.nexacron.service.ReminderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllReminders(@RequestHeader("X-User-Id") Long userId) {
        List<ReminderResponse> reminders = reminderService.getAllReminders(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reminders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReminderById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.getReminderById(id, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reminder);
    }

    @PostMapping
    public ResponseEntity<?> createReminder(
            @Valid @RequestBody ReminderRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.createReminder(request, userId);
        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReminder(
            @PathVariable Long id,
            @Valid @RequestBody ReminderRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.updateReminder(id, request, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reminder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        reminderService.deleteReminder(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ReminderResponse> toggleComplete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.toggleReminderComplete(id, userId);
        return ResponseEntity.ok(reminder);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ReminderResponse>> getRemindersByCategory(
            @PathVariable Long categoryId,
            @RequestHeader("X-User-Id") Long userId) {
        List<ReminderResponse> reminders = reminderService.getRemindersByCategory(categoryId, userId);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ReminderResponse>> getOverdueReminders(@RequestHeader("X-User-Id") Long userId) {
        List<ReminderResponse> reminders = reminderService.getOverdueReminders(userId);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<ReminderResponse>> getRemindersByPriority(
            @PathVariable String priority,
            @RequestHeader("X-User-Id") Long userId) {
        List<ReminderResponse> reminders = reminderService.getRemindersByPriority(userId, priority);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReminderResponse>> searchReminders(
            @RequestParam String keyword,
            @RequestHeader("X-User-Id") Long userId) {
        List<ReminderResponse> reminders = reminderService.searchReminders(userId, keyword);
        return ResponseEntity.ok(reminders);
    }
}