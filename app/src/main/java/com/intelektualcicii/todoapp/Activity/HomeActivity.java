package com.intelektualcicii.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.intelektualcicii.todoapp.DataAdapter.TaskAdapter;
import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDAO;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.Dialog.CreateNewTaskBottomSheet;
import com.intelektualcicii.todoapp.R;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

    Button logOut;
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    TaskDatabase taskDatabase;
    ImageView sortPriority,sortAZ,sortDate;
    Boolean isSortByPrioriyClicked, isSortByAzClicked,isSortByDateClicked;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logOut=findViewById(R.id.bt_logOut);
        addTask=findViewById(R.id.add_task_floating_bt);
        sortPriority=findViewById(R.id.iv_priortity_home);
        sortAZ=findViewById(R.id.iv_sort_az_home);
        sortDate=findViewById(R.id.iv_sortByDate_home);

        recyclerView=findViewById(R.id.recyclerViewHomeTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        isSortByPrioriyClicked=false;
        isSortByAzClicked=false;
        isSortByDateClicked=false;

        taskDatabase= Room.databaseBuilder(getApplicationContext(),
                        TaskDatabase.class,"task").
                allowMainThreadQueries().fallbackToDestructiveMigration().build();

       TaskDAO taskDAO =taskDatabase.taskDAO();
        //ovo


        List<Task> tasks = taskDatabase.taskDAO().getAll();
        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(taskAdapter);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });
        
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewTaskBottomSheet createNewTaskBottomSheet= new CreateNewTaskBottomSheet();
                createNewTaskBottomSheet.show(getSupportFragmentManager(),"createNewTaskBottomSheet");
            }
        });

        sortPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSortByPrioriyClicked)
                {
                    isSortByPrioriyClicked=false;
                    sortPriority.setImageResource(R.drawable.priority);
                }
                else{
                    isSortByPrioriyClicked=true;
                    sortPriority.setImageResource(R.drawable.priority_blue);
                }
            }
        });


        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSortByDateClicked)
                {
                    isSortByDateClicked=false;
                    sortDate.setImageResource(R.drawable.deadline);
                }
                else{
                    isSortByDateClicked=true;
                    sortDate.setImageResource(R.drawable.deadline_blue);
                }
            }
        });

        sortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSortByAzClicked)
                {
                    isSortByAzClicked=false;
                    sortAZ.setImageResource(R.drawable.sort_az);
                }
                else{
                    isSortByAzClicked=true;
                    sortAZ.setImageResource(R.drawable.sort_az_blue);
                }
            }
        });

        
    }
    
    

}