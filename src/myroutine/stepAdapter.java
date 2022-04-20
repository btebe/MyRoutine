package com.myroutine.myroutine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.myroutine.myroutine.DbBitmapUtility.getImage;

/**
 * Created by basmatebe on 4/15/17.
 */

public class stepAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<Step> mDataSource;


    public stepAdapter (Context context, List<Step> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.step_item, parent, false);


        ImageView thumbnailImageView = (ImageView) rowView.findViewById(R.id.gridpic);
        TextView tv = (TextView) rowView.findViewById(R.id.stepn);
        Step step = (Step) getItem(position);
        if(step.getPic() != null){
            //String image = step.getPic();
            //Bitmap img = getImage(image);
            Bitmap x = BitmapFactory.decodeFile(step.getPic());
            thumbnailImageView.setImageBitmap(x);
            //thumbnailImageView.setImageURI(Uri.parse(new File(step.getPic()).toString()));
            //thumbnailImageView.setImageBitmap(img);
        }else{
            thumbnailImageView.setImageResource(R.mipmap.ic_launcher);
        }
        tv.setText( (position+1)+". "+step.getName());





        return rowView;

    }
}
