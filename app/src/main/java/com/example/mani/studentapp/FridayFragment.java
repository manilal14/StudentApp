package com.example.mani.studentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class FridayFragment extends Fragment {

    EditText er1,er2,er3,er4,er5,er6;
    public static String sr1,sr2,sr3,sr4,sr5,sr6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_friday, container, false);

        er1 = view.findViewById(R.id.er1);
        er2 = view.findViewById(R.id.er2);
        er3 = view.findViewById(R.id.er3);
        er4 = view.findViewById(R.id.er4);
        er5 = view.findViewById(R.id.er5);
        er6 = view.findViewById(R.id.er6);

        sr1 = er1.getText().toString().trim();
        sr2 = er2.getText().toString().trim();
        sr3 = er3.getText().toString().trim();
        sr4 = er4.getText().toString().trim();
        sr5 = er5.getText().toString().trim();
        sr6 = er6.getText().toString().trim();



        return view;
    }
}
