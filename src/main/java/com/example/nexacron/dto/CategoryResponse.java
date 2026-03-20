package com.example.nexacron.dto;

import com.example.nexacron.model.Category;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long reminderCount;

    public static CategoryResponseBuilder builder() {
        return new CategoryResponseBuilder();
    }

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .reminderCount((long) category.getReminders().size())
                .build();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getReminderCount() { return reminderCount; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setReminderCount(Long reminderCount) { this.reminderCount = reminderCount; }

    // Builder class
    public static class CategoryResponseBuilder {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long reminderCount;

        public CategoryResponseBuilder id(Long id) { this.id = id; return this; }
        public CategoryResponseBuilder name(String name) { this.name = name; return this; }
        public CategoryResponseBuilder description(String description) { this.description = description; return this; }
        public CategoryResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CategoryResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public CategoryResponseBuilder reminderCount(Long reminderCount) { this.reminderCount = reminderCount; return this; }

        public CategoryResponse build() {
            CategoryResponse response = new CategoryResponse();
            response.id = this.id;
            response.name = this.name;
            response.description = this.description;
            response.createdAt = this.createdAt;
            response.updatedAt = this.updatedAt;
            response.reminderCount = this.reminderCount;
            return response;
        }
    }
}