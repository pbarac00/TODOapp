package com.intelektualcicii.todoapp.DataAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.intelektualcicii.todoapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskText,startedDate,dueDate;
    ImageView priority;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        this.taskText=itemView.findViewById(R.id.taskText_rv_task);
        this.priority=itemView.findViewById(R.id.priority_rv_task);
        this.startedDate=itemView.findViewById(R.id.startedDate_rv_task);
        this.dueDate=itemView.findViewById(R.id.dueDate_rv_task);
    }
}
