package com.myroutine.myroutine;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StartingPage extends AppCompatActivity {
    DatabaseHelper mydb;
    EditText etpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
        mydb = new DatabaseHelper(this);



    }

    public void onEnter(View view){
        Intent x = new Intent(this,UserPage.class);
        startActivity(x);


    }


    /*public void onLogin(View view){
        etpass = (EditText) findViewById(R.id.etpassword);
        String pass = etpass.getText().toString();

        boolean isInserted = mydb.insertDate(pass);
        if(isInserted){
            Toast.makeText(this, "inserted",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "not inserted",Toast.LENGTH_LONG).show();
        }

    }*/

    public void onSettings(View view){
        Cursor res = mydb.getAllData(DatabaseHelper.TABLE_NAME1);
        if(res.getCount() > 0){
            Intent x = new Intent(this,LoginPage.class);
            startActivity(x);
            //showMessage("full", "data found");


        }else{
            //showMessage("Error", "No data found");
            Intent x = new Intent(this,RegisterSuperUser.class);
            startActivity(x);

        }



    }
    /*public void onViewAll(View view){
        Cursor res = mydb.getAllData(DatabaseHelper.TABLE_NAME1);
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

        }
        //show all the data
        showMessage("Data", bf.toString());
    }*/

    public void showMessage(String title, String message){
        AlertDialog.Builder dia = new AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }

}
