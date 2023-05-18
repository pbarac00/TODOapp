package com.intelektualcicii.todoapp.DataAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.R;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    List<Task> tasks;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_home_tasks_card,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.taskText.setText(tasks.get(position).taskName);
        holder.dueDate.setText(tasks.get(position).dueDate);
        if (tasks.get(position).priority==1)
        {
            holder.priority.setImageResource(R.drawable.med_priority);
        }
        else if (tasks.get(position).priority==2)
        {
            holder.priority.setImageResource(R.drawable.high_priority);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
