package com.intelektualcicii.todoapp.Dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;


import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateNewTaskBottomSheet extends BottomSheetDialogFragment {

    EditText newTODO;
    ImageView lowPriority,medPriority,highPriority,setDueDate;
    Button saveTODO;
    private String taskText;
    private Integer taskPriority;
    private String dueDate;
    TaskDatabase db;
    Calendar calendar;
    int currYear,currMonth,currDay;
    private CreateNewTaskBottomSheetListener mListener;
    private ExecutorService executorsService= Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    Boolean isDueDateSet;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bottom_sheet_create_new_task,container,false);
        newTODO=view.findViewById(R.id.et_newTODO);
        lowPriority=view.findViewById(R.id.iv_lowPriority);
        medPriority=view.findViewById(R.id.iv_medPriority);
        highPriority=view.findViewById(R.id.iv_highPriority);
        saveTODO=view.findViewById(R.id.bt_saveNewTODO);
        setDueDate=view.findViewById(R.id.iv_setDuedate);
        taskPriority=0;
        isDueDateSet=false;
        dueDate="";
        calendar = Calendar.getInstance();
        currYear=calendar.get((calendar.YEAR));
        currMonth=calendar.get((calendar.MONTH));
        currDay=calendar.get((calendar.DAY_OF_MONTH));
        db= Room.databaseBuilder(getContext(), TaskDatabase.class, "task").
                fallbackToDestructiveMigration().build();



        lowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                lowPriority.setImageResource(R.drawable.low_priority_dark);
                taskPriority=0;
                Toast.makeText(getContext(), "Priority: LOW", Toast.LENGTH_SHORT).show();
            }
        });

        medPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                medPriority.setImageResource(R.drawable.medium_priority_dark);
                taskPriority=1;
                Toast.makeText(getContext(), "Priority: MEDIUM", Toast.LENGTH_SHORT).show();
            }
        });

        highPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                highPriority.setImageResource(R.drawable.high_priority_dark);
                taskPriority=2;
                Toast.makeText(getContext(), "Priority: HIGH", Toast.LENGTH_SHORT).show();
            }
        });

        saveTODO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTodo();
            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDueDateDialog();
            }
        });

        return view;

    }

    public interface CreateNewTaskBottomSheetListener{
        void onDismissBottomSheetCalled(Boolean isCalled);
    }

    private void setDueDateDialog() {
        if (isDueDateSet)
        {
            Toast.makeText(getContext(), "Due date already set", Toast.LENGTH_SHORT).show();
        }
        else{
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    dueDate=DateFormat.getDateInstance().format(calendar.getTime());

                    Toast.makeText(getContext(),"Due date: "+  dueDate.toString(), Toast.LENGTH_SHORT).show();
                    if (!dueDate.isEmpty())
                    {
                        isDueDateSet=true;
                        setDueDate.setImageResource(R.drawable.schedule_dark);
                    }

                }
            }, currYear, currMonth, currDay);
            datePickerDialog.show();
        }

    }

    private void setDefaultPriorityIcons(){
        lowPriority.setImageResource(R.drawable.low_priority_green);
        medPriority.setImageResource(R.drawable.med_priority_yellow);
        highPriority.setImageResource(R.drawable.high_priority_red);
    }


    private void createNewTodo(){
        taskText=newTODO.getText().toString().trim();
        if (taskText.isEmpty()){
            Toast.makeText(getContext(), "Nothing to save, please edit TO-DO", Toast.LENGTH_SHORT).show();
        }
        else
        {
            executorsService.execute(new Runnable() {
                @Override
                public void run() {

                    String createdDate= DateFormat.getDateInstance().format(calendar.getTime());
                    String uniqueID=UUID.randomUUID().toString();


                    Task task=new Task(taskText,taskPriority,false,uniqueID, createdDate);
                    task.setDueDate(dueDate);
                    db.taskDAO().insertAll(task);


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "TO-DO added", Toast.LENGTH_SHORT).show();
                            mListener.onDismissBottomSheetCalled(true);
                            dismiss();
                        }
                    });
                }
            });


        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener=(CreateNewTaskBottomSheetListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement bottom sheet listener");
        }
    }


    }

