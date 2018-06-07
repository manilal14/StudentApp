package com.example.mani.studentapp.AttendanceRelated;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mani.studentapp.R;

import java.util.List;

public class PastAttendanceAdapter extends BaseAdapter {

    Context mCtx;
    List<PastAttendance> mPastAttendenceList;

    public PastAttendanceAdapter(Context mCtx, List<PastAttendance> mPastAttendenceList) {
        this.mCtx = mCtx;
        this.mPastAttendenceList = mPastAttendenceList;
    }

    @Override
    public int getCount() {
        return mPastAttendenceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPastAttendenceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view==null) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            view = inflater.inflate(R.layout.check_past_attendance_single_layout,null);
        }

        TextView tv_roll_no = view.findViewById(R.id.tv_roll_no);
        TextView tv_student_name = view.findViewById(R.id.tv_student_name);

        tv_roll_no.setText(String.valueOf(mPastAttendenceList.get(position).getRoll_no()));
        tv_student_name.setText(mPastAttendenceList.get(position).getName());

        if(mPastAttendenceList.get(position).getStatus() == 0){
            tv_student_name.setTextColor(Color.parseColor("#E50303"));
        }

        return view;
    }
}
