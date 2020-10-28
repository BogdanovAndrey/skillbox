package main.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.controllers.TaskController;
import main.model.Task;
import main.model.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)

public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private TaskRepository taskRepository;


    @Before
    public void setUp() {

        Task taskOne = new Task();
        taskOne.setId(1);
        taskOne.setName("task 1");
        taskOne.setDescription("This is first task");
        Task taskTwo = new Task();
        taskTwo.setId(2);
        taskTwo.setName("task 2");
        taskTwo.setDescription("This is second task");
        Task taskThree = new Task();
        taskThree.setName("task3");
        taskThree.setDescription("description3");
        Task taskThreeModified = new Task();
        taskThreeModified.setName("task3");
        taskThreeModified.setDescription("description3");
        taskThreeModified.setId(3);
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskOne);
        taskList.add(taskTwo);
        given(taskRepository.findAll()).willReturn(taskList);
        given(taskRepository.findById(1)).willReturn(java.util.Optional.of(taskOne));
        given(taskRepository.save(any(Task.class))).willReturn(taskThreeModified);
        given(taskRepository.existsById(taskOne.getId())).willReturn(true);
        given(taskRepository.existsById(taskThreeModified.getId())).willReturn(true);
    }

    @Test
    public void testGet() throws Exception {
        //Тест запроса всех записей
        mockMvc.perform(get("/tasks/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"task 1\"")))
                .andExpect(content().string(containsString("\"name\":\"task 2\"")));

        //Тест запроса одной записи
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"task 1\",\"description\":\"This is first task\"}"));
        //Тест запроса одной записи (отсутствующей)
        mockMvc.perform(get("/tasks/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPost() throws Exception {

        //Тест записи
        mockMvc.perform(post("/tasks/")
                .param("name", "test3")
                .param("description", "description3"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("3")));
    }

    @Test
    public void testDelete() throws Exception {
        //Тест удаления существующей записи
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());
        //Тест удаления несуществующей записи
        mockMvc.perform(delete("/tasks/4"))
                .andExpect(status().isNotFound());
        //Тест удаления всех записей
        mockMvc.perform(delete("/tasks/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPut() throws Exception {

        //Тест обновления
        mockMvc.perform(put("/tasks/3")
                .param("name", "test3")
                .param("description", "new_description3"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("3")));

        //Тест обновления
        mockMvc.perform(put("/tasks/4")
                .param("name", "test4")
                .param("description", "new_description3"))
                .andExpect(status().isNotFound());


        List<Task> taskList = new ArrayList<>(2);
        taskList.add(new Task());
        taskList.add(new Task());

        //Тест обновления (правильный)
        mockMvc.perform(put("/tasks/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(taskList)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(taskList.size()))));

        //Тест обновления (неправильный)
        mockMvc.perform(put("/tasks/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new Task())))
                .andExpect(status().isBadRequest());
    }
}

