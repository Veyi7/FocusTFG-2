package org.focus.service;

import org.focus.api.model.MiniTask;
import org.focus.api.model.Task;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Service
public class TaskService {

    private List<Task> taskList;

    public TaskService() {
        taskList = new ArrayList<>();
    }


    //private String url = "jdbc:postgresql://localhost:5432/focus";
    //private String user = "postgres";
    //private String password = "FIB1234fib!";

    private String url = "jdbc:postgresql://containers-us-west-143.railway.app:6311/railway";
    private String user = "postgres";
    private String password = "hxxEPWbYMw99VZFl4mjB";

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
                task.setUserid(rs.getString("user_id"));
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
                task.setUserid(rs.getString("user_id"));
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

    public List<Task> getAllTasksUser(String userid) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tasks WHERE user_id = '"+userid+"'")) {
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDateTime(rs.getTimestamp("start_date_time").toString());
                task.setCreationDateTime(rs.getTimestamp("creation_date_time").toString());
                task.setDone(rs.getBoolean("done"));
                task.setUserid(rs.getString("user_id"));
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
             PreparedStatement ps = conn.prepareStatement(
                     "insert into tasks(title, description, start_date_time, creation_date_time, user_id) values (?, ?, ?, ?, ?) returning id"
             )) {
                ps.setString(1, task.getTitle());
                ps.setString(2, task.getDescription());
                System.out.println(task.getStartDateTime());
                System.out.println(task.getCreationDateTime());
                ps.setTimestamp(3, Timestamp.valueOf(task.getStartDateTime()));
                ps.setTimestamp(4, Timestamp.valueOf(task.getCreationDateTime()));
                ps.setString(5, task.getUserid());
                boolean hasResultSet = ps.execute();
                if (!hasResultSet) {
                    int rowsAffected = ps.getUpdateCount();
                    System.out.println(rowsAffected);
                } else {
                    ResultSet resultSet = ps.getResultSet();
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        System.out.println(id);
                        return id;
                    }
                }
                return 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateTask(Task task) {

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

             String sql = "update tasks set title = '"+ task.getTitle() +"', description = '"+task.getDescription()+"', start_date_time = '"+task.getStartDateTime()+"', done = "+task.isDone()+" where id = "+task.getId()+";";
             PreparedStatement ps = conn.prepareStatement(sql);

             int rowsAffected = ps.executeUpdate();
             return rowsAffected;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteTask(int id) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
             String sql = "delete from minitasks where taskid ="+id+";";
             PreparedStatement ps = conn.prepareStatement(sql);
             int rowsAffected = ps.executeUpdate();
            try (Connection conn2 = DriverManager.getConnection(url, user, password);
                 Statement stmt2 = conn2.createStatement()) {
                 String sql2 = "delete from tasks where id ="+id+";";
                 PreparedStatement ps2 = conn.prepareStatement(sql2);
                 int ra2 = ps2.executeUpdate();
                 return ra2;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int createMiniTask(MiniTask miniTask) {
        try (Connection conn2 = DriverManager.getConnection(url, user, password);
             Statement stmt2 = conn2.createStatement()) {
             String sql = "insert into minitasks(title, taskid) values ('" + miniTask.getTitle() + "'," + miniTask.getTaskId() + ")";
             PreparedStatement ps = conn2.prepareStatement(sql);

             int rowsAffected = ps.executeUpdate();
             return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int updateMiniTask(MiniTask mt) {
        try (Connection conn2 = DriverManager.getConnection(url, user, password);
             Statement stmt2 = conn2.createStatement()) {
             String sql = "update minitasks set title = '" + mt.getTitle() + "', done = " + mt.isDone() + " where id = " + mt.getId() + ";";
             PreparedStatement ps = conn2.prepareStatement(sql);

             int rowsAffected = ps.executeUpdate();
             return rowsAffected;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
