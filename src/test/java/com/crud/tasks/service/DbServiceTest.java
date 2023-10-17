package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DbServiceTest {
@Autowired
    private TaskRepository repository;


private DbService dbService;

@AfterEach
public void clearUp() {
    repository.deleteAll();
}


    @BeforeEach
    public void setUp() {
        Task task1 = new Task(1L,"111","111");
        repository.save(task1);

    }

    @Test
    void getAllTasks() {
       List<Task>tasks =repository.findAll();
       assertEquals(1,tasks.size());
        repository.deleteAll();

    }

    @Test
    void saveTask() {
        repository.save(new Task(2L,"TestTitle 2","content 2"));

        assertEquals(2,repository.findAll().size());
        assertEquals("TestTitle 2",repository.findAll().get(1).getTitle());

repository.deleteAll();
    }

    @Test
    void deleteTask() {
        repository.save(new Task(3L,"TestTitle 3","content 3"));

        repository.deleteById(repository.findAll().get(1).getId());

        assertEquals(1,repository.findAll().size());

        repository.deleteAll();
    }



}