
package task_manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import task_manager.model.Task;
import task_manager.service.TaskService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import task_manager.dto.TaskRequest;
import task_manager.dto.TaskResponse;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/api/tasks")
    public List<TaskResponse> getTasks() {

        return taskService.getTasks()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/api/tasks/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {

        Task task = taskService.getTaskById(id);

        return mapToResponse(task);
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {

        Task task = new Task(
                null,
                request.getTitle(),
                request.getDescription(),
                request.isCompleted()
        );

        Task createdTask = taskService.createTask(task);

        TaskResponse response = mapToResponse(createdTask);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/api/tasks/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {

        Task task = new Task(
                id,
                request.getTitle(),
                request.getDescription(),
                request.isCompleted()
        );

        Task updatedTask = taskService.updateTask(id, task);

        return mapToResponse(updatedTask);
    }

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);

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
