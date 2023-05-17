package com.intelektualcicii.todoapp.DataAdapter;

import android.view.View;
import android.widget.TextView;
import com.intelektualcicii.todoapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskName, priority;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        this.taskName=(TextView)itemView.findViewById(R.id.taskName);
        this.priority=(TextView) itemView.findViewById(R.id.priority);
    }
}
