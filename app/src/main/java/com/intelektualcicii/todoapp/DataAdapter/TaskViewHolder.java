package com.intelektualcicii.todoapp.DataAdapter;

import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskText,dueDate,hardcodedDueDate;
    ImageView priority;
    ImageView notDoneCircle;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        this.taskText=itemView.findViewById(R.id.taskText_rv_task);
        this.priority=itemView.findViewById(R.id.priority_rv_task);
        this.dueDate=itemView.findViewById(R.id.dueDate_rv_task);
        this.notDoneCircle=itemView.findViewById(R.id.recyclerview_notDone_circle);
        this.hardcodedDueDate=itemView.findViewById(R.id.rv_home_task_hardcoded_dueText);
    }

}
