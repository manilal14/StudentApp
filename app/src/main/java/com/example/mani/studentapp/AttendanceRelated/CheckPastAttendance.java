package com.example.mani.studentapp.AttendanceRelated;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mani.studentapp.R;

public class CheckPastAttendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_past_attendance);

        Intent i = getIntent();

        String date  = i.getStringExtra("date");
        int period   = i.getIntExtra("period",0);
        int class_id = i.getIntExtra("class_id",0);

        Toast.makeText(CheckPastAttendance.this,""+date+" "+period+"  "+class_id,
                Toast.LENGTH_SHORT).show();
    }

}
