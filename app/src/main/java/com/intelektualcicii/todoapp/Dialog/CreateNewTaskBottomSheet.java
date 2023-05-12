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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.intelektualcicii.todoapp.R;

public class CreateNewTaskBottomSheet extends BottomSheetDialogFragment {
    EditText newTODO;
    ImageView lowPriority,medPriority,highPriority;
    Button saveTODO;

    private Integer taskPriority;

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


        lowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                lowPriority.setImageResource(R.drawable.low_priority_dark);
                taskPriority=1;
            }
        });

        medPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                medPriority.setImageResource(R.drawable.medium_priority_dark);
                taskPriority=2;
            }
        });


        highPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPriorityIcons();
                highPriority.setImageResource(R.drawable.high_priority_dark);
                taskPriority=3;
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
        String taskName;
        Integer priority;
        taskName=newTODO.getText().toString().trim();
    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Toast.makeText(getContext(), "ToDO saved lol", Toast.LENGTH_SHORT).show();
        //TODO implemetiraj da se savea TO-DO ako nije prazan
    }
}
