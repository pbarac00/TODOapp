package com.intelektualcicii.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.intelektualcicii.todoapp.DataAdapter.TaskAdapter;
import com.intelektualcicii.todoapp.DataHolder.Task;
import com.intelektualcicii.todoapp.DataHolder.TaskDAO;
import com.intelektualcicii.todoapp.DataHolder.TaskDatabase;
import com.intelektualcicii.todoapp.Dialog.CreateNewTaskBottomSheet;
import com.intelektualcicii.todoapp.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements CreateNewTaskBottomSheet.CreateNewTaskBottomSheetListener {

    Button logOut;
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    TaskDatabase taskDatabase;
    ImageView sortPriority,sortAZ,sortDate;
    Boolean isSortByPriorityClicked, isSortByAzClicked,isSortByDateClicked,isAddTodoOpen;

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
        isSortByPriorityClicked =false;
        isSortByAzClicked=false;
        isSortByDateClicked=false;
        isAddTodoOpen=false;
        taskDatabase= Room.databaseBuilder(getApplicationContext(),
                        TaskDatabase.class,"task").
                allowMainThreadQueries().fallbackToDestructiveMigration().build();
       TaskDAO taskDAO =taskDatabase.taskDAO();


        setDataInRecyclerView();

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
                isAddTodoOpen=true;
            }
        });

        sortPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSortByPriorityClicked)
                {
                    isSortByPriorityClicked =false;
                    sortPriority.setImageResource(R.drawable.priority);
                    setDataInRecyclerView();
                }
                else{
                    isSortByPriorityClicked =true;
                    sortPriority.setImageResource(R.drawable.priority_blue);
                    sortDataInRvByPriority();
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
                    setDataInRecyclerView();
                }
                else{
                    isSortByDateClicked=true;
                    sortDate.setImageResource(R.drawable.deadline_blue);
                    sortDataInRvByDate();
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
                    setDataInRecyclerView();
                }
                else{
                    isSortByAzClicked=true;
                    sortAZ.setImageResource(R.drawable.sort_az_blue);
                    sortDataInRvByName();
                }
            }
        });


        
    }

    private void setDataInRecyclerView()
    {
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(taskAdapter);
    }

    private void sortDataInRvByDate()
    {
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        tasks.sort((o1, o2)
                -> o2.getDueDate().compareTo(
                o1.getDueDate()));
        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(taskAdapter);

    }

    private void sortDataInRvByPriority()
    {
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        tasks.sort((o1, o2)
                -> o2.priority.compareTo(
                o1.priority));
        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(taskAdapter);
    }

    private void sortDataInRvByName()
    {
        //ako je sort by priority upaljen, prvo sortira po prioritetu i onda ove sta imaju isti prioritet sortira po imenu
        if (isSortByPriorityClicked){
            //sortitaj po prioritetu
            //oni koji imaju isti prioritet sortitaj po imenu
            List<Task> tasks = taskDatabase.taskDAO().getAll();
            tasks.sort((o1, o2)
                    -> o1.taskName.toLowerCase(Locale.ROOT).compareTo(
                    o2.taskName.toLowerCase(Locale.ROOT)));
            taskAdapter = new TaskAdapter(tasks);
        }
        else{
            //ako nije upaljen samo ovo
            List<Task> tasks = taskDatabase.taskDAO().getAll();
            tasks.sort((o1, o2)
                    -> o1.taskName.toLowerCase(Locale.ROOT).compareTo(
                    o2.taskName.toLowerCase(Locale.ROOT)));
            taskAdapter = new TaskAdapter(tasks);
        }

        recyclerView.setAdapter(taskAdapter);
    }


    @Override
    public void onDismissBottomSheetCalled(Boolean isCalled) {
        if (isCalled){
            setDataInRecyclerView();
        }
    }
}