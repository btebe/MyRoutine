package com.myroutine.myroutine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.myroutine.myroutine.dynamicgrid.DynamicGridView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private static final String TAG = Game.class.getName();

    DynamicGridView gridView;
    DatabaseHelper mydb;

    public GameFragment() {

        //gridView = (DynamicGridView) findViewById(R.id.girdview2);
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mydb = new DatabaseHelper(getContext());
        View myFragmentView = inflater.inflate(R.layout.fragment_blank, container, false);



        // Inflate the layout for this fragment
        return myFragmentView;
    }


}
