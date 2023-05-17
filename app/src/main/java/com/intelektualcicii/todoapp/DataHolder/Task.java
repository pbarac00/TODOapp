package com.intelektualcicii.todoapp.DataHolder;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Task {
    @PrimaryKey @NonNull
    public String taskId;
    @ColumnInfo(name="taskName")
    public String taskName;
    @ColumnInfo(name="priority")
    public Integer priority; // low-0, medium-1, high-2
    @ColumnInfo(name="isFinished")
    public boolean isFinished;


    public Task() {
    }

    public Task(String taskName, Integer priority, boolean isFinished, String taskId) {
        this.taskName = taskName;
        this.priority = priority;
        this.isFinished = isFinished;
        this.taskId= taskId;
    }

    @NonNull
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(@NonNull String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
