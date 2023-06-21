package com.intelektualcicii.todoapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelektualcicii.todoapp.R;
import com.intelektualcicii.todoapp.Interface.SelectItemListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView taskText,dueDate,hardcodedDueDate;
    ImageView priority;
    ImageView notDoneCircle;
    SelectItemListener selectItemListener;
    LinearLayout rv_background;

    public TaskViewHolder(@NonNull View itemView, SelectItemListener selectItemListener) {
        super(itemView);

        this.rv_background=itemView.findViewById(R.id.rv_background);
        this.taskText=itemView.findViewById(R.id.taskText_rv_task);
        this.priority=itemView.findViewById(R.id.priority_rv_task);
        this.dueDate=itemView.findViewById(R.id.dueDate_rv_task);
        this.notDoneCircle=itemView.findViewById(R.id.recyclerview_notDone_circle);
        this.hardcodedDueDate=itemView.findViewById(R.id.rv_home_task_hardcoded_dueText);

        this.selectItemListener=selectItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        selectItemListener.onItemClick(getAdapterPosition());
    }
}
