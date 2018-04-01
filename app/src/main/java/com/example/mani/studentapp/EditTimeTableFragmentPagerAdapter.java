package com.example.mani.studentapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EditTimeTableFragmentPagerAdapter extends FragmentPagerAdapter {

    public EditTimeTableFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch(position){


            case 0: return new MondayFragment();
            case 1: return new TuesdayFragment();
            case 2: return new WednesdayFragment();
            case 3: return new ThrusdayFragment();
            case 4: return new FridayFragment();

        }

        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "Monday";
            case 1: return "Tuesday";
            case 2: return "Wednesday";
            case 3: return "Thrusday";
            case 4: return "Friday";

        }

        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
