package com.demo.user.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.user.dto.Response;
import com.demo.user.dto.TaskRequest;
import com.demo.user.entity.UserPrincipal;
import com.demo.user.service.TaskService;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/addTask")
    public Response.ResponseMessage addTask(@RequestBody List<TaskRequest> request,
            @AuthenticationPrincipal UserPrincipal principal) {

        taskService.addTask(request, principal);
        Response.ResponseMessage responseData = new Response.ResponseMessage(LocalDateTime.now(), HttpStatus.OK.value(),
                "Task added successfully");
        return responseData;
    }

    @GetMapping("/tasks")
    public Response.ResponseData getAllTasks(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String status,
            @AuthenticationPrincipal UserPrincipal principal) {

        Response.ResponseData responseData = new Response.ResponseData(LocalDateTime.now(), HttpStatus.OK.value(),
                "Tasks fetched successfully", taskService.getAllTasks(page, size, status, principal));
        return responseData;
    }

    @PatchMapping("/updateTask/{taskId}")
    public Response.ResponseMessage updateTask(@PathVariable UUID taskId, @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        taskService.updateTask(taskId, request, principal);
        Response.ResponseMessage responseData = new Response.ResponseMessage(LocalDateTime.now(), HttpStatus.OK.value(),
                "Task updated successfully");
        return responseData;
    }

    @DeleteMapping("/deleteTask/{taskId}")
    public Response.ResponseMessage deleteTask(@PathVariable UUID taskId,
            @AuthenticationPrincipal UserPrincipal principal) {
        taskService.deleteTask(taskId, principal);
        Response.ResponseMessage responseData = new Response.ResponseMessage(LocalDateTime.now(), HttpStatus.OK.value(),
                "Task deleted successfully");
        return responseData;
    }

}
