package com.example.mani.studentapp.AttendanceRelated;

import android.annotation.SuppressLint;
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
        View v = inflater.inflate(R.layout.fetch_past_attendance_single_layout,parent,false);
        return new PastAttendanceViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PastAttendanceAdapter.PastAttendanceViewHolder holder, int position) {

       PastAttendance aStudent = mPastAttendanceList.get(position);

       holder.tv_name.setText(aStudent.getName());

       if(aStudent.getStatus() == 0) {
           holder.tv_name.setTextColor(Color.parseColor("#000000"));
           holder.tv_name.setBackgroundColor(R.color.failed_red);
       }
    }

    @Override
    public int getItemCount() {
        return mPastAttendanceList.size();
    }

    public class PastAttendanceViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;

        public PastAttendanceViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
