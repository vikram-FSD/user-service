package com.demo.user.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.user.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query(value = "SELECT id, user_id, name, description, status, updated_at FROM tasks WHERE user_id = ? AND id = ?", nativeQuery = true)
    public Task findByUserIdAndId(UUID userId, UUID taskId);

    public Page<Task> findByUserId(UUID userId, Pageable pageable);

    public Page<Task> findByUserIdAndStatus(UUID userId, String status, Pageable pageable);

}
