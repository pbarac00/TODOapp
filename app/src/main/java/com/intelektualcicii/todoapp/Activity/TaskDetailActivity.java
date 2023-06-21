package com.intelektualcicii.todoapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.intelektualcicii.todoapp.Model.Task;
import com.intelektualcicii.todoapp.Database.TaskDatabase;
import com.intelektualcicii.todoapp.R;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    EditText et_taskText_task_detail;
    SwitchMaterial switch_doneOnOff_task_detail;
    ImageView iv_priorityLow_task_detail,iv_priorityMed_task_detail,
            iv_priorityHigh_task_detail,iv_editDueDate_task_detail,iv_back_task_detail;
    TextView tv_dueDate_task_detail,tv_createdDate_task_detail;
    Button bt_updateTask_task_detail, bt_deleteTask_task_detail;
    TaskDatabase db;
    Boolean isDueDateSet;
    Calendar calendar;
    int currYear,currMonth,currDay;
    String dueDate;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        // This function is used to initialize the widgets (views) used in this activity.
        initializeWidgets();

        //This function is used to initialize variables.
        initializeVariables();

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

        // Adds listener's on every widget item in this activity
        // and following code what to do when widget is clicked
        addListenersOnWidgets(task);



    }

    private void addListenersOnWidgets(Task task) {
        // logic behind click on update button
        bt_updateTask_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText=et_taskText_task_detail.getText().toString();
                if(TextUtils.isEmpty(taskText) ) {
                    Toast.makeText(TaskDetailActivity.this, "Please enter task text", Toast.LENGTH_LONG).show();
                }
                else {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            task.taskName = taskText;
                            if (isDueDateSet)  task.setDueDate(dueDate);

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


        // logic behind click on delete button
        bt_deleteTask_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(TaskDetailActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete note?")
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
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        iv_back_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogOnBackPressed();
            }
        });


        iv_priorityLow_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                iv_priorityLow_task_detail.setImageResource(R.drawable.low_priority_dark);
                et_taskText_task_detail.setBackgroundResource(R.drawable.priority_low_backround_rv);
                task.priority=0;
                Toast.makeText(getApplicationContext(), "Priority: LOW", Toast.LENGTH_SHORT).show();
            }
        });

        iv_priorityMed_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                iv_priorityMed_task_detail.setImageResource(R.drawable.medium_priority_dark);
                et_taskText_task_detail.setBackgroundResource(R.drawable.priority_medium_backround_rv);
                task.priority=1;
                Toast.makeText(getApplicationContext(), "Priority: MEDIUM", Toast.LENGTH_SHORT).show();
            }
        });

        iv_priorityHigh_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                iv_priorityHigh_task_detail.setImageResource(R.drawable.high_priority_dark);
                et_taskText_task_detail.setBackgroundResource(R.drawable.priority_high_background);
                task.priority=2;
                Toast.makeText(getApplicationContext(), "Priority: HIGH", Toast.LENGTH_SHORT).show();
            }
        });

        iv_editDueDate_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDueDateDialog();
            }
        });

        switch_doneOnOff_task_detail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (task.isFinished){
                    task.isFinished=false;
                    et_taskText_task_detail.setPaintFlags(0);
                }
                else{
                    task.isFinished=true;
                    et_taskText_task_detail.setPaintFlags(et_taskText_task_detail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }
            }
        });


    }


    private void initializeWidgets(){
        et_taskText_task_detail=findViewById(R.id.et_taskText_task_detail);
        switch_doneOnOff_task_detail=findViewById(R.id.switch_doneOnOff_task_detail);

        iv_priorityLow_task_detail=findViewById(R.id.iv_priorityLow_task_detail);
        iv_priorityMed_task_detail=findViewById(R.id.iv_priorityMed_task_detail);
        iv_priorityHigh_task_detail=findViewById(R.id.iv_priorityHigh_task_detail);

        iv_back_task_detail=findViewById(R.id.iv_back_task_detail);
        iv_editDueDate_task_detail=findViewById(R.id.iv_editDueDate_task_detail);
        tv_dueDate_task_detail=findViewById(R.id.tv_dueDate_task_detail);
        tv_createdDate_task_detail=findViewById(R.id.tv_createdDate_task_detail);
        bt_updateTask_task_detail=findViewById(R.id.bt_updateTask_task_detail);
        bt_deleteTask_task_detail=findViewById(R.id.bt_deleteTask_task_detail);
    }

    private void initializeVariables()
    {
        isDueDateSet=false;
        dueDate="";
        calendar = Calendar.getInstance();
        currYear=calendar.get((calendar.YEAR));
        currMonth=calendar.get((calendar.MONTH));
        currDay=calendar.get((calendar.DAY_OF_MONTH));
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
                et_taskText_task_detail.setBackgroundResource(R.drawable.priority_low_backround_rv);
                break;
            case 1:
                iv_priorityMed_task_detail.setImageResource(R.drawable.medium_priority_dark);
                et_taskText_task_detail.setBackgroundResource(R.drawable.priority_medium_backround_rv);
                break;
            case 2:
                iv_priorityHigh_task_detail.setImageResource(R.drawable.high_priority_dark);
                et_taskText_task_detail.setBackgroundResource(R.drawable.priority_high_background);
                break;
        }


        if (task.isFinished==true)
        {
            et_taskText_task_detail.setPaintFlags(et_taskText_task_detail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            switch_doneOnOff_task_detail.setOnCheckedChangeListener (null);
            switch_doneOnOff_task_detail.setChecked(true);
        }
    }


    public void alertDialogOnBackPressed(){
        AlertDialog dialog = new AlertDialog.Builder(TaskDetailActivity.this)
                .setTitle("Go back")
                .setMessage("Are you sure you want to go back?\nAll unsaved changes will be lost")
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setDefaultPriorityIcons()
    {
        iv_priorityLow_task_detail.setImageResource(R.drawable.low_priority_green);
        iv_priorityMed_task_detail.setImageResource(R.drawable.med_priority_yellow);
        iv_priorityHigh_task_detail.setImageResource(R.drawable.high_priority_red);
    }

    private void setDueDateDialog() {
        if (isDueDateSet)
        {
            Toast.makeText(getApplicationContext(), "Due date already set", Toast.LENGTH_SHORT).show();
        }
        else{
            DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    dueDate= DateFormat.getDateInstance().format(calendar.getTime());

                   // Toast.makeText(getApplicationContext(),"Due date: "+  dueDate.toString(), Toast.LENGTH_SHORT).show();
                    if (!dueDate.isEmpty())
                    {
                        //if dueDate is picked make this changes
                        isDueDateSet=true;
                        iv_editDueDate_task_detail.setImageResource(R.drawable.schedule_dark);
                        tv_dueDate_task_detail.setText(dueDate);
                    }

                }
            }, currYear, currMonth, currDay);
            datePickerDialog.show();
        }



    }

    @Override
    public void onBackPressed() {
        alertDialogOnBackPressed();
    }
}