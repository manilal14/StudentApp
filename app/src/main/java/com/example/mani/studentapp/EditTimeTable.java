package com.example.mani.studentapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class EditTimeTable extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_table);

        // To remove the shadow between appbar and tabbar
        getSupportActionBar().setElevation(0);

        ViewPager viewPager = findViewById(R.id.viewpager);
        EditTimeTableFragmentPagerAdapter adapter = new EditTimeTableFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);



    }





}
