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




        return view;

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
        //implemetiraj da se savea TO-DO ako nije prazan
    }
}
