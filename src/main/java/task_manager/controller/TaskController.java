package task_manager.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import task_manager.model.Task;
import task_manager.service.TaskService;
import task_manager.dto.TaskRequest;
import task_manager.dto.TaskResponse;

import java.util.List;
import java.security.Principal;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/api/tasks")
    public List<TaskResponse> getTasks(Principal principal) {

        return taskService.getTasks(principal.getName())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/api/tasks/{id}")
    public TaskResponse getTaskById(
            @PathVariable Long id, Principal principal) {

        Task task = taskService.getTaskById(id, principal.getName());

        return mapToResponse(task);
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            Principal principal) {

        Task task = new Task(
                null,
                request.getTitle(),
                request.getDescription(),
                request.isCompleted()
        );

        Task createdTask = taskService.createTask(
                task, principal.getName()
        );

        TaskResponse response = mapToResponse(createdTask);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/api/tasks/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            Principal principal) {

        Task task = new Task(
                id,
                request.getTitle(),
                request.getDescription(),
                request.isCompleted()
        );

        Task updatedTask = taskService.updateTask(
                id, task, principal.getName()
        );

        return mapToResponse(updatedTask);
    }

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id, Principal principal) {

        taskService.deleteTask(id, principal.getName());

        return ResponseEntity.noContent().build();
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}