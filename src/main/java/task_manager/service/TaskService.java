package task_manager.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task_manager.model.AppUser;
import task_manager.model.Task;
import task_manager.repository.TaskRepository;
import task_manager.repository.UserRepository;
import task_manager.exception.TaskNotFoundException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private AppUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        ));
    }

    @Transactional(readOnly = true)
    public List<Task> getTasks(String username) {
        AppUser user = getUser(username);
        return taskRepository.findByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id, String username) {
        AppUser user = getUser(username);

        return taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with ID: " + id
                        ));
    }

    @Transactional
    public Task createTask(Task task, String username) {
        AppUser user = getUser(username);

        task.setUser(user);

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task updatedTask, String username) {
        Task existingTask = getTaskById(id, username);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Long id, String username) {
        Task task = getTaskById(id, username);
        taskRepository.delete(task);
    }
}