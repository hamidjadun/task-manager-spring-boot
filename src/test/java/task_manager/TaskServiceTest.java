package task_manager;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import task_manager.model.Task;
import task_manager.repository.TaskRepository;
import task_manager.service.TaskService;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import task_manager.exception.TaskNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Test
    void testGetTasks() {

        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

        TaskService taskService = new TaskService(taskRepository);

        Task task1 = new Task(1L, "Learn Java", "Java basics", false);
        Task task2 = new Task(2L, "Learn Spring Boot", "Spring Boot basics", true);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getTasks();

        assertEquals(2, tasks.size());
        assertEquals("Learn Java", tasks.get(0).getTitle());
        assertEquals("Learn Spring Boot", tasks.get(1).getTitle());
    }

    @Test
    void testGetTaskById() {

        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

        TaskService taskService = new TaskService(taskRepository);

        Task task = new Task(1L, "Learn Java", "Java basics", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Learn Java", result.getTitle());
        assertEquals("Java basics", result.getDescription());
        assertEquals(false, result.isCompleted());
    }

    @Test
    void testGetTaskByIdNotFound() {

        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

        TaskService taskService = new TaskService(taskRepository);

        when(taskRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(100L);
        });
    }

    @Test
    void testCreateTask() {

        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

        TaskService taskService = new TaskService(taskRepository);

        Task task = new Task(null, "Learn Spring Boot", "Practice Spring Boot testing", false);

        Task savedTask = new Task(1L, "Learn Spring Boot", "Practice Spring Boot testing", false);

        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertEquals(1L, result.getId());
        assertEquals("Learn Spring Boot", result.getTitle());
        assertEquals("Practice Spring Boot testing", result.getDescription());
        assertEquals(false, result.isCompleted());
    }

    @Test
    void testUpdateTask() {

        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

        TaskService taskService = new TaskService(taskRepository);

        Task existingTask = new Task(1L, "Learn Java", "Java basics", false);

        Task updatedTask = new Task(null, "Learn Spring Boot", "Spring Boot basics", true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals(1L, result.getId());
        assertEquals("Learn Spring Boot", result.getTitle());
        assertEquals("Spring Boot basics", result.getDescription());
        assertEquals(true, result.isCompleted());
    }

    @Test
    void testDeleteTask() {

        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

        TaskService taskService = new TaskService(taskRepository);

        Task task = new Task(1L, "Learn Java", "Java basics", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        Mockito.verify(taskRepository).delete(task);
    }
}