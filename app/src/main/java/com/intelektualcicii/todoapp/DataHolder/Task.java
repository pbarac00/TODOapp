package com.intelektualcicii.todoapp.DataHolder;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.time.LocalDate;

import java.util.UUID;

@Entity
public class Task implements Parcelable {

    @PrimaryKey @NonNull
    public String taskId;

    @ColumnInfo(name="taskName")
    public String taskName;

    @ColumnInfo(name="priority")
    public Integer priority; // low-0, medium-1, high-2

    @ColumnInfo(name="isFinished")
    public boolean isFinished;

    @ColumnInfo(name="startedDate")
    public String startedDate;

    public String dueDate;


    public Task() {
    }

    public Task(String taskName, Integer priority, boolean isFinished, String taskId, String startedDate) {
        this.taskName = taskName;
        this.priority = priority;
        this.isFinished = isFinished;
        this.taskId= taskId;
        this.startedDate=startedDate;
    }

    //From here to end of class is the implementation of Parcelable methods,
    // all of them are automatically generated when implementing interface
    protected Task(Parcel in) {
        taskId = in.readString();
        taskName = in.readString();
        if (in.readByte() == 0) {
            priority = null;
        } else {
            priority = in.readInt();
        }
        isFinished = in.readByte() != 0;
        startedDate = in.readString();
        dueDate = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(taskId);
        dest.writeString(taskName);
        if (priority == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(priority);
        }
        dest.writeByte((byte) (isFinished ? 1 : 0));
        dest.writeString(startedDate);
        dest.writeString(dueDate);
    }
}
