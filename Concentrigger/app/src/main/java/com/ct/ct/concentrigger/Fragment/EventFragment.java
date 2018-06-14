package com.ct.ct.concentrigger.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ct.ct.concentrigger.Activity.MapActivity;
import com.ct.ct.concentrigger.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    Button btnMap;


    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event, container, false);


        initItem(view);


        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    public void initItem(View view){
        btnMap = (Button)view.findViewById(R.id.btn_map);
    }

}
