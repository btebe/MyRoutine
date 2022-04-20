package com.myroutine.myroutine;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginPage extends AppCompatActivity {
    EditText pass;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mydb = new DatabaseHelper(this);
    }

    public void onLogin(View view){
        pass = (EditText)findViewById(R.id.loginpass);
        String password = pass.getText().toString();
        if(password.equals("")){
            showMessage("Error","Please Enter Password");
        }else{
            Cursor res = mydb.getSuperUser(password);
            if(res.getCount() > 0){
                Intent x = new Intent(this,SuperUserPage.class);
                startActivity(x);
            }else{
                showMessage("Error","Invalid");
            }

        }




    }
    public void backFunction(View view){
        Intent x = new Intent(this,StartingPage.class);
        startActivity(x);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder dia = new AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }
}
