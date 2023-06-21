package com.intelektualcicii.todoapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.intelektualcicii.todoapp.Adapter.TaskAdapter;
import com.intelektualcicii.todoapp.Model.Task;
import com.intelektualcicii.todoapp.Dao.TaskDAO;
import com.intelektualcicii.todoapp.Database.TaskDatabase;
import com.intelektualcicii.todoapp.Dialog.CreateNewTaskBottomSheet;
import com.intelektualcicii.todoapp.R;
import com.intelektualcicii.todoapp.Interface.SelectItemListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements
        CreateNewTaskBottomSheet.CreateNewTaskBottomSheetListener,
        NavigationView.OnNavigationItemSelectedListener,
        SelectItemListener {

    public int selectedTabPosition;
    public int currentllySelectedInBottomMenu;
    TabLayout tabLayout;
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    TaskDatabase taskDatabase;
    ImageView navMenu;
    Boolean isSortByPriorityClicked, isSortByAzClicked,isSortByDateClicked,isAddTodoOpen;
    BottomNavigationView bottom_navigation;
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
        bottom_navigation=findViewById(R.id.bottom_navigation);
        currentllySelectedInBottomMenu=0;

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
                selectedTabPosition = tab.getPosition();

                if (selectedTabPosition == 0) {
                    setActiveInRecyclerView();
                    doSortIfIsSelected();

                } else if (selectedTabPosition == 1) {
                    setFinishedInRecyclerView();
                    doSortIfIsSelected();
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


        bottom_navigation.setSelectedItemId(R.id.placeholder);

        bottom_navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sortByName:
                        sortDataInRvByName();
                        currentllySelectedInBottomMenu = R.id.sortByName;
                        return true;
                    case R.id.sortByPriority:
                        sortDataInRvByPriority();
                        currentllySelectedInBottomMenu= R.id.sortByPriority;
                        return true;
                    case R.id.sortByDueDate:
                        sortDataInRvByDueDate();
                        currentllySelectedInBottomMenu=R.id.sortByDueDate;
                        return true;
                    case R.id.sortByCreatedDate:
                        sortDataInRvByCreatedDate();
                        currentllySelectedInBottomMenu= R.id.sortByCreatedDate;
                        return true;
                }
                return false;
            }
        });

    }


    private void setActiveInRecyclerView()
    {
            taskAdapter = new TaskAdapter(getActiveTask(),HomeActivity.this::onItemClick);
            recyclerView.setAdapter(taskAdapter);
    }

    List<Task> getActiveTask(){

        List<Task> tasks = taskDatabase.taskDAO().getAll();
        List<Task> activeTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isFinished==false){
                activeTasks.add(task);
            }
        }
        return activeTasks;
    }

    List<Task> getFinishedTask() {

        List<Task> tasks = taskDatabase.taskDAO().getAll();
        List<Task> finishedTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isFinished==true){
                finishedTasks.add(task);
            }
        }
        return finishedTasks;
    }



    private void setFinishedInRecyclerView()
    {
            // Task adapter is also initialized with reference to the method 'onItemClick'
            // in the 'HomeActivity' class. That is needed to have listener on items in RV.
            taskAdapter = new TaskAdapter(getFinishedTask(),HomeActivity.this::onItemClick);
            recyclerView.setAdapter(taskAdapter);


    }

    private void sortDataInRvByDueDate()
    {
        List<Task> tasks= new ArrayList<>();
        if (selectedTabPosition==0)
        {
            tasks=getActiveTask();
        }
        else{
            tasks=getFinishedTask();
        }

        Comparator<Task> comparator = (o1, o2) -> {
            String dueDate1 = o1.getDueDate();
            String dueDate2 = o2.getDueDate();

            // Check if either dueDate is empty
            if (dueDate1.isEmpty() && !dueDate2.isEmpty()) {
                return 1; // Move o1 to the end of the list
            } else if (!dueDate1.isEmpty() && dueDate2.isEmpty()) {
                return -1; // Move o2 to the end of the list
            }

            // Compare dueDates for non-empty values
            return dueDate1.compareTo(dueDate2);
        };

        tasks.sort(comparator);
        taskAdapter = new TaskAdapter(tasks, HomeActivity.this::onItemClick);
        recyclerView.setAdapter(taskAdapter);
    }

    private void doSortIfIsSelected(){
        switch (currentllySelectedInBottomMenu) {
            case R.id.sortByName:
                sortDataInRvByName();
                break;
            case R.id.sortByPriority:
                sortDataInRvByPriority();
                break;
            case R.id.sortByDueDate:
                sortDataInRvByDueDate();
                break;
            case R.id.sortByCreatedDate:
                sortDataInRvByCreatedDate();
                break;
        }
    }

    private void sortDataInRvByCreatedDate()
    {
        List<Task> tasks= new ArrayList<>();
        if (selectedTabPosition==0)
        {
            tasks=getActiveTask();
        }
        else{
            tasks=getFinishedTask();
        }

        tasks.sort((o1, o2) -> o1.getStartedDate().compareTo(o2.getStartedDate()));
        taskAdapter = new TaskAdapter(tasks, HomeActivity.this::onItemClick);
        recyclerView.setAdapter(taskAdapter);

    }

private void sortDataInRvByPriority()
{
    List<Task> tasks= new ArrayList<>();
    if (selectedTabPosition==0)
    {
        tasks=getActiveTask();
    }
    else{
        tasks=getFinishedTask();
    }

    tasks.sort((o1, o2) -> o2.priority.compareTo(o1.priority));
    taskAdapter = new TaskAdapter(tasks,HomeActivity.this::onItemClick);
    recyclerView.setAdapter(taskAdapter);
}

    private void sortDataInRvByName()
    {

        List<Task> tasks= new ArrayList<>();
        if (selectedTabPosition==0)
        {
            tasks=getActiveTask();
        }
        else{
            tasks=getFinishedTask();
        }

            tasks.sort(Comparator.comparing(o -> o.taskName.toLowerCase(Locale.ROOT)));
            taskAdapter = new TaskAdapter(tasks,HomeActivity.this::onItemClick);

        recyclerView.setAdapter(taskAdapter);
    }

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
                deleteAllFinishedFromDb(taskDatabase);
                break;

            case R.id.delete_all_task:
                deleteAllFromDb();
                break;

            case R.id.log_out:
                logOut();
                break;

        }
        //zatvara menu logOut
        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }


    private void deleteAllFromDb(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to delete all TO-DO?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Task> tasks = taskDatabase.taskDAO().getAll();

                for (Task task : tasks)
                    {
                        taskDatabase.taskDAO().delete(task);
                    }

                Toast.makeText(HomeActivity.this, "All finished deleted", Toast.LENGTH_SHORT).show();
                setFinishedInRecyclerView();
                setActiveInRecyclerView();
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

    private void deleteAllFinishedFromDb(TaskDatabase taskDatabase) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to delete all finished?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Task> tasks = taskDatabase.taskDAO().getAll();

                for (Task task : tasks) {
                    if (task.isFinished==true){
                        taskDatabase.taskDAO().delete(task);
                    }
                }
                Toast.makeText(HomeActivity.this, "All finished deleted", Toast.LENGTH_SHORT).show();
                setFinishedInRecyclerView();
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
    public void onItemClick(int position) {
        List<Task> tasks= new ArrayList<>();
        if (selectedTabPosition==0)
        {
            tasks=getActiveTask();
        }
        else if (selectedTabPosition==1)
        {
            tasks=getFinishedTask();
        }

        Intent intent = new Intent(HomeActivity.this, TaskDetailActivity.class);
        intent.putExtra("task", tasks.get(position));
        startActivity(intent);
    }
}