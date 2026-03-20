package com.example.nexacron.dto;

import com.example.nexacron.model.Reminder;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
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
}