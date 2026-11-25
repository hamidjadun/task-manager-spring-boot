package task_manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.http.MediaType;

import task_manager.model.Task;
import task_manager.model.AppUser;
import task_manager.repository.TaskRepository;
import task_manager.repository.UserRepository;
import task_manager.security.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private Task testTask;
    private String token;

    private static final String TEST_USERNAME = "integration_test_user";

    @BeforeEach
    void setUp() {

        AppUser user = userRepository.findByUsername(TEST_USERNAME)
                .orElseGet(() -> userRepository.save(
                        new AppUser(
                                TEST_USERNAME,
                                "$2a$10$abcdefghijklmnopqrstuu",
                                "USER"
                        )
                ));

        token = jwtService.generateToken(TEST_USERNAME);

        testTask = new Task(
                null,
                "Integration Test Task",
                "Test description",
                false
        );

        testTask.setUser(user);

        testTask = taskRepository.save(testTask);
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteById(testTask.getId());
    }

    private MockHttpServletRequestBuilder withAuth(
            MockHttpServletRequestBuilder request) {

        return request.header("Authorization", "Bearer " + token);
    }

    @Test
    void testGetTasks() throws Exception {

        mockMvc.perform(withAuth(get("/api/tasks")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetTaskById() throws Exception {

        mockMvc.perform(withAuth(
                        get("/api/tasks/" + testTask.getId())))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTask() throws Exception {

        String taskJson = """
                {
                    "title": "Learn Integration Testing",
                    "description": "Practice MockMvc",
                    "completed": false
                }
                """;

        mockMvc.perform(withAuth(post("/api/tasks"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Learn Integration Testing"));
    }

    @Test
    void testUpdateTask() throws Exception {

        String updatedTaskJson = """
                {
                    "title": "Master Spring Boot",
                    "description": "Practice integration testing",
                    "completed": true
                }
                """;

        mockMvc.perform(withAuth(
                        put("/api/tasks/" + testTask.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Master Spring Boot"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testDeleteTask() throws Exception {

        mockMvc.perform(withAuth(
                        delete("/api/tasks/" + testTask.getId())))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateTaskWithInvalidData() throws Exception {

        String taskJson = """
                {
                    "title": "",
                    "description": "Testing invalid data",
                    "completed": false
                }
                """;

        mockMvc.perform(withAuth(post("/api/tasks"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTaskNotFound() throws Exception {

        mockMvc.perform(withAuth(get("/api/tasks/9999")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTaskNotFound() throws Exception {

        String updatedTaskJson = """
                {
                    "title": "Updated Task",
                    "description": "Testing missing task",
                    "completed": true
                }
                """;

        mockMvc.perform(withAuth(put("/api/tasks/9999"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTaskNotFound() throws Exception {

        mockMvc.perform(withAuth(delete("/api/tasks/9999")))
                .andExpect(status().isNotFound());
    }
}