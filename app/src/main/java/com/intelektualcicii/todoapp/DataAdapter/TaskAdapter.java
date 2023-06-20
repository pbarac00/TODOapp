package com.intelektualcicii.todoapp.DataAdapter;

import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;
import com.intelektualcicii.todoapp.SelectItemListener;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    List<Task> tasks;
    TaskDatabase db;
    private SelectItemListener listener;
    private ExecutorService executorsService= Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());


    public TaskAdapter(List<Task> tasks, SelectItemListener listener) {
        this.tasks = tasks;
        this.listener=listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_home_tasks_card,parent,false);
        return new TaskViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.taskText.setText(tasks.get(position).taskName);

        if (tasks.get(holder.getAdapterPosition()).priority==1)
        {
            holder.priority.setImageResource(R.drawable.med_priority_yellow);
            holder.rv_background.setBackgroundResource(R.drawable.priority_medium_backround_rv);
        }
        else if (tasks.get(holder.getAdapterPosition()).priority==2)
        {
            holder.priority.setImageResource(R.drawable.high_priority_red);
            holder.rv_background.setBackgroundResource(R.drawable.priority_high_background);
        }

        if (tasks.get(holder.getAdapterPosition()).isFinished==true)
        {
            holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.notDoneCircle.setImageResource(R.drawable.done_circle);
        }

        //provjerava da li je dueDate postavljen ili mu je vrijednost ""
        //inicijalna vrijednost je "" jer se datumi sortiraju pa mora imat neku vrijednost
        if (tasks.get(holder.getAdapterPosition()).dueDate.length()>0){
            holder.hardcodedDueDate.setText("Due date: ");
            holder.dueDate.setText(tasks.get(holder.getAdapterPosition()).dueDate);

        }

        //animation in rv and deleting object from database
        holder.notDoneCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.notDoneCircle.setImageResource(R.drawable.done_circle);
                holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                executorsService.execute(new Runnable() {
                    @Override
                    public void run() {
                        db= Room.databaseBuilder(v.getContext(), TaskDatabase.class, "task").
                                fallbackToDestructiveMigration().build();
                      // db.taskDAO().delete(tasks.get(holder.getAdapterPosition()));
                        Task task = tasks.get(holder.getAdapterPosition());
                        task.setFinished(true);
                        db.taskDAO().updateTask(task);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        removeAt(holder.getAdapterPosition());
                                    }
                                }, 500);
                            }
                        });
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    private void removeAt(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tasks.size());
    }
}
