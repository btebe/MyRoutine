package com.myroutine.myroutine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by basmatebe on 4/27/17.
 */

public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Object> tDataSource;
    private static final int TASK_ITEM = 0;
    private static final int HEADER = 1;


    private List<Step> sDataSource;
    private List<Step> sData;



    public UserAdapter(Context context, List<Object> taskitems, List<Step> stepitems) {
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
                    view = mInflater.inflate(R.layout.user_canvas, null);
                    break;
                case HEADER:
                    view = mInflater.inflate(R.layout.header, null);
                    break;

            }
        }
        //determin which type of view to populate if view is not null
        switch (getItemViewType(position)) {
            case TASK_ITEM:
                TextView canname = (TextView) view.findViewById(R.id.usname);
                TextView cantime = (TextView) view.findViewById(R.id.ustime);
                ImageView pic1 = (ImageView) view.findViewById(R.id.img1);
                ImageView pic2 = (ImageView) view.findViewById(R.id.img2);
                ImageView pic3 = (ImageView) view.findViewById(R.id.img3);
                ImageView pic4 = (ImageView) view.findViewById(R.id.img4);
                ImageView pic5 = (ImageView) view.findViewById(R.id.img5);
                ImageView pic6 = (ImageView) view.findViewById(R.id.img6);


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



}
