
package task_manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import task_manager.model.Task;
import task_manager.repository.TaskRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = taskRepository.save(
                new Task(null, "Integration Test Task", "Test description", false)
        );
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteById(testTask.getId());
    }

    @Test
    void testGetTasks() throws Exception {

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ));
    }

    @Test
    void testGetTaskById() throws Exception {

        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
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

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Learn Integration Testing"));
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

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Master Spring Boot"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testDeleteTask() throws Exception {

        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
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

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testGetTaskNotFound() throws Exception {

        mockMvc.perform(get("/api/tasks/9999"))
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

        mockMvc.perform(put("/api/tasks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isNotFound());
    }


    @Test
    void testDeleteTaskNotFound() throws Exception {

        mockMvc.perform(delete("/api/tasks/9999"))
                .andExpect(status().isNotFound());
    }

}
