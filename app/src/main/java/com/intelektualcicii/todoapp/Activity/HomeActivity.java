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
import com.google.android.material.tabs.TabLayout;
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

    TabLayout tabLayout;
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
        tabLayout=findViewById(R.id.tabLayout_Home);
        tabLayout.addTab(tabLayout.newTab().setText("Active"));
        tabLayout.addTab(tabLayout.newTab().setText("Finished"));
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tab.getPosition();

                // Provjerite koji je tab selektiran
                if (selectedTabPosition == 0) {
                    // Izvršavanje koda za Tab 1
                    // Primjer: Prikazivanje sadržaja za Tab 1
                    setActiveInRecyclerView();

                } else if (selectedTabPosition == 1) {
                    // Izvršavanje koda za Tab 2
                    // Primjer: Prikazivanje sadržaja za Tab 2
                    setFinishedInRecyclerView();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        setActiveInRecyclerView();



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

    // Method gets all tasks from database, then it filters it by checking value of isFinished.
    // All objects that have value of isFinished==false are added to another list activeTasks
    // List activeTasks is sent to adapter, and active tasks are displayed to screen.
    private void setActiveInRecyclerView()
    {
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        List<Task> activeTasks = new ArrayList<>();


        for (Task task : tasks) {
            if (task.isFinished==false){
                activeTasks.add(task);
            }
        }

        if (activeTasks.size()>=0){
            taskAdapter = new TaskAdapter(activeTasks,HomeActivity.this::onItemClick);
            recyclerView.setAdapter(taskAdapter);
        }
    }


    // Method gets all tasks from database, then it filters it by checking value of isFinished.
    // All objects that have value of isFinished==true are added to another list finishedTasks
    // List finishedTasks is sent to adapter, and finished tasks are displayed to screen
    private void setFinishedInRecyclerView()
    {

        List<Task> tasks = taskDatabase.taskDAO().getAll();
        List<Task> finishedTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isFinished==true){
                finishedTasks.add(task);
            }
        }

        if (finishedTasks.size()>=0){
            // Task adapter is also initialized with reference to the method 'onItemClick'
            // in the 'HomeActivity' class. That is needed to have listener on items in RV.
            taskAdapter = new TaskAdapter(finishedTasks,HomeActivity.this::onItemClick);
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

    //function that communicates with bottom sheet dialog(add new task)
    //when isCalled becomes true it changes to Active tab and refreshes recyclerView
    @Override
    public void onDismissBottomSheetCalled(Boolean isCalled) {
        if (isCalled){
            
            setActiveInRecyclerView();
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
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
        //ovo je kriva logika prominit kasnije
        List<Task> tasks = taskDatabase.taskDAO().getAll();
        Intent intent = new Intent(HomeActivity.this, TaskDetailActivity.class);
        intent.putExtra("task", tasks.get(position));

        startActivity(intent);
    }
}