package com.myroutine.myroutine;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterSuperUser extends AppCompatActivity {
    DatabaseHelper mydb;

    EditText pass, inemail;
    Button registerbtn;
    ImageView bcs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_super_user);
        mydb = new DatabaseHelper(this);
        pass = (EditText) findViewById(R.id.inputpass);
        inemail = (EditText) findViewById(R.id.inputemail);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        bcs = (ImageView) findViewById(R.id.bcs);
        if(getIntent().hasExtra("EditUser")){
            registerbtn.setText("Submit Edit");
            pass.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            SuperUser info = mydb.getUser();
            pass.setText(info.getPassword());
            inemail.setText(info.getEmail());

            bcs.setVisibility(View.VISIBLE);
            bcs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent x = new Intent(RegisterSuperUser.this,SuperUserPage.class);
                    startActivity(x);
                }
            });
        }
    }

    public void onRegister(View view){
        if(registerbtn.getText().equals("Register")){
            String password = pass.getText().toString();
            String email = inemail.getText().toString();
            boolean isInserted = mydb.addToSuperTable(new SuperUser(password,email));
            if(isInserted){
                Toast.makeText(this, "Registered",Toast.LENGTH_LONG).show();
                Intent x = new Intent(this,SuperUserPage.class);
                startActivity(x);
            }else{
                Toast.makeText(this, "not registered",Toast.LENGTH_LONG).show();
            }

        }else {
            SuperUser user = new SuperUser(pass.getText().toString(), inemail.getText().toString());
            mydb.updateSuperUser(user);
            Intent x = new Intent(RegisterSuperUser.this,SuperUserPage.class);
            startActivity(x);
        }


    }
}
