package com.example.mani.studentapp.AttendanceRelated;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mani.studentapp.R;

public class AttendanceHomePage extends AppCompatActivity {

    TextView mTakeAttendence, mSeeAttendence;
    String mTeacher_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_home_page);

        mSeeAttendence = findViewById(R.id.see_attendence);
        mTakeAttendence = findViewById(R.id.take_attendence);

        /*
            Main Function of this OnclickListener is to ask for teacher id and
            send it(in int form) to new activity showing classes taught
            by that teacher having given id.
         */
        mTakeAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog =  new AlertDialog.Builder(AttendanceHomePage.this);
                alertDialog.setTitle("Welcome Teacher");
                alertDialog.setMessage("Enter Your id");

                final EditText input = new EditText(AttendanceHomePage.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mTeacher_id = input.getText().toString().trim();
                                int teacher_id ;

                                /*
                                    checking if String mTeacherId is convertible to int or not
                                    Now it is not needed as input type is set to number
                                  */

                                try {
                                   teacher_id = Integer.parseInt(mTeacher_id);
                                    Intent i = new Intent(AttendanceHomePage.this,FetchSubjectList.class);
                                    i.putExtra("teacher_id",teacher_id);
                                    startActivity(i);

                                }catch (NumberFormatException e) {
                                    Toast.makeText(AttendanceHomePage.this,"Fill Correct Id",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                alertDialog.setNegativeButton("Cancle",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        /**
         *  Take college_id,branch_id,sem_id and student_id from students
         *  and show them their attendance
         */
        mSeeAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = AttendanceHomePage.this;
                final AlertDialog.Builder alertDialog;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog = new AlertDialog.Builder(context,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
                } else {
                    alertDialog = new AlertDialog.Builder(context);
                }
                alertDialog.setTitle("Welcome Student");
                alertDialog.setMessage("Enter Your details");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText et_college_id = new EditText(context);
                et_college_id.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_college_id.setHint("college id");
                layout.addView(et_college_id);

                final EditText et_branch_id = new EditText(context);
                et_branch_id.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_branch_id.setHint("branch id");
                layout.addView(et_branch_id);

                final EditText et_sem_id = new EditText(context);
                et_sem_id.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_sem_id.setHint("sem id");
                layout.addView(et_sem_id);

                final EditText et_student_id = new EditText(context);
                et_student_id.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_student_id.setHint("student_id");
                layout.addView(et_student_id);

                alertDialog.setView(layout);


                alertDialog.setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Integer college_id;
                                Integer branch_id;
                                Integer sem_id;
                                Integer student_id;

                                try {

                                    college_id  = Integer.parseInt(et_college_id.getText().toString().trim());
                                    branch_id   = Integer.parseInt(et_branch_id.getText().toString().trim());
                                    sem_id      = Integer.parseInt(et_sem_id.getText().toString().trim());
                                    student_id  = Integer.parseInt(et_student_id.getText().toString().trim());

                                    Intent i = new Intent(AttendanceHomePage.this,CheckAttendance.class);
                                    i.putExtra("college_id",college_id);
                                    i.putExtra("branch_id",branch_id);
                                    i.putExtra("sem_id",sem_id);
                                    i.putExtra("student_id",student_id);

                                    startActivity(i);

                                }catch (NumberFormatException e){
                                    Toast.makeText(AttendanceHomePage.this,"Fill all details",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                alertDialog.setNegativeButton("Cancle",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();

            }
        });
    }


}
