package com.intelektualcicii.todoapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    EditText et_taskText_task_detail;
    SwitchMaterial switch_doneOnOff_task_detail;
    ImageView iv_priorityLow_task_detail,iv_priorityMed_task_detail,
            iv_priorityHigh_task_detail,iv_editDueDate_task_detail;
    TextView tv_dueDate_task_detail,tv_createdDate_task_detail;
    Button bt_updateTask_task_detail, bt_deleteTask_task_detail;
    TaskDatabase db;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        // This function is used to initialize the widgets (views) used in this activity.
        initializeWidgets();

        //This block of code initialize db instance.
        db= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "task").
                fallbackToDestructiveMigration().build();

        //This block of code gets object task that is passed in this activity trough intent.
        Intent intent= getIntent();
        Task task = intent.getParcelableExtra("task");

        // Sets values of task in widgets.
        setTaskValuesInWidgets(task);

        // Applies effect like crossed out text, and switch on for finished tasks.
        applyInitialEffectsOnUI(task);



        //botuni update i delete onCLick....
        bt_updateTask_task_detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ubacit logiku za updejt
                String taskText=et_taskText_task_detail.getText().toString();
                if(TextUtils.isEmpty(taskText) ) {
                    Toast.makeText(TaskDetailActivity.this, "Please enter task text", Toast.LENGTH_LONG).show();
                }
                else {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            task.taskName = taskText;

                            db.taskDAO().updateTask(task);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TaskDetailActivity.this, "TO-DO updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TaskDetailActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });


        bt_deleteTask_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(TaskDetailActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Do you wnat to delete note?")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                db.taskDAO().delete(task);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(TaskDetailActivity.this, HomeActivity.class);
                                        Toast.makeText(getApplicationContext(), "Note is deleted", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }



    private void initializeWidgets(){
        et_taskText_task_detail=findViewById(R.id.et_taskText_task_detail);
        switch_doneOnOff_task_detail=findViewById(R.id.switch_doneOnOff_task_detail);

        iv_priorityLow_task_detail=findViewById(R.id.iv_priorityLow_task_detail);
        iv_priorityMed_task_detail=findViewById(R.id.iv_priorityMed_task_detail);
        iv_priorityHigh_task_detail=findViewById(R.id.iv_priorityHigh_task_detail);

        iv_editDueDate_task_detail=findViewById(R.id.iv_editDueDate_task_detail);
        tv_dueDate_task_detail=findViewById(R.id.tv_dueDate_task_detail);
        tv_createdDate_task_detail=findViewById(R.id.tv_createdDate_task_detail);
        bt_updateTask_task_detail=findViewById(R.id.bt_updateTask_task_detail);
        bt_deleteTask_task_detail=findViewById(R.id.bt_deleteTask_task_detail);
    }

    private void setTaskValuesInWidgets(Task task) {
        et_taskText_task_detail.setText(task.getTaskName());
        tv_dueDate_task_detail.setText(task.getDueDate());
        tv_createdDate_task_detail.setText(task.getStartedDate());
    }

    private void applyInitialEffectsOnUI(Task task)
    {
        switch (task.priority)
        {
            case 0:
                iv_priorityLow_task_detail.setImageResource(R.drawable.low_priority_dark);
                break;
            case 1:
                iv_priorityMed_task_detail.setImageResource(R.drawable.medium_priority_dark);
                break;
            case 2:
                iv_priorityHigh_task_detail.setImageResource(R.drawable.high_priority_dark);
                break;
        }


        if (task.isFinished==true)
        {
            et_taskText_task_detail.setPaintFlags(et_taskText_task_detail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            switch_doneOnOff_task_detail.setOnCheckedChangeListener (null);
            switch_doneOnOff_task_detail.setChecked(true);
        }
    }
}