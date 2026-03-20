package com.example.nexacron.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReminderRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    private Long categoryId;

    private String priority = "MEDIUM";

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Long getCategoryId() { return categoryId; }
    public String getPriority() { return priority; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setPriority(String priority) { this.priority = priority; }
}