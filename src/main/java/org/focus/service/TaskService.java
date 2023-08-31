package org.focus.service;

import org.focus.api.model.MiniTask;
import org.focus.api.model.Task;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private List<Task> taskList;

    public TaskService() {
        taskList = new ArrayList<>();
    }


    private String url = "jdbc:postgresql://localhost:5432/focus";
    private String user = "postgres";
    private String password = "FIB1234fib!";

    public Task getTask(Integer id) {
        Task task = new Task();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tasks WHERE id = "+ id)) {
                rs.next();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDateTime(rs.getTimestamp("start_date_time").toString());
                task.setCreationDateTime(rs.getTimestamp("creation_date_time").toString());
                task.setDone(rs.getBoolean("done"));
                task.setUserid(rs.getInt("user_id"));
                List<MiniTask> minis = new ArrayList<>();
                try (Connection conn2 = DriverManager.getConnection(url, user, password);
                     Statement stmt2 = conn2.createStatement();
                     ResultSet rs2 = stmt2.executeQuery("SELECT * FROM miniTasks WHERE taskid = " + task.getId())) {
                    while (rs2.next()) {
                        MiniTask mini = new MiniTask();
                        mini.setId(rs2.getInt("id"));
                        mini.setTitle(rs2.getString("title"));
                        mini.setDone(rs2.getBoolean("done"));
                        mini.setTaskId(rs2.getInt("taskId"));
                        minis.add(mini);
                    }
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                task.setMiniTasks(minis);

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tasks")) {
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDateTime(rs.getTimestamp("start_date_time").toString());
                task.setCreationDateTime(rs.getTimestamp("creation_date_time").toString());
                task.setDone(rs.getBoolean("done"));
                task.setUserid(rs.getInt("user_id"));
                List<MiniTask> minis = new ArrayList<>();
                try (Connection conn2 = DriverManager.getConnection(url, user, password);
                     Statement stmt2 = conn2.createStatement();
                     ResultSet rs2 = stmt2.executeQuery("SELECT * FROM minitasks WHERE taskid = " + task.getId())) {
                    while (rs2.next()) {
                        MiniTask mini = new MiniTask();
                        mini.setId(rs2.getInt("id"));
                        mini.setTitle(rs2.getString("title"));
                        mini.setDone(rs2.getBoolean("done"));
                        mini.setTaskId(rs2.getInt("taskId"));
                        minis.add(mini);
                    }
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                task.setMiniTasks(minis);
                tasks.add(task);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public int createTask(Task task) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("insert into tasks(title, description, start_date_time, creation_date_time, user_id) values ('"+task.getTitle()+"','"+task.getDescription()+"','"+task.getStartDateTime()+"','"+task.getCreationDateTime()+"',"+234+") returning id")) {
             rs.next();
             int id = rs.getInt("id");
             return id;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTask(Task task) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("update tasks set title = '"+ task.getTitle() +"', description = '"+task.getDescription()+"', start_date_time = '"+task.getStartDateTime()+"', done = "+task.isDone()+" where id = "+task.getId()+" returning id;")) {
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTask(int id) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("delete from minitasks where taskid ="+id+" returning id;")) {
            try (Connection conn2 = DriverManager.getConnection(url, user, password);
                 Statement stmt2 = conn2.createStatement();
                 ResultSet rs2 = stmt2.executeQuery("delete from tasks where id ="+id+" returning id;")) {
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMiniTask(MiniTask miniTask) {
        try (Connection conn2 = DriverManager.getConnection(url, user, password);
             Statement stmt2 = conn2.createStatement();
             ResultSet rs2 = stmt2.executeQuery("insert into minitasks(title, taskid) values ('" + miniTask.getTitle() + "'," + miniTask.getTaskId() + ") returning id")) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateMiniTask(MiniTask mt) {
        try (Connection conn2 = DriverManager.getConnection(url, user, password);
             Statement stmt2 = conn2.createStatement();
             ResultSet rs2 = stmt2.executeQuery("update minitasks set title = '" + mt.getTitle() + "', done = " + mt.isDone() + " where id = " + mt.getId() + " returning id;")) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
