package com.intelektualcicii.todoapp.DataHolder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class},version = 4)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}
