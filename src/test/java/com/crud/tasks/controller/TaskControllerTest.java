package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.domain.TrelloListDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DbService dbService;
    @MockBean
    private TaskController taskController;
    @MockBean
    private TaskMapper taskMapper;

    @Test
    void shouldGetTask() throws Exception {
        List<Task> tasks = List.of(new Task(1L, "title", "content"));
        when(dbService.getAllTasks()).thenReturn(tasks);
        when(taskMapper.mapToTaskDtoList(any())).thenReturn(List.of(new TaskDto(1L, "title", "content")));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("content")));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        List<Task> tasks = List.of(new Task(1L, "title", "content"));
        dbService.deleteTask(tasks.get(0).getId());


        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/tasks/{taskId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void shouldUpdateTask() throws Exception {

        Task updatedTask = new Task(2L, "2title", "2content");

// tutaj miałem problem ale czat gpt podpowiedział że musze przekształcić Task na JSON
   ObjectMapper objectMapper = new ObjectMapper();
   String updatedTaskJson = objectMapper.writeValueAsString(updatedTask);

        when(dbService.saveTask(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.mapToTaskDtoList(any())).thenReturn(List.of(new TaskDto(1L, "title", "content")));


        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/tasks")
                        .content(updatedTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    void shouldCreateTask() throws Exception {

        Task newTask = new Task(3L, "3title", "3content");

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedTaskJson = objectMapper.writeValueAsString(newTask);


        when(dbService.saveTask(newTask)).thenReturn(newTask);
        when(taskMapper.mapToTaskDtoList(any())).thenReturn(List.of(new TaskDto(1L, "3title", "3content")));


        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/tasks")
                        .content(updatedTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

