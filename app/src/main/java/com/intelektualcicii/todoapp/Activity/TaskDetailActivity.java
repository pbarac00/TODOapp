package com.intelektualcicii.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.intelektualcicii.todoapp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {


    EditText et_taskText_task_detail;
    SwitchMaterial switch_doneOnOff_task_detail;
    ImageView iv_priorityLow_task_detail,iv_priorityMed_task_detail,
            iv_priorityHigh_task_detail,iv_editDueDate_task_detail;
    TextView tv_dueDate_task_detail,tv_createdDate_task_detail;
    Button bt_updateTask_task_detail, bt_deleteTask_task_detail;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

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


        et_taskText_task_detail.setText(getIntent().getParcelableExtra("taskName"));






    }
}