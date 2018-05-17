package com.example.mani.studentapp.TimeTableRelated;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mani.studentapp.R;


public class WednesdayFragment extends Fragment {

    EditText t1,t2,t3,t4,t5,t6;
    String sm1,sm2,sm3,sm4,sm5,sm6;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wednesday, container, false);

        sharedPreferences = getContext().getSharedPreferences(TimeTable.MY_PREFERENCES, Context.MODE_PRIVATE);

        t1 = v.findViewById(R.id.ew1);
        t2 = v.findViewById(R.id.ew2);
        t3 = v.findViewById(R.id.ew3);
        t4 = v.findViewById(R.id.ew4);
        t5 = v.findViewById(R.id.ew5);
        t6 = v.findViewById(R.id.ew6);

        return v;
    }

    public void saveWednesdayTimeTable() {

        sm1 = t1.getText().toString().trim();
        sm2 = t2.getText().toString().trim();
        sm3 = t3.getText().toString().trim();
        sm4 = t4.getText().toString().trim();
        sm5 = t5.getText().toString().trim();
        sm6 = t6.getText().toString().trim();


        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(! (sm1.equals("") && sm2.equals("") && sm3.equals("") &&
                sm4.equals("") && sm5.equals("") && sm6.equals("")) ) {

            editor.putString("w1key", sm1);
            editor.putString("w2key", sm2);
            editor.putString("w3key", sm3);
            editor.putString("w4key", sm4);
            editor.putString("w5key", sm5);
            editor.putString("w6key", sm6);
            editor.commit();
        }


    }
}
