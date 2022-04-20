package com.myroutine.myroutine;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static android.icu.util.Calendar.getInstance;

public class NewTask extends AppCompatActivity {
    //private int mHour, mMinute;
    TextView dateval,timeval;
    EditText name;
    String format;
    Button nextbtn;
    DatabaseHelper mydb;
    String[] s = { "Anyday","Sunday ", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        mydb = new DatabaseHelper(this);
        this.deleteDatabase("MyRoutine.db");
        dateval = (TextView)findViewById(R.id.valday);
        timeval  = (TextView)findViewById(R.id.valtime);
        nextbtn = (Button) findViewById(R.id.nextbtn);
        name  = (EditText)findViewById(R.id.task_name);
        if(getIntent().hasExtra("returned")){
            String[] returned = getIntent().getExtras().getStringArray("returned");
            name.setText(returned[0]);
            dateval.setText(returned[1]);
            timeval.setText(returned[2]);

        }

        if(getIntent().hasExtra("taskid")){
            int id = getIntent().getExtras().getInt("taskid");
            Task task = mydb.getTask(id);
            name.setText(task.getName());
            dateval.setText(task.getDay());
            timeval.setText(task.getTime());
            nextbtn.setText("Done");
        }

        //nextbtn.setTextColor(ContextCompat.getColor(NewTask.this, R.color.lavendergrey));
        //nextbtn.setEnabled(false);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence chars, int i, int i1, int i2) {
                if(chars.length() != 0){
                    nextbtn.setTextColor(ContextCompat.getColor(NewTask.this, R.color.black));
                    nextbtn.setEnabled(true);
                }else{
                    nextbtn.setTextColor(ContextCompat.getColor(NewTask.this, R.color.lavendergrey));
                    nextbtn.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



    }
    public void backFunction(View view){

        Intent x = new Intent(this,SuperUserPage.class);
        startActivity(x);
    }
    public void nextFunction(View view){
        if(getIntent().hasExtra("taskid")){
            String date = dateval.getText().toString();
            String time = timeval.getText().toString();
            String taskname = name.getText().toString();
            Task task = new Task(taskname, date, time);
            task.setId(getIntent().getExtras().getInt("taskid"));
            mydb.updateTask(task);
            Intent x = new Intent(this,SuperUserPage.class);
            startActivity(x);
        }else{

            String date = dateval.getText().toString();
            String time = timeval.getText().toString();
            String taskname = name.getText().toString();
            String[] taskarray = {taskname, date, time};
            Intent x = new Intent(this,NewStep.class);
            //to pass object Task
            x.putExtra("taskinfo",taskarray);
            startActivity(x);

        }



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTime(View view) {
        final Calendar c = getInstance();
       int  mHour = c.get(Calendar.HOUR);
       int  mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,TimePickerDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay % 12;
                        if (hour == 0)
                            hour = 12;
                        //so the time can always be in english
                        timeval.setText(String.format(Locale.US,"%2d:%02d %s", hour, minute,
                                hourOfDay < 12 ? "AM" : "PM"));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void onDay(View view){
        //launch alet box for the days of the week
        AlertDialog.Builder builder3=new AlertDialog.Builder(this);
        builder3.setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dateval.setText(s[which].toString());

                    }
                }
        );
        //make the items in the dialog scrollable
        builder3.show().getListView().setVerticalScrollBarEnabled(true);


    }
     public void onViewAll(){
        Cursor res = mydb.getAllData(DatabaseHelper.TABLE_NAME2);
        if(res.getCount() == 0){
            //show message
            showMessage("Error", "No data found");
            return;
        }
        StringBuffer bf = new StringBuffer();
        //iterate thru data
        while(res.moveToNext()){
            //store result in buffer
            //res.getstring(col number)
            bf.append("id: "+ res.getString(0)+ "\n");
            bf.append("name: "+ res.getString(1)+ "\n");
            bf.append("day: "+ res.getString(2)+ "\n");
            bf.append("time: "+ res.getString(3)+ "\n");

        }
        //show all the data
        showMessage("Data", bf.toString());
    }

    public void showMessage(String title, String message){
        android.app.AlertDialog.Builder dia = new android.app.AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }

}
