package com.demo.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.user.dto.PagedResponse;
import com.demo.user.dto.TaskRequest;
import com.demo.user.dto.TaskResponse;
import com.demo.user.entity.Task;
import com.demo.user.entity.UserPrincipal;
import com.demo.user.exception.BusinessException;
import com.demo.user.mapper.TaskMapper;
import com.demo.user.repository.TaskJDBCRepository;
import com.demo.user.repository.TaskRepository;
import com.demo.user.utils.Util;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskJDBCRepository taskJDBCRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, TaskJDBCRepository taskJDBCRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskJDBCRepository = taskJDBCRepository;
    }

    @Cacheable(value = "tasks", key = "#principal.userId + ':' + #page + ':' + #size + ':' + (#status == null ? 'ALL' : #status)")
    public PagedResponse<TaskResponse> getAllTasks(int page, int size, String status, UserPrincipal principal) {

        log.info("Fetching tasks from DB for user: {}", principal.getUserId());

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks;

        if (status == null || status.trim().isEmpty()) {
            tasks = taskRepository.findByUserId(UUID.fromString(principal.getUserId()), pageable);
        } else {
            if (!Util.isValidStatus(status)) {
                throw new BusinessException("Invalid status");
            }
            tasks = taskRepository.findByUserIdAndStatus(UUID.fromString(principal.getUserId()), status, pageable);
        }

        return new PagedResponse<>(tasks.getContent().stream().map(taskMapper::toResponse).toList(), page,
                tasks.getTotalElements(), tasks.getTotalPages());
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void addTask(List<TaskRequest> request, UserPrincipal principal) {
        var userId = principal.getUserId();
        List<Task> entity = taskMapper.toEntity(request);
        taskJDBCRepository.bulkInsert(entity, userId);

    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void updateTask(UUID taskId, TaskRequest request, UserPrincipal principal) {

        var task = taskRepository.findByUserIdAndId(UUID.fromString(principal.getUserId()),
                taskId);
        if (task == null) {
            throw new BusinessException("Task not found");
        }
        if (!Util.isValidStatus(request.status())) {
            throw new BusinessException("Invalid status");
        }
        if (request.name() != null && !request.name().trim().isEmpty()) {
            task.setName(request.name());
        }
        if (request.description() != null && !request.description().trim().isEmpty()) {
            task.setDescription(request.description());
        }
        task.setStatus(request.status());
        task.setUpdated_at(LocalDateTime.now());
        taskRepository.save(task);
    }

    public void deleteTask(UUID taskId, UserPrincipal principal) {

        var task = taskRepository.findByUserIdAndId(UUID.fromString(principal.getUserId()),
                taskId);
        if (task == null) {
            throw new BusinessException("Task not found");
        }
        taskRepository.delete(task);
    }

}
