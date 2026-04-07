package com.demo.user.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.demo.user.entity.Task;

@Repository
public class TaskJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void bulkInsert(List<Task> tasks, String userId) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO tasks (id, user_id, name, description, status) VALUES (?, ?, ?, ?, ?)",
                tasks,
                100,
                (PreparedStatement ps, Task task) -> {
                    ps.setObject(1, UUID.randomUUID());
                    ps.setObject(2, UUID.fromString(userId));
                    ps.setString(3, task.getName());
                    ps.setString(4, task.getDescription());
                    ps.setString(5, task.getStatus());
                });
    }
}
