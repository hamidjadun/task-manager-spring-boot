package task_manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task_manager.exception.TaskNotFoundException;
import task_manager.model.AppUser;
import task_manager.model.Task;
import task_manager.repository.TaskRepository;
import task_manager.repository.UserRepository;
import task_manager.service.TaskService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskService taskService;
    private AppUser user;

    @BeforeEach
    void setUp() {

        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);

        user = mock(AppUser.class);
        when(user.getId()).thenReturn(1L);

        when(userRepository.findByUsername("hamid"))
                .thenReturn(Optional.of(user));

        taskService = new TaskService(taskRepository, userRepository);
    }

    @Test
    void testGetTasks() {

        Task task1 = new Task(1L, "Learn Java", "Java basics", false);
        Task task2 = new Task(2L, "Learn Spring Boot", "Spring Boot basics", true);

        when(taskRepository.findByUserId(1L))
                .thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getTasks("hamid");

        assertEquals(2, tasks.size());
        assertEquals("Learn Java", tasks.get(0).getTitle());
        assertEquals("Learn Spring Boot", tasks.get(1).getTitle());
    }

    @Test
    void testGetTaskById() {

        Task task = new Task(1L, "Learn Java", "Java basics", false);

        when(taskRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L, "hamid");

        assertEquals(1L, result.getId());
        assertEquals("Learn Java", result.getTitle());
    }

    @Test
    void testGetTaskByIdNotFound() {

        when(taskRepository.findByIdAndUserId(100L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () ->
                taskService.getTaskById(100L, "hamid"));
    }

    @Test
    void testCreateTask() {

        Task task = new Task(null, "Learn Spring Boot",
                "Practice Spring Boot testing", false);

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task, "hamid");

        assertEquals("Learn Spring Boot", result.getTitle());
        assertEquals(user, result.getUser());

        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateTask() {

        Task existingTask = new Task(
                1L, "Learn Java", "Java basics", false);

        Task updatedTask = new Task(
                null, "Learn Spring Boot", "Spring Boot basics", true);

        when(taskRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(existingTask))
                .thenReturn(existingTask);

        Task result = taskService.updateTask(
                1L, updatedTask, "hamid");

        assertEquals("Learn Spring Boot", result.getTitle());
        assertEquals("Spring Boot basics", result.getDescription());
        assertTrue(result.isCompleted());
    }

    @Test
    void testDeleteTask() {

        Task task = new Task(
                1L, "Learn Java", "Java basics", false);

        when(taskRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(1L, "hamid");

        verify(taskRepository).delete(task);
    }
}