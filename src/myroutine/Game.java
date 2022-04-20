package com.myroutine.myroutine;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.myroutine.myroutine.dynamicgrid.DynamicGridView;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity {


    private static final String TAG = ListOfSteps.class.getName();
    DynamicGridView gridView;
    DatabaseHelper mydb;
    //List<Step> list;
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    boolean shaked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mydb = new DatabaseHelper(this);
        gridView = (DynamicGridView) findViewById(R.id.girdview2);
        final List<Task> task = mydb.getAllTasks();
        Random r = new Random();
        final int rand = r.nextInt(task.size());
        int sn = rand;
        final List<Step> list = mydb.getAllSteps(task.get(rand).getId());
        //SuperUserPage superd = new SuperUserPage();
        //List<Object> grps = superd.taskcategories(mydb.getAllTasks());
        final List<Step> original= mydb.getAllSteps(task.get(sn).getId());
        //initializeShake
        final StepGridAdp x = new StepGridAdp(Game.this, list, getResources().getInteger(R.integer.column_count), original, this);
        gridView.setAdapter(x);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                shaked = true;
                //Collections.shuffle(list);
                Collections.shuffle(list);
                //List<Step> s = list;
                x.set(list);
                //showMessage("data", list.get(0).getId()+" second: "+ original.get(0).getId());
                //Toast.makeText(Game.this, "shaked", Toast.LENGTH_SHORT).show();

            }
        });

        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gridView.stopEditMode();
                //List<Step>  sss= x.getList();
                int counter =0;
                List <Object> modified = x.getItems();
                for(int i=0; i<original.size();i++){
                    if(modified.get(i) instanceof Step){
                        if(original.get(i).getId() == ((Step) modified.get(i)).getId()){
                            counter++;

                        }else{
                            Toast.makeText(Game.this,"keep going (^_^)", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                }
                if(counter == original.size()){
                    Toast.makeText(Game.this,"Well done!!!", Toast.LENGTH_SHORT).show();
                }
                //Step s = mydb.getStep(gridView.getId());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }



    public void backFunction(View view){
        if(getIntent().hasExtra("user")){
            Intent x = new Intent(this, UserPage.class);
            startActivity(x);
        }else{
            Intent x = new Intent(this, SuperUserPage.class);
            startActivity(x);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void showMessage(String title, String message){
        android.app.AlertDialog.Builder dia = new android.app.AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }





}
