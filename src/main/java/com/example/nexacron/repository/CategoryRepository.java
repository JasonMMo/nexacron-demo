package com.example.nexacron.repository;

import com.example.nexacron.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByUserIdAndName(Long userId, String name);

    List<Category> findByUserIdOrderByNameAsc(Long userId);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndName(Long userId, String name);
}