package task_manager;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import task_manager.controller.TaskController;
import task_manager.service.TaskService;
import task_manager.model.Task;
import task_manager.dto.TaskResponse;
import task_manager.dto.TaskRequest;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskControllerTest {

    private Principal getPrincipal() {
        return () -> "hamid";
    }

    @Test
    void testGetTasks() {

        TaskService taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);

        Task task = new Task(1L, "Learn Java", "Java basics", false);

        when(taskService.getTasks("hamid"))
                .thenReturn(List.of(task));

        List<TaskResponse> result =
                taskController.getTasks(getPrincipal());

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Learn Java", result.get(0).getTitle());
    }

    @Test
    void testGetTaskById() {

        TaskService taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);

        Task task = new Task(1L, "Learn Java", "Java basics", false);

        when(taskService.getTaskById(1L, "hamid"))
                .thenReturn(task);

        TaskResponse result =
                taskController.getTaskById(1L, getPrincipal());

        assertEquals(1L, result.getId());
        assertEquals("Learn Java", result.getTitle());
        assertEquals("Java basics", result.getDescription());
        assertEquals(false, result.isCompleted());
    }

    @Test
    void testCreateTask() {

        TaskService taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);

        TaskRequest request = new TaskRequest(
                "Learn Spring Boot",
                "Practice REST API testing",
                false
        );

        Task createdTask = new Task(
                1L,
                "Learn Spring Boot",
                "Practice REST API testing",
                false
        );

        when(taskService.createTask(any(Task.class), eq("hamid")))
                .thenReturn(createdTask);

        ResponseEntity<TaskResponse> response =
                taskController.createTask(request, getPrincipal());

        assertEquals(201, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Learn Spring Boot", response.getBody().getTitle());
    }

    @Test
    void testUpdateTask() {

        TaskService taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);

        TaskRequest request = new TaskRequest(
                "Master Spring Boot",
                "Learn advanced Spring Boot",
                true
        );

        Task updatedTask = new Task(
                1L,
                "Master Spring Boot",
                "Learn advanced Spring Boot",
                true
        );

        when(taskService.updateTask(
                eq(1L), any(Task.class), eq("hamid")))
                .thenReturn(updatedTask);

        TaskResponse result =
                taskController.updateTask(1L, request, getPrincipal());

        assertEquals(1L, result.getId());
        assertEquals("Master Spring Boot", result.getTitle());
        assertEquals("Learn advanced Spring Boot", result.getDescription());
        assertEquals(true, result.isCompleted());
    }

    @Test
    void testDeleteTask() {

        TaskService taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);

        ResponseEntity<Void> response =
                taskController.deleteTask(1L, getPrincipal());

        verify(taskService).deleteTask(1L, "hamid");

        assertEquals(204, response.getStatusCode().value());
    }
}