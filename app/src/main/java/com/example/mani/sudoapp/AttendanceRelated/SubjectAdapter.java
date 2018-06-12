package com.example.mani.sudoapp.AttendanceRelated;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mani.sudoapp.R;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.CLASSViewHolder> {

    private Context mCtx;
    private List<Subject> mSubjectList;

    public SubjectAdapter(Context mCtx, List<Subject> mSubjectList) {

        this.mCtx = mCtx;
        this.mSubjectList = mSubjectList;
    }

    @NonNull
    @Override
    public CLASSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View v = inflater.inflate(R.layout.fetch_subject_list_single_layout,null);

        return new CLASSViewHolder(v,mCtx, mSubjectList);
    }

    @Override
    public void onBindViewHolder(@NonNull CLASSViewHolder holder, int position) {

        Subject aSubject = mSubjectList.get(position);
        holder.tv_subject.setText(aSubject.getSubjectName());
        holder.tv_semester.setText(aSubject.getSemester());
        holder.tv_class.setText(aSubject.getClassName());
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }


    public class CLASSViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_subject;
        TextView tv_semester;
        TextView tv_class;
        List<Subject> subjectList = new ArrayList<>();
        Context ctx;

        public CLASSViewHolder(View itemView,Context ctx,List<Subject> subjectList) {
            super(itemView);
            this.subjectList = subjectList;
            this.ctx = ctx;

            itemView.setOnClickListener(this);

            tv_subject  = itemView.findViewById(R.id.tv_subject);
            tv_semester = itemView.findViewById(R.id.tv_semester);
            tv_class    = itemView.findViewById(R.id.tv_class);
        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Subject aSubject = this.subjectList.get(position);

            Intent i = new Intent(this.ctx,FetchStudentList.class);

            i.putExtra("class_id", aSubject.getClass_id());
            i.putExtra("subject_id",aSubject.getSubject_id());
            this.ctx.startActivity(i);
        }
    }

    public void clear() {
        mSubjectList.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<Subject> list) {
        mSubjectList.addAll(list);
        notifyDataSetChanged();

    }


}