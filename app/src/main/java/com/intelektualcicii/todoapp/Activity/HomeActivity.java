package com.intelektualcicii.todoapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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


public class HomeActivity extends AppCompatActivity implements
        CreateNewTaskBottomSheet.CreateNewTaskBottomSheetListener, NavigationView.OnNavigationItemSelectedListener  {

    Button logOut,testButton;
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    TaskDatabase taskDatabase;
    ImageView sortPriority,sortAZ,sortDate,navMenu;
    Boolean isSortByPriorityClicked, isSortByAzClicked,isSortByDateClicked,isAddTodoOpen;


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

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
        testButton=findViewById(R.id.testButton);
        taskDatabase= Room.databaseBuilder(getApplicationContext(),
                        TaskDatabase.class,"task").
                allowMainThreadQueries().fallbackToDestructiveMigration().build();
       TaskDAO taskDAO =taskDatabase.taskDAO();



        navMenu=findViewById(R.id.iv_navMenu);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();




        setDataInRecyclerView();


        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TaskDetailActivity.class));
            }
        });

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

        sortPriority.setOnClickListener( v -> {
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
        });
        sortDate.setOnClickListener(v -> {
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
        });
        sortAZ.setOnClickListener(v -> {
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

    private void logOut(){

        FirebaseAuth.getInstance().signOut();

        Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
        finish();

    }

    @Override
    public void onDismissBottomSheetCalled(Boolean isCalled) {
        if (isCalled){
            setDataInRecyclerView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.naav:
                Toast.makeText(this, "Log ou", Toast.LENGTH_SHORT).show();
                break;

            case R.id.log_out:
                logOut();
                break;

        }
        //zatvara menu logOut
        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }
}