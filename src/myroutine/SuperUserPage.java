package com.myroutine.myroutine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SuperUserPage extends AppCompatActivity {

    ListView lv;
    DatabaseHelper mydb;
    TaskAdapter tADP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view);

        mydb = new DatabaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(SuperUserPage.this, NewTask.class);
                startActivity(x);
            }
        });

        FloatingActionButton settings = (FloatingActionButton) findViewById(R.id.settingsbtn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(SuperUserPage.this, RegisterSuperUser.class);
                x.putExtra("EditUser", true);
                startActivity(x);
            }
        });
        lv = (ListView) findViewById(R.id.parentlv);
        //List<Task> list = mydb.getAllTasks();
        List<Task> list = mydb.getAllTasksOrderByTime();
        List<Object> categories = taskcategories(list);
        List<Step> step = mydb.getAllSteps();
        //showMessage("data", categories.toString()+"");
        if (!list.isEmpty()) {
            tADP = new TaskAdapter(getApplicationContext(), categories, step, SuperUserPage.this, lv);
            lv.setAdapter(tADP);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    Intent x = new Intent(SuperUserPage.this, SlideShow.class);
                    Task selectedItem = (Task) parent.getItemAtPosition(position);
                    x.putExtra("taskid", selectedItem.getId());
                    startActivity(x);


                }
            });

        } else {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }


    }
    public void onGame(View view){
        Intent x = new Intent(this, Game.class);

        startActivity(x);
    }


    public void showMessage(String title, String message) {
        android.app.AlertDialog.Builder dia = new android.app.AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }

    public void backFunction(View view) {
        Intent x = new Intent(this, StartingPage.class);
        startActivity(x);
    }


    public List<Object> taskcategories(List<Task> task) {
        String[] s = {"Sunday ", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
                "Saturday"};
        List<Object> sunday = new ArrayList<>();
        sunday.add(s[0]);
        List<Object> monday = new ArrayList<>();
        monday.add(s[1]);
        List<Object> tuesday = new ArrayList<>();
        tuesday.add(s[2]);
        List<Object> wednesday = new ArrayList<>();
        wednesday.add(s[3]);
        List<Object> thursday = new ArrayList<>();
        thursday.add(s[4]);
        List<Object> friday = new ArrayList<>();
        friday.add(s[5]);
        List<Object> saturday = new ArrayList<>();
        saturday.add(s[6]);
        List<Object> anyday = new ArrayList<>();
        anyday.add("Anyday");
        for (int i = 0; i < task.size(); i++) {
            if (task.get(i).getDay().equals(sunday.get(0))) {
                sunday.add(task.get(i));
            } else if (task.get(i).getDay().equals(monday.get(0))) {
                monday.add(task.get(i));
            } else if (task.get(i).getDay().equals(tuesday.get(0))) {
                tuesday.add(task.get(i));
            } else if (task.get(i).getDay().equals(wednesday.get(0))) {
                wednesday.add(task.get(i));
            } else if (task.get(i).getDay().equals(thursday.get(0))) {
                thursday.add(task.get(i));
            } else if (task.get(i).getDay().equals(friday.get(0))) {
                friday.add(task.get(i));
            } else if (task.get(i).getDay().equals(saturday.get(0))) {
                saturday.add(task.get(i));
            } else {
                anyday.add(task.get(i));
            }
        }
        List<Object> mainList = new ArrayList<>();
        if (anyday.size() != 1) mainList.addAll(anyday);
        if (sunday.size() != 1) mainList.addAll(sunday);
        if (monday.size() != 1) mainList.addAll(monday);
        if (tuesday.size() != 1) mainList.addAll(tuesday);
        if (wednesday.size() != 1) mainList.addAll(wednesday);
        if (thursday.size() != 1) mainList.addAll(thursday);
        if (friday.size() != 1) mainList.addAll(friday);
        if (saturday.size() != 1) mainList.addAll(saturday);
        return mainList;
    }


}
