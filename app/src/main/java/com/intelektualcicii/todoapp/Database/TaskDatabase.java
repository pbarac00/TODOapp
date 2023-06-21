package com.intelektualcicii.todoapp.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.intelektualcicii.todoapp.Dao.TaskDAO;
import com.intelektualcicii.todoapp.Model.Task;

@Database(entities = {Task.class},version = 4)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}
