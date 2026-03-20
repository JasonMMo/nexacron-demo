package com.example.nexacron.dto;

import com.example.nexacron.model.Reminder;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

public class ReminderResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    private String categoryName;
    private String priority;

    public static ReminderResponseBuilder builder() {
        return new ReminderResponseBuilder();
    }

    public static ReminderResponse from(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .description(reminder.getDescription())
                .dueDate(reminder.getDueDate())
                .completed(reminder.isCompleted())
                .completedAt(reminder.getCompletedAt())
                .createdAt(reminder.getCreatedAt())
                .updatedAt(reminder.getUpdatedAt())
                .categoryId(reminder.getCategory() != null ? reminder.getCategory().getId() : null)
                .categoryName(reminder.getCategory() != null ? reminder.getCategory().getName() : null)
                .priority(reminder.getPriority().name())
                .build();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getPriority() { return priority; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setPriority(String priority) { this.priority = priority; }

    // Builder class
    public static class ReminderResponseBuilder {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime dueDate;
        private boolean completed;
        private LocalDateTime completedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long categoryId;
        private String categoryName;
        private String priority;

        public ReminderResponseBuilder id(Long id) { this.id = id; return this; }
        public ReminderResponseBuilder title(String title) { this.title = title; return this; }
        public ReminderResponseBuilder description(String description) { this.description = description; return this; }
        public ReminderResponseBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public ReminderResponseBuilder completed(boolean completed) { this.completed = completed; return this; }
        public ReminderResponseBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }
        public ReminderResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ReminderResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public ReminderResponseBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public ReminderResponseBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public ReminderResponseBuilder priority(String priority) { this.priority = priority; return this; }

        public ReminderResponse build() {
            ReminderResponse response = new ReminderResponse();
            response.id = this.id;
            response.title = this.title;
            response.description = this.description;
            response.dueDate = this.dueDate;
            response.completed = this.completed;
            response.completedAt = this.completedAt;
            response.createdAt = this.createdAt;
            response.updatedAt = this.updatedAt;
            response.categoryId = this.categoryId;
            response.categoryName = this.categoryName;
            response.priority = this.priority;
            return response;
        }
    }
}