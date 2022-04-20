package com.myroutine.myroutine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;


/**
 * Created by basmatebe on 4/21/17.
 */

public class TaskAdapter extends BaseAdapter {
    Activity actclass;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Object> tDataSource;
    private static final int TASK_ITEM = 0;
    private static final int HEADER = 1;
    ListView lv;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    List<Step> sDataSource;
    List<Step> sData;
    List<String> slist;


    public TaskAdapter(Context context, List<Object> taskitems, List<Step> stepitems, Activity x, ListView lv) {
        mContext = context;
        tDataSource = taskitems;
        sDataSource = stepitems;
        this.lv = lv;

        actclass = x;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if (tDataSource.get(position) instanceof Task) {
            return TASK_ITEM;
        } else {
            return HEADER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return tDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return tDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        //determine which view to inflate if view is null
        if (view == null) {
            switch (getItemViewType(position)) {
                case TASK_ITEM:
                    view = mInflater.inflate(R.layout.task_canvas, null);
                    break;
                case HEADER:
                    view = mInflater.inflate(R.layout.header, null);
                    break;

            }
        }
        //determin which type of view to populate if view is not null
        switch (getItemViewType(position)) {
            case TASK_ITEM:
                TextView canname = (TextView) view.findViewById(R.id.canname);
                TextView cantime = (TextView) view.findViewById(R.id.cantime);
                ImageView pic1 = (ImageView) view.findViewById(R.id.pic1);
                ImageView pic2 = (ImageView) view.findViewById(R.id.pic2);
                ImageView pic3 = (ImageView) view.findViewById(R.id.pic3);
                ImageView pic4 = (ImageView) view.findViewById(R.id.pic4);
                ImageView pic5 = (ImageView) view.findViewById(R.id.pic5);
                ImageView pic6 = (ImageView) view.findViewById(R.id.pic6);
                final ImageButton iv= (ImageButton) view.findViewById(R.id.slidebtn);
                //final LinearLayout blue = (LinearLayout) view.findViewById(R.id.ai);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSlide(v, position);
                    }
                });


                //Task task = (Task) getItem(position);
                canname.setText(((Task) tDataSource.get(position)).getName());
                cantime.setText(((Task) tDataSource.get(position)).getTime());
                sData = new ArrayList<>();
                int taskid = ((Task) tDataSource.get(position)).getId();
                for (int i = 0; i < sDataSource.size(); i++) {
                    if (sDataSource.get(i).getTaskID() == taskid) {
                        sData.add(sDataSource.get(i));
                    }
                }
                ImageView[] imgv = {pic1, pic2, pic3, pic4, pic5, pic6};
                for (int i = 0; i < sData.size(); i++) {
                    if (sData.get(i).getPic() != null) {
                        if (!(i > imgv.length - 1)) {
                            Bitmap x = BitmapFactory.decodeFile(sData.get(i).getPic());
                            imgv[i].setImageBitmap(x);
                        }
                    }
                }

                break;
            case HEADER:
                TextView header = (TextView) view.findViewById(R.id.headertitle);
                header.setText((String) tDataSource.get(position));
                break;

        }

        return view;

    }

    public void onSlide(View v, final int position) {
        //button's parent which is a linearlayout
        LinearLayout parentRow = (LinearLayout) v.getParent();
        //final int position = listView.getPositionForView(parentRow);
        LinearLayout lw = (LinearLayout) parentRow.getChildAt(0);
        if (lw.getVisibility() == View.GONE) {
            lw.setVisibility(View.VISIBLE);
        } else {
            lw.setVisibility(View.GONE);
        }
        //open edit task page
        final Button b1 = (Button) lw.getChildAt(0);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task x = (Task) getItem(position);
                Intent intent = new Intent(mContext, NewTask.class);
                intent.putExtra("taskid", x.getId());
                //to transfer to an activiity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                //Toast.makeText(mContext, b1.getText()+" "+x.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        //opens the steps page fo the task selected
        final Button b2 = (Button) lw.getChildAt(1);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task x = (Task) getItem(position);
                Intent intent = new Intent(mContext, ListOfSteps.class);
                intent.putExtra("taskid", x.getId());
                intent.putExtra("taskname", x.getName());
                //to transfer to an activiity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        final Button b3 = (Button) lw.getChildAt(2);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task x = (Task) getItem(position);
                showMessage("Confirmation", "Are you sure you?", x.getId(), position);
            }
        });
    }

    public void showMessage(String title, String msg, final int position, final int rowid) {
        final DatabaseHelper mydb = new DatabaseHelper(mContext);
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(actclass);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                List<Step> st = mydb.getAllSteps(position);
                for (int n = 0; n < st.size(); n++) {
                    Step step = st.get(n);
                    if (step.getAudio() != null) {
                        File deleteaudio = new File(step.getAudio());
                        deleteaudio.delete();
                    }
                    if (step.getPic() != null) {
                        File deletepic = new File(step.getPic());
                        deletepic.delete();
                    }
                }
                // Toast.makeText(NewStep.this, step.toString(), Toast.LENGTH_LONG).show();
                mydb.deleteAllStep(position);
                mydb.deleteTask(position);
                Intent in = actclass.getIntent();
                actclass.overridePendingTransition(0, 0);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                actclass.finish();
                actclass.overridePendingTransition(0, 0);
                actclass.startActivity(in);


            }
        });
        alertDialog.setNegativeButton("Cancel", null);
        //cancels
        alertDialog.setCancelable(true);
        alertDialog.show();
    }


}




