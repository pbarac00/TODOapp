package com.intelektualcicii.todoapp.DataHolder;

public class Task {
    public String taskName;
    public Integer priority; // low-0, medium-1, high-2
    boolean isFinished;

    public Task() {
    }

    public Task(String taskName, Integer priority, boolean isFinished) {
        this.taskName = taskName;
        this.priority = priority;
        this.isFinished = isFinished;
    }
}
