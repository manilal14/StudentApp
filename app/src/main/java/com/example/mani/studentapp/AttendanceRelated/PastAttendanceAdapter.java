package com.example.mani.studentapp.AttendanceRelated;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mani.studentapp.R;

import java.util.List;

public class PastAttendanceAdapter extends RecyclerView.Adapter<PastAttendanceAdapter.PastAttendanceViewHolder>{

    Context mCtx;
    List<PastAttendance> mPastAttendanceList;

    public PastAttendanceAdapter(Context mCtx, List<PastAttendance> mPastAttendanceList) {
        this.mCtx = mCtx;
        this.mPastAttendanceList = mPastAttendanceList;
    }

    @NonNull
    @Override
    public PastAttendanceAdapter.PastAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View v = inflater.inflate(R.layout.check_past_attendance_single_layout,parent,false);
        return new PastAttendanceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PastAttendanceAdapter.PastAttendanceViewHolder holder, int position) {

        PastAttendance aStudent = mPastAttendanceList.get(position);

        holder.tv_roll_no.setText(String.valueOf(aStudent.getRoll_no()));
        holder.tv_name.setText(aStudent.getName());

        if(aStudent.getStatus() == 0) {
            holder.tv_name.setTextColor(Color.parseColor("#E50303"));

        }
    }

    @Override
    public int getItemCount() {
        return mPastAttendanceList.size();
    }

    public class PastAttendanceViewHolder extends RecyclerView.ViewHolder{

        TextView tv_roll_no, tv_name;

        public PastAttendanceViewHolder(View itemView) {
            super(itemView);

            tv_roll_no = itemView.findViewById(R.id.tv_roll_no);
            tv_name    = itemView.findViewById(R.id.tv_student_name);
        }
    }
}
