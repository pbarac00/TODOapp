package com.intelektualcicii.todoapp.Dialog;

import android.annotation.SuppressLint;
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
    private ExecutorService executorsService= Executors.newSingleThreadExecutor();
    EditText newTODO;
    ImageView lowPriority,medPriority,highPriority;
    Button saveTODO;
    private String taskText;
    private Integer taskPriority;
    TaskDatabase db;
    private CreateNewTaskBottomSheetListener mListener;
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());


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
        taskPriority=0;
        db= Room.databaseBuilder(getContext(), TaskDatabase.class, "task").
                fallbackToDestructiveMigration().build();



        lowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                lowPriority.setImageResource(R.drawable.low_priority_dark);
                taskPriority=0;
            }
        });

        medPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                medPriority.setImageResource(R.drawable.medium_priority_dark);
                taskPriority=1;
            }
        });

        highPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                highPriority.setImageResource(R.drawable.high_priority_dark);
                taskPriority=2;
            }
        });

        saveTODO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTodo();
            }
        });

        return view;

    }

    public interface CreateNewTaskBottomSheetListener{
        void onDismissBottomSheetCalled(Boolean isCalled);
    }

    private void setDefaultPriorityIcons(){
        lowPriority.setImageResource(R.drawable.low_priority);
        medPriority.setImageResource(R.drawable.med_priority);
        highPriority.setImageResource(R.drawable.high_priority);
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
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.YEAR,2022);
                    calendar.set(Calendar.MONTH,5);
                    calendar.set(Calendar.DAY_OF_MONTH,3);
                    String createdDate= DateFormat.getDateInstance().format(calendar.getTime());
                    String uniqueID=UUID.randomUUID().toString();
                    Task task=new Task(taskText,taskPriority,false,uniqueID, createdDate);
                    //TODO need to implement date picker this is hardcoded and optional
                    calendar.set(Calendar.YEAR,2022);
                    calendar.set(Calendar.MONTH,6);
                    calendar.set(Calendar.DAY_OF_MONTH,4);
                    String dueDate=DateFormat.getDateInstance().format(calendar.getTime());
                    task.setDueDate(dueDate);

                    db.taskDAO().insertAll(task);


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "task added", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //TODO implemetiraj da se savea TO-DO ako nije prazan
    }


    }

