package com.example.nexacron.repository;

import com.example.nexacron.model.Reminder;
import com.example.nexacron.model.Reminder.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByUserId(Long userId);

    List<Reminder> findByUserIdAndCompleted(Long userId, boolean completed);

    List<Reminder> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Reminder> findByUserIdAndPriority(Long userId, Priority priority);

    List<Reminder> findByUserIdOrderByDueDateAsc(Long userId);

    List<Reminder> findByUserIdAndDueDateBeforeAndCompletedFalseOrderByDueDateAsc(
        Long userId, LocalDateTime dateTime);

    @Query("SELECT r FROM Reminder r WHERE r.user.id = :userId AND " +
           "(:completed IS NULL OR r.completed = :completed) AND " +
           "(:categoryId IS NULL OR r.category.id = :categoryId) AND " +
           "(:priority IS NULL OR r.priority = :priority) AND " +
           "(:overdueOnly = false OR (r.dueDate < CURRENT_TIMESTAMP AND r.completed = false))")
    List<Reminder> findByUserIdWithFilters(
        @Param("userId") Long userId,
        @Param("completed") Boolean completed,
        @Param("categoryId") Long categoryId,
        @Param("priority") Priority priority,
        @Param("overdueOnly") boolean overdueOnly);

    @Query("SELECT COUNT(r) FROM Reminder r WHERE r.user.id = :userId AND r.completed = false")
    long countIncompleteByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Reminder r WHERE r.user.id = :userId AND r.completed = false " +
           "AND r.dueDate < CURRENT_TIMESTAMP ORDER BY r.dueDate ASC")
    List<Reminder> findOverdueReminders(@Param("userId") Long userId);
}