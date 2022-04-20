package com.myroutine.myroutine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.myroutine.myroutine.dynamicgrid.DynamicGridView;

import java.util.List;

public class ListOfSteps extends AppCompatActivity {
    private static final String TAG = ListOfSteps.class.getName();


    TextView title;
    GridView gridView;
    DatabaseHelper mydb;
    ImageView img;
    //DynamicGridView gridView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_steps);
        mydb = new DatabaseHelper(this);
        String name = getIntent().getExtras().getString("taskname");
        final int taskid = getIntent().getExtras().getInt("taskid");
        img = (ImageView)findViewById(R.id.add);
        title = (TextView)findViewById(R.id.taskn);
        title.setText(name);
        gridView = (GridView) findViewById(R.id.girdview);
        //gridView = (DynamicGridView) findViewById(R.id.girdview);

        List<Step> list = mydb.getAllSteps(taskid);

        //showMessage("data", list.toString());
        stepAdapter x = new stepAdapter(ListOfSteps.this, list);
        //StepGridAdp x = new StepGridAdp(ListOfSteps.this,list, getResources().getInteger(R.integer.column_count));
        gridView.setAdapter(x);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Intent x = new Intent(ListOfSteps.this, NewStep.class);

                Step selectedItem =(Step) parent.getItemAtPosition(position);
               // mydb.getAllSteps()
                //showMessage("data",selectedItem);
                String[] steparray = {selectedItem.getName(), selectedItem.getPic(),
                        selectedItem.getAudio(), selectedItem.getTimer()};
                x.putExtra("EditMode", true);
                x.putExtra("steparray", steparray);
                x.putExtra("item-position", selectedItem.getId());
                x.putExtra("taskid", selectedItem.getTaskID());
                startActivity(x);

            }
        });

        /*gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
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

        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
        {
            @Override
            public void onActionDrop()
            {
                gridView.stopEditMode();
            }
        });*/

    }

    //@Override
   /* public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }*/
    public void onAdd(View view){
        int taskid = getIntent().getExtras().getInt("taskid");
        String name = getIntent().getExtras().getString("taskname");
        Intent x = new Intent(this,NewStep.class);
        x.putExtra("taskidre", taskid);
        x.putExtra("tasknamere", name);
        startActivity(x);
    }
    //goes to the parent main view
    public void doneFunction(View view){
        if(gridView.getChildCount() == 0){
            showMessage("Warning","Please add a step...");
        }else{
            Intent x = new Intent(this,SuperUserPage.class);
            startActivity(x);
        }

    }

    public void showMessage(String title, String message){
        android.app.AlertDialog.Builder dia = new android.app.AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }


}
