package org.focus.api.controller;

import org.focus.api.model.MiniTask;
import org.focus.api.model.Task;
import org.focus.api.model.TaskResponse;
import org.focus.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;

import java.util.List;
import java.sql.Timestamp;

@CrossOrigin(origins = {"https://focusapp-c58cf.web.app"})
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

    @GetMapping("/task/user")
    public List<Task> getAllTasksUser(@RequestParam String user_id) {
        return taskService.getAllTasksUser(user_id);
    }

    @PostMapping("/task/new")
    public ResponseEntity<TaskResponse> createTask(@RequestParam String title, String description, String data, String user_id) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);

        //Creation Time

        LocalDateTime hora = LocalDateTime.now();

        Timestamp timestamp = Timestamp.valueOf(hora);

        System.out.println(timestamp.toString());
        task.setCreationDateTime(timestamp.toString());

        //Time of the task

        task.setStartDateTime(data);

        //User_id

        task.setUserid(user_id);

        int id = taskService.createTask(task);

        TaskResponse responseTask;
        if (id == 0) {
            responseTask = new TaskResponse(id, "ERROR");
        }
        else {
            System.out.println(id);
            responseTask = new TaskResponse(id, "Task created");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseTask);
    }

    @PostMapping("/task/new/minitask")
    public void createMiniTask(@RequestParam Integer id, String title) {
        MiniTask miniTask = new MiniTask();
        miniTask.setTitle(title);
        miniTask.setTaskId(id);

        taskService.createMiniTask(miniTask);
    }

    @DeleteMapping("/task/delete")
    public TaskResponse deleteTask(@RequestParam Integer id) {
        int ra = taskService.deleteTask(id);
        TaskResponse rt;
        if (ra>0) {
            rt = new TaskResponse(ra, "Task Erased");
        }
        else {
            rt = new TaskResponse(0, "ERROR");
        }

        return rt;
    }

    @PostMapping("/task/update")
    public TaskResponse updateTask(@RequestParam Integer id, String title, String description, String date, Boolean done){
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDone(done);

        //Time of the task

        task.setStartDateTime(date);

        int ra = taskService.updateTask(task);
        TaskResponse rt;
        if (ra>0) {
            rt = new TaskResponse(ra, "Task Erased");
        }
        else {
            rt = new TaskResponse(0, "ERROR");
        }

        return rt;
    }

    @PostMapping("/task/update/minitask")
    public TaskResponse updateMiniTask(@RequestParam Integer id, String title, Boolean done) {
        MiniTask mt = new MiniTask();
        mt.setTitle(title);
        mt.setId(id);
        mt.setDone(done);

        int ra = taskService.updateMiniTask(mt);
        TaskResponse rt;
        if (ra>0) {
            rt = new TaskResponse(ra, "Task Erased");
        }
        else {
            rt = new TaskResponse(0, "ERROR");
        }

        return rt;
    }

    @GetMapping("/test")
    public String getTest() {
        LocalDateTime hora = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(hora);
        System.out.println(timestamp);
        return timestamp.toString();
    }
}
