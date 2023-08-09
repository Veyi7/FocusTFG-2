package org.focus.api.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Task {
    private int id;
    private String title;
    private String description;
    private Timestamp startDateTime;
    private Timestamp creationDateTime;
    private boolean done;
    private int userid;

    private List<MiniTask> miniTasks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Timestamp creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<MiniTask> getMiniTasks() {
        return miniTasks;
    }

    public void setMiniTasks(List<MiniTask> miniTasks) {
        this.miniTasks = miniTasks;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

}
