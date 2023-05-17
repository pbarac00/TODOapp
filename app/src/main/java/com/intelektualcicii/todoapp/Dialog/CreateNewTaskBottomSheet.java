package com.intelektualcicii.todoapp.Dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
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
import androidx.room.Room;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.R;

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
        db= Room.databaseBuilder(getContext(), TaskDatabase.class, "task").build();



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
                    String uniqueID=UUID.randomUUID().toString();
                    Task note=new Task(taskText,taskPriority,false,uniqueID);
                    db.taskDAO().insertAll(note);

                }
            });
            Toast.makeText(getContext(), "task added", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //TODO implemetiraj da se savea TO-DO ako nije prazan
    }
}
