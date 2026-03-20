package com.example.nexacron.controller;

import com.example.nexacron.dto.ReminderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReminderController.class)
class ReminderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReminder_WithoutAuth_ShouldReturn401() throws Exception {
        ReminderRequest request = new ReminderRequest();
        request.setTitle("Test Reminder");

        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Reminder\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllReminders_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/reminders"))
                .andExpect(status().isUnauthorized());
    }
}