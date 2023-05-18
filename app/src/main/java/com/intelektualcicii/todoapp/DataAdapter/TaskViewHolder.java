package com.intelektualcicii.todoapp.DataAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskText,startedDate,dueDate;
    ImageView priority;
    ImageView notDoneCircle;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        this.taskText=itemView.findViewById(R.id.taskText_rv_task);
        this.priority=itemView.findViewById(R.id.priority_rv_task);
        this.dueDate=itemView.findViewById(R.id.dueDate_rv_task);
        this.notDoneCircle=itemView.findViewById(R.id.recyclerview_notDone_circle);




        notDoneCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notDoneCircle.setImageResource(R.drawable.done_circle);


            }
        });



    }
    public void deleteTask(int position){


    }
}
