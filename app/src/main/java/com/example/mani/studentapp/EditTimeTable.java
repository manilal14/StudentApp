package com.example.mani.studentapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EditTimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_table);

        Button btnMonday = findViewById(R.id.btn_monday);
        Button btnTuesday = findViewById(R.id.btn_tuesday);

        btnMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTimeTableChangeFragment(v);
            }
        });

        btnTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTimeTableChangeFragment(v);
            }
        });
    }

    public void editTimeTableChangeFragment(View view)
    {
        Fragment fragment;

        if(view == findViewById(R.id.btn_monday))
        {
            fragment = new FragmentMonday();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_edit_time_table,fragment);
            ft.commit();

        }

        else if(view == findViewById(R.id.btn_tuesday))
        {
            fragment = new FragmentTuesday();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_edit_time_table,fragment);
            ft.commit();
        }

    }


}
