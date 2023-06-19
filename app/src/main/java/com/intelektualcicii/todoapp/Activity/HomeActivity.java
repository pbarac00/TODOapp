package com.intelektualcicii.todoapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.intelektualcicii.todoapp.SelectItemListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements
        CreateNewTaskBottomSheet.CreateNewTaskBottomSheetListener,
        NavigationView.OnNavigationItemSelectedListener,
        SelectItemListener {

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

        addTask=findViewById(R.id.add_task_floating_bt);

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



        navMenu=findViewById(R.id.iv_navMenu);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();




        setDataInRecyclerView();
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Toast.makeText(HomeActivity.this, "clicked", Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
               // Toast.makeText(HomeActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
               // Toast.makeText(HomeActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });


        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
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
//
//        sortPriority.setOnClickListener( v -> {
//            if (isSortByPriorityClicked)
//            {
//                isSortByPriorityClicked =false;
//                sortPriority.setImageResource(R.drawable.priority);
//                setDataInRecyclerView();
//            }
//            else{
//                isSortByPriorityClicked =true;
//                sortPriority.setImageResource(R.drawable.priority_blue);
//                sortDataInRvByPriority();
//            }
//        });
//        sortDate.setOnClickListener(v -> {
//            if (isSortByDateClicked)
//            {
//                isSortByDateClicked=false;
//                sortDate.setImageResource(R.drawable.deadline);
//                setDataInRecyclerView();
//            }
//            else{
//                isSortByDateClicked=true;
//                sortDate.setImageResource(R.drawable.deadline_blue);
//                sortDataInRvByDate();
//            }
//        });
//        sortAZ.setOnClickListener(v -> {
//            if (isSortByAzClicked)
//            {
//                isSortByAzClicked=false;
//                sortAZ.setImageResource(R.drawable.sort_az);
//                setDataInRecyclerView();
//            }
//            else{
//                isSortByAzClicked=true;
//                sortAZ.setImageResource(R.drawable.sort_az_blue);
//                sortDataInRvByName();
//            }
//        });


        
    }

    private void setDataInRecyclerView()
    {
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        List<Task> notFinishedTasks = new ArrayList<>();
        //izbacit taskove kojima je isFinished YES
        //unfinishedTasks


        for (Task task : tasks) {
            if (task.isFinished==false){
                notFinishedTasks.add(task);
            }
            }

        if (notFinishedTasks.size()>0){
            taskAdapter = new TaskAdapter(notFinishedTasks,HomeActivity.this::onItemClick);
            recyclerView.setAdapter(taskAdapter);
        }

    }

//    private void sortDataInRvByDate()
//    {
//        List<Task> tasks = taskDatabase.taskDAO().getAll();
//        tasks.sort((o1, o2)
//                -> o2.getDueDate().compareTo(
//                o1.getDueDate()));
//        taskAdapter = new TaskAdapter(tasks);
//        recyclerView.setAdapter(taskAdapter);
//
//    }
//
//    private void sortDataInRvByPriority()
//    {
//        List<Task> tasks = taskDatabase.taskDAO().getAll();
//        tasks.sort((o1, o2)
//                -> o2.priority.compareTo(
//                o1.priority));
//        taskAdapter = new TaskAdapter(tasks);
//        recyclerView.setAdapter(taskAdapter);
//    }
//
//    private void sortDataInRvByName()
//    {
//        //ako je sort by priority upaljen, prvo sortira po prioritetu i onda ove sta imaju isti prioritet sortira po imenu
//        if (isSortByPriorityClicked){
//            //sortitaj po prioritetu
//            //oni koji imaju isti prioritet sortitaj po imenu
//            List<Task> tasks = taskDatabase.taskDAO().getAll();
//            tasks.sort((o1, o2)
//                    -> o1.taskName.toLowerCase(Locale.ROOT).compareTo(
//                    o2.taskName.toLowerCase(Locale.ROOT)));
//            taskAdapter = new TaskAdapter(tasks);
//        }
//        else{
//            //ako nije upaljen samo ovo
//            List<Task> tasks = taskDatabase.taskDAO().getAll();
//            tasks.sort((o1, o2)
//                    -> o1.taskName.toLowerCase(Locale.ROOT).compareTo(
//                    o2.taskName.toLowerCase(Locale.ROOT)));
//            taskAdapter = new TaskAdapter(tasks);
//        }
//
//        recyclerView.setAdapter(taskAdapter);
//    }

    private void logOut(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();



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

            case R.id.delete_finished:
                Toast.makeText(this, "need to implement delete finished", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_all_task:
                Toast.makeText(this, "need to implement delete all", Toast.LENGTH_SHORT).show();
                break;

            case R.id.log_out:
                logOut();
                break;

            case R.id.delete_account:
                Toast.makeText(this, "need to implement delete account", Toast.LENGTH_SHORT).show();
                break;

        }
        //zatvara menu logOut
        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        Toast.makeText(this, tasks.get(position).taskName, Toast.LENGTH_SHORT).show();
    }
}