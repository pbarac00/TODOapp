package com.intelektualcicii.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.intelektualcicii.todoapp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TaskDetailActivity extends AppCompatActivity {

    ImageView editTask;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        editTask=findViewById(R.id.iv_editTask);

        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskDetailActivity.this,EditTaskActivity.class));
            }
        });

    }
}