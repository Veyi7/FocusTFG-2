package org.focus.api.controller;

import org.focus.api.model.MiniTask;
import org.focus.api.model.Task;
import org.focus.api.model.TaskResponse;
import org.focus.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.swing.text.DateFormatter;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

@CrossOrigin(origins = {"http://192.168.1.90:3000"})
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

        String time = (timestamp.toString()).substring(0,19);
        System.out.println(time);
        task.setCreationDateTime(time);

        //Time of the task

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        formatoEntrada.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        try {
            Date aux = formatoEntrada.parse(data);
            String aux2 = formatoSalida.format(aux);
            System.out.println(aux2);
            task.setStartDateTime(aux2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //User_id

        task.setUserid(user_id);

        int id = taskService.createTask(task);
        //System.out.println(id);
        TaskResponse responseTask = new TaskResponse(id, "Task created");
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
    public void deleteTask(@RequestParam Integer id){
        taskService.deleteTask(id);
    }

    @PostMapping("/task/update")
    public void updateTask(@RequestParam Integer id, String title, String description, String date, Boolean done){
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDone(done);

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        formatoEntrada.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        try {
            Date aux = formatoEntrada.parse(date);
            String aux2 = formatoSalida.format(aux);
            System.out.println(aux2);
            task.setStartDateTime(aux2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskService.updateTask(task);
    }

    @PostMapping("/task/update/minitask")
    public void updateMiniTask(@RequestParam Integer id, String title, Boolean done) {
        MiniTask mt = new MiniTask();
        mt.setTitle(title);
        mt.setId(id);
        mt.setDone(done);

        taskService.updateMiniTask(mt);
    }

    @GetMapping("/test")
    public String getTest() {
        LocalDateTime hora = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(hora);
        System.out.println(timestamp);
        return timestamp.toString();
    }
}
