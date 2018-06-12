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

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    Context mCtx;
    List<Student> mStudentList;

    public StudentAdapter(Context mCtx, List<Student> mStudentList) {
        this.mCtx = mCtx;
        this.mStudentList = mStudentList;
    }

    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View v = inflater.inflate(R.layout.fetch_student_list_single_layout,parent,false);
        return new StudentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.StudentViewHolder holder, int position) {

        final Student student = mStudentList.get(position);

        holder.tv_roll_no.setText(String.valueOf(student.getStudent_roll_no()));
        holder.tv_name.setText(student.getName());


        // To change the status of student according to checkbox
        holder.attendence_checkbox.setOnCheckedChangeListener(null);
        holder.attendence_checkbox.setChecked(student.isPresent());


        holder.attendence_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                student.setPresent(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{

        TextView tv_roll_no;
        TextView tv_name;
        CheckBox attendence_checkbox;

        public StudentViewHolder(View itemView) {
            super(itemView);
            tv_roll_no          = itemView.findViewById(R.id.tv_roll_no);
            tv_name             = itemView.findViewById(R.id.tv_student_name);
            attendence_checkbox = itemView.findViewById(R.id.checkbox_for_attendence);
        }
    }


    public void selectAll() {
        notifyDataSetChanged();
    }
    public void deSelectAll() {
        notifyDataSetChanged();
    }

    public void clear() {
        mStudentList.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<Student> list) {
        mStudentList.addAll(list);
        notifyDataSetChanged();

    }
}