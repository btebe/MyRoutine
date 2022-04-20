package com.myroutine.myroutine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myroutine.myroutine.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basmatebe on 4/28/17.
 */

public class GameAdapter extends BaseAdapter {
    private static final String TAG = Game.class.getName();


    private Context mContext;
    private LayoutInflater mInflater;
    private List<Object> tDataSource;
    private static final int TASK_ITEM = 0;
    private static final int HEADER = 1;


    private List<Step> sDataSource;
    private List<Step> sData;



    public GameAdapter(Context context, List<Object> taskitems, List<Step> stepitems) {
        mContext = context;
        tDataSource = taskitems;
        sDataSource = stepitems;
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
                    view = mInflater.inflate(R.layout.gamelay, null);
                    break;
                case HEADER:
                    view = mInflater.inflate(R.layout.header, null);
                    break;

            }
        }
        //determin which type of view to populate if view is not null
        switch (getItemViewType(position)) {
            case TASK_ITEM:

                sData = new ArrayList<>();
                int taskid = ((Task) tDataSource.get(position)).getId();
                for (int i = 0; i < sDataSource.size(); i++) {
                    if (sDataSource.get(i).getTaskID() == taskid) {
                        sData.add(sDataSource.get(i));
                    }
                }
                final DynamicGridView gridView = (DynamicGridView) view.findViewById(R.id.girdview3);
                //StepGridAdp st = new StepGridAdp(mContext, sData, mContext.getResources().getInteger(R.integer.column_count));
                //gridView.setAdapter(st);
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
                        //Step s = mydb.getStep(gridView.getId());
                    }
                });



                break;
            case HEADER:
                TextView header = (TextView) view.findViewById(R.id.headertitle);
                header.setText((String) tDataSource.get(position));
                break;

        }

        return view;

    }


}
