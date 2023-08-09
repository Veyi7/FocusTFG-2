package org.focus.api.controller;

import org.focus.api.model.Task;
import org.focus.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

@RestController
public class TaskController {

    private TaskService taskService;


    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/task")
    public Task getTask(@RequestParam Integer id){
        return taskService.getTask(id);
    }

    @GetMapping("/task/all")
    public List<Task> getAllTask(){
        return taskService.getAllTasks();
    }

    @PostMapping("/task/new")
    public void createTask(@RequestParam String title, String description) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        //Creation Time
        Date fecha = new Date();
        Timestamp timestamp = new Timestamp(fecha.getTime());
        System.out.println(timestamp);
        task.setCreationDateTime(timestamp);

        //Time of the task
        timestamp = new Timestamp(fecha.getTime());
        System.out.println(timestamp);
        task.setStartDateTime(timestamp);
        taskService.createTask(task);
    }

    @DeleteMapping("/task/delete")
    public void deleteTask(@RequestParam Integer id){
        taskService.deleteTask(id);
    }

    @PostMapping("/task/update")
    public void updateTask(@RequestParam Task task){
        taskService.updateTask(task);
    }
    @GetMapping("/test")
    public String getTest() {
        return "Hello World!";
    }
}
