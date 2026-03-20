package com.example.nexacron.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReminderRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    private Long categoryId;

    private String priority = "MEDIUM";
}