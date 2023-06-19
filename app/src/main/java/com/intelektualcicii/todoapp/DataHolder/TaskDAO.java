package com.intelektualcicii.todoapp.DataHolder;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task")
    List<Task> getAll();
    @Insert
    void insertAll(Task... tasks);
    @Delete
    void delete(Task task);
    @Update(entity = Task.class)
    void updateTask(Task...tasks);
}
