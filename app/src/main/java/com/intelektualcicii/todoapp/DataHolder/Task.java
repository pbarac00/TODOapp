package com.intelektualcicii.todoapp.DataHolder;

import java.util.UUID;

public class Task {
    public String taskName;
    public Integer priority; // low-0, medium-1, high-2
    public boolean isFinished;
    public String taskId;

    public Task() {
    }

    public Task(String taskName, Integer priority, boolean isFinished, String taskId) {
        this.taskName = taskName;
        this.priority = priority;
        this.isFinished = isFinished;
        this.taskId= taskId;
    }
}
