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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myroutine.myroutine.dynamicgrid.BaseDynamicGridAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by basmatebe on 4/19/17.
 */

public class StepGridAdp extends BaseDynamicGridAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
   // private List<Step> mDataSource;
    List<Step> original;
    List<Step> items;
    Activity act;


    protected StepGridAdp(Context context, List<Step> items, int columnCount, List<Step> ls, Activity act) {
        super(context, items,columnCount);
        original = ls;
        this.items = items;
        mContext = context;
        this.act = act;
        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    public List<Step> getList(){
        return items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Step steps  = (Step)getItem(position);

       /* if(original.get(position).getId() == steps.getId()){
            Toast.makeText(mContext, original.get(position).getId()+" "+steps.getId(), Toast.LENGTH_SHORT).show();
        }*/
        //Toast.makeText(mContext, original.get(position).getId()+" "+steps.getId(), Toast.LENGTH_SHORT).show();


        /*int n = 0;
        for(int i=0; i<original.size(); i++){
            if(original.get(i).getPic().equals(items.get(i).getPic())){
                n++;
            }
        }*/
        /*if(n == original.size()){
            Toast.makeText(mContext, "matched", Toast.LENGTH_SHORT).show();
        }*/
        //showMessage("data", original.get(0).getId()+" second"+items.get(0).getId());

        StepViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_item, parent,false);
            holder = new StepViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StepViewHolder) convertView.getTag();
        }
        Step step  = (Step)getItem(position);
        holder.build(step.getPic());
        return convertView;
    }

    private class StepViewHolder {
        // private TextView titleText;
        private ImageView image;

        private StepViewHolder(View view) {
            //titleText = (TextView) view.findViewById(R.id.stepn);
            image = (ImageView) view.findViewById(R.id.gridpic2);
        }

        void build(String img) {
            //titleText.setText(title);
            if(img != null){
                Bitmap x = BitmapFactory.decodeFile(img);
                image.setImageBitmap(x);

            }else{
                image.setImageResource(R.mipmap.ic_launcher);
            }

        }
    }

    public void showMessage(String title, String message){
        android.app.AlertDialog.Builder dia = new android.app.AlertDialog.Builder(act);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }




}
