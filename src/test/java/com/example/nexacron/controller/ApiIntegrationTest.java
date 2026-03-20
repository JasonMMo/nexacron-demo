package com.example.nexacron.controller;

import com.example.nexacron.dto.CategoryRequest;
import com.example.nexacron.dto.ReminderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAuthenticationFlow() throws Exception {
        // User registration
        String registrationJson = objectMapper.writeValueAsString(createUserRequest());

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testCategoryCRUDOperations() throws Exception {
        // Create category
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test Category");
        categoryRequest.setDescription("Test description");

        String categoryJson = objectMapper.writeValueAsString(categoryRequest);

        // POST /api/categories
        String response = mockMvc.perform(post("/api/categories")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify category was created
        mockMvc.perform(get("/api/categories")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"));

        // Update category
        categoryRequest.setName("Updated Category");
        String updatedJson = objectMapper.writeValueAsString(categoryRequest);

        mockMvc.perform(put("/api/categories/1")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category"));

        // Delete category
        mockMvc.perform(delete("/api/categories/1")
                .header("X-User-Id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testReminderCRUDOperations() throws Exception {
        // Create reminder
        ReminderRequest reminderRequest = new ReminderRequest();
        reminderRequest.setTitle("Test Reminder");
        reminderRequest.setDescription("This is a test reminder");
        reminderRequest.setPriority("HIGH");

        String reminderJson = objectMapper.writeValueAsString(reminderRequest);

        // POST /api/reminders
        String response = mockMvc.perform(post("/api/reminders")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify reminder was created
        mockMvc.perform(get("/api/reminders")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Reminder"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));

        // Update reminder
        reminderRequest.setTitle("Updated Reminder");
        reminderRequest.setDescription("Updated description");
        String updatedJson = objectMapper.writeValueAsString(reminderRequest);

        mockMvc.perform(put("/api/reminders/1")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Reminder"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        // Toggle completion status
        mockMvc.perform(patch("/api/reminders/1/complete")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        // Delete reminder
        mockMvc.perform(delete("/api/reminders/1")
                .header("X-User-Id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testReminderFiltering() throws Exception {
        // Create test data
        ReminderRequest reminder1 = new ReminderRequest();
        reminder1.setTitle("High Priority Task");
        reminder1.setDescription("Important task");
        reminder1.setPriority("HIGH");

        ReminderRequest reminder2 = new ReminderRequest();
        reminder2.setTitle("Low Priority Task");
        reminder2.setDescription("Not so important");
        reminder2.setPriority("LOW");

        // Create reminders
        mockMvc.perform(post("/api/reminders")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reminder1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/reminders")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reminder2)))
                .andExpect(status().isCreated());

        // Test filtering by priority
        mockMvc.perform(get("/api/reminders/priority/HIGH")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("High Priority Task"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));

        mockMvc.perform(get("/api/reminders/priority/LOW")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Low Priority Task"));

        // Test search
        mockMvc.perform(get("/api/reminders/search?keyword=Important")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("High Priority Task"));
    }

    @Test
    void testErrorHandling() throws Exception {
        // Test 404 for non-existent reminder
        mockMvc.perform(get("/api/reminders/999")
                .header("X-User-Id", "1"))
                .andExpect(status().isNotFound());

        // Test 404 for non-existent category
        mockMvc.perform(get("/api/categories/999")
                .header("X-User-Id", "1"))
                .andExpect(status().isNotFound());

        // Test validation errors
        CategoryRequest invalidCategory = new CategoryRequest();
        invalidCategory.setName(""); // Empty name should fail validation

        mockMvc.perform(post("/api/categories")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());
    }

    private Object createUserRequest() {
        // Helper method to create user registration request
        return new Object() {
            String email = "test@example.com";
            String password = "password123";
            String name = "Test User";
        };
    }
}