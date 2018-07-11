package com.example.mani.sudoapp.AttendanceRelated;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mani.sudoapp.R;

import java.util.List;

public class EditPastAttendanceAdapter extends RecyclerView.Adapter
        <EditPastAttendanceAdapter.EditPastAttandanceViewHolder> {

    Context mCtx;
    List<PastAttendance> mPastAttendanceList;

    public EditPastAttendanceAdapter(Context mCtx, List<PastAttendance> mPastAttendanceList) {
        this.mCtx = mCtx;
        this.mPastAttendanceList = mPastAttendanceList;
    }

    @NonNull
    @Override
    public EditPastAttandanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View v = inflater.inflate(R.layout.fetch_student_list_single_layout,parent,false);
        return new EditPastAttandanceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EditPastAttandanceViewHolder holder, int position) {

        final PastAttendance aStudent = mPastAttendanceList.get(position);

        holder.roll_no.setText(String.valueOf(aStudent.getRoll_no()));
        holder.name.setText(aStudent.getName());

        holder.checkBox.setOnCheckedChangeListener(null);

        if(aStudent.getStatus() == false) {
            holder.checkBox.setChecked(false);
        }
        else{
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aStudent.setStatus(isChecked);

            }
        });




    }

    @Override
    public int getItemCount() {
        return mPastAttendanceList.size();
    }

    public class EditPastAttandanceViewHolder extends RecyclerView.ViewHolder {

        TextView roll_no;
        TextView name;
        CheckBox checkBox;

        public EditPastAttandanceViewHolder(View itemView) {
            super(itemView);

            roll_no  = itemView.findViewById(R.id.tv_roll_no);
            name     = itemView.findViewById(R.id.tv_student_name);
            checkBox = itemView.findViewById(R.id.checkbox_for_attendence);
        }
    }
}
