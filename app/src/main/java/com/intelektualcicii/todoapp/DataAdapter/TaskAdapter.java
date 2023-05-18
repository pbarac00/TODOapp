package com.intelektualcicii.todoapp.DataAdapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    List<Task> tasks;
    TaskDatabase db;
    private ExecutorService executorsService= Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());


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

        if (tasks.get(holder.getAdapterPosition()).priority==1)
        {
            holder.priority.setImageResource(R.drawable.med_priority);
        }
        else if (tasks.get(holder.getAdapterPosition()).priority==2)
        {
            holder.priority.setImageResource(R.drawable.high_priority);
        }

        if (tasks.get(holder.getAdapterPosition()).dueDate!=null){
            holder.hardcodedDueDate.setText("Due date: ");
            holder.dueDate.setText(tasks.get(holder.getAdapterPosition()).dueDate);
        }

        //animation in rv and deleting object from database
        holder.notDoneCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.notDoneCircle.setImageResource(R.drawable.done_circle);
                executorsService.execute(new Runnable() {
                    @Override
                    public void run() {
                        db= Room.databaseBuilder(v.getContext(), TaskDatabase.class, "task").
                                fallbackToDestructiveMigration().build();
                        db.taskDAO().delete(tasks.get(holder.getAdapterPosition()));

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
