package com.example.nexacron.controller;

import com.example.nexacron.dto.ReminderRequest;
import com.example.nexacron.dto.ReminderResponse;
import com.example.nexacron.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders(@RequestHeader("X-User-Id") Long userId) {
        List<ReminderResponse> reminders = reminderService.getAllReminders(userId);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> getReminderById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.getReminderById(id, userId);
        return ResponseEntity.ok(reminder);
    }

    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(
            @Valid @RequestBody ReminderRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.createReminder(request, userId);
        return ResponseEntity.status(201).body(reminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> updateReminder(
            @PathVariable Long id,
            @Valid @RequestBody ReminderRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ReminderResponse reminder = reminderService.updateReminder(id, request, userId);
        return ResponseEntity.ok(reminder);
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