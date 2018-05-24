package com.example.mani.studentapp.AttendanceRelated;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mani.studentapp.NewsRelaled.MySingleton;
import com.example.mani.studentapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.mani.studentapp.CommonVariablesAndFunctions.handleVolleyError;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.retrySeconds;

public class FetchStudentList extends AppCompatActivity {

    RecyclerView recyclerView_studentList;
    List<Student> mStudentList;
    Integer mClass_id;
    Integer mSubject_id;
    Calendar mCalendar;
    TextView tv_choose_date;
    StudentAdapter mStudentAdapter;
    Integer mPeriod,mHrs;
    String mDateSelected = null;
    Spinner spinner_period,spinner_duration;

    LinearLayout mStudentListLayout;

    ProgressBar mProgressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout mErrorLinearLayout;
    TextView mErrorTextView;
    Button mRetry;

    String FETCH_STUDENTS_URL = "http://192.168.43.154/studentApp_attendance/fetch_studentList.php";
    String SUBMIT_ATTENDENCE_URL = "http://192.168.43.154/studentApp_attendance/send_attendence_to_database.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_student_list);


        mProgressBar       = findViewById(R.id.progress_bar);

        mStudentListLayout = findViewById(R.id.ll_student_list );

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mErrorLinearLayout  = findViewById(R.id.ll_error_layout);
        mErrorTextView      = findViewById(R.id.tv_error_message);
        mRetry              = findViewById(R.id.btn_retry);


        recyclerView_studentList = findViewById(R.id.recycler_view_student_list);
        recyclerView_studentList.setHasFixedSize(true);

        mClass_id   = getIntent().getIntExtra("class_id",0);
        mSubject_id = getIntent().getIntExtra("subject_id",0);



        mStudentList = new ArrayList<>();

        // Calling function to get Student list of a particular class
        loadStudentsFromDatabase();

        // Launching calender
        tv_choose_date = findViewById(R.id.tv_choose_date);
        tv_choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCalender();
            }
        });


        spinner_period = findViewById(R.id.spinner_choose_peroid);
        spinner_duration = findViewById(R.id.spinner_choose_duration);

        String [] periods = new String[]{"Period : 1","Period : 2","Period : 3",
                "Period : 4","Period : 5","Period : 6"};
        String [] hrs = new String[]{"1 hrs","2 hrs","3 hrs"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(FetchStudentList.this,
                R.layout.spinner_item,periods);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_items);
        spinner_period.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(FetchStudentList.this,
                R.layout.spinner_item,hrs);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_items);
        spinner_duration.setAdapter(adapter2);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStudentsFromDatabase();
            }
        });

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStudentsFromDatabase();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // If there are no students, then there is no need of menu.
        if(mStudentList.size() == 0) {
            Toast.makeText(FetchStudentList.this,"No Students available",Toast.LENGTH_SHORT).show();
            return true;
        }

        int id = item.getItemId();
        switch (id){

            case R.id.menu_submit_attendance:
                prepareForSubmission();
                break;

            case R.id.menu_select_all:
                mStudentAdapter.selectAll();
                for(int i=0;i<mStudentList.size();i++) {
                    mStudentList.get(i).setPresent(true);
                }

                break;

            case R.id.menu_deselect_all:
                mStudentAdapter.deSelectAll();
                for (int i=0;i<mStudentList.size();i++)
                    mStudentList.get(i).setPresent(false);
                break;
        }


        return super.onOptionsItemSelected(item);
    }


    private void launchCalender() {
        mCalendar =  Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        new DatePickerDialog(FetchStudentList.this, date, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        mDateSelected = sdf.format(mCalendar.getTime());
        tv_choose_date.setText(mDateSelected);

    }




    private void loadStudentsFromDatabase() {

        mSwipeRefreshLayout.setRefreshing(true);

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, FETCH_STUDENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // First clear the adaptar if something is there
                        // useful on refreshing
                        if(mStudentAdapter != null) {
                            mStudentAdapter.clear();
                            mStudentAdapter.addAll(mStudentList);
                        }


                        try{

                            JSONArray students = new JSONArray(response);

                            if(students.length() == 0){
                                mSwipeRefreshLayout.setRefreshing(false);
                                mErrorLinearLayout.setVisibility(View.VISIBLE);
                                mErrorTextView.setText("No students are available !");
                                mRetry.setVisibility(View.GONE);
                                return;
                            }


                            for (int i=0;i<students.length();i++) {
                                JSONObject studentJSONObject = students.getJSONObject(i);

                                int student_id    = studentJSONObject.getInt("student_id");
                                int student_roll_no = studentJSONObject.getInt("student_roll_no");
                                String  name = studentJSONObject.getString("student_name");
                                mStudentList.add(new Student(student_id,student_roll_no,name));
                            }

                            mErrorLinearLayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);

                            mStudentAdapter = new StudentAdapter(FetchStudentList.this,mStudentList);
                            recyclerView_studentList.setLayoutManager(new LinearLayoutManager(FetchStudentList.this));
                            recyclerView_studentList.setAdapter(mStudentAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error,mSwipeRefreshLayout,mErrorTextView,mErrorLinearLayout);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("class_id",String.valueOf(mClass_id));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds*1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(FetchStudentList.this).addToRequestQueue(stringRequest);

    }

    private void prepareForSubmission() {

        if(mDateSelected == null) {
            Toast.makeText(FetchStudentList.this,"Please select date",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        mPeriod = spinner_period.getSelectedItemPosition() + 1;
        mHrs = spinner_duration.getSelectedItemPosition() + 1;
        int totalStrength = mStudentList.size();
        int presentStrength = 0;
        int absentStrength;

        // common properties like class_id,date,period... and properties
        // related to specific student are separated

        // Common properties
        final JSONArray commonProperties = new JSONArray();
        JSONObject temp = new JSONObject();
        try {
            temp.put("subject_id",mSubject_id);
            temp.put("class_id",mClass_id);
            temp.put("date_selected",mDateSelected);
            temp.put("period",mPeriod);
            temp.put("duration",mHrs);

            commonProperties.put(temp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Specific properties
        final JSONArray studentJsonArray = new JSONArray();

        for (int i=0;i<mStudentList.size();i++) {

            try {

                JSONObject tempObject = new JSONObject();

                tempObject.put("student_id",mStudentList.get(i).getStudent_id());
                tempObject.put("student_name",mStudentList.get(i).getName());
                if(mStudentList.get(i).isPresent()) {
                    presentStrength++;
                    tempObject.put("is_present", 1);
                }
                else
                    tempObject.put("is_present",0);

                studentJsonArray.put(tempObject);

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        absentStrength = totalStrength - presentStrength;

        AlertDialog.Builder alertDialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(FetchStudentList.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(FetchStudentList.this);
        }

        alertDialog.setTitle("ATTENDANCE REPORT");
        alertDialog.setMessage("Date Selected  "+ mDateSelected +
                "\nPeriod no. "+ mPeriod+
                "\nNo. of hours "+ mHrs+
                "\nTotal Strength "+ totalStrength +
                "\nPresent " +presentStrength +
                "\nAbsent "+absentStrength+
                "\nAre you sure, you want to submit?");


        final int finalPresentStrength = presentStrength;

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        //mSwipeRefreshLayout.setRefreshing(true);
                        mStudentListLayout.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                        submitAttendance(commonProperties, studentJsonArray, finalPresentStrength);
                    }
                });

        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void submitAttendance(final JSONArray commomProperties,
                                  final JSONArray studentJsonArray, final int presentStrength) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_ATTENDENCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            mErrorLinearLayout.setVisibility(View.GONE);
                            mProgressBar.setVisibility(View.GONE);

                            JSONObject jsonObject = new JSONObject(response);
                            int responseCode      = jsonObject.getInt("responseCode");
                            String responseMessage = jsonObject.getString("message");

                            setResponseLayout(responseCode,presentStrength);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error,mProgressBar,mErrorTextView,mErrorLinearLayout);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("commonJsonArray",commomProperties.toString());
                params.put("studentJsonArray",studentJsonArray.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds*1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(FetchStudentList.this).addToRequestQueue(stringRequest);
    }


    private void setResponseLayout(int responseCode, int presentStrength){

        // Response message if attendance taken successcfully
        if(responseCode == 1){

            LinearLayout successResponseLayout;
            TextView srlDate,srlPeriod,srlNoOfHrs,srlTotalStrength,srlPresent;
            Button srlOk;

            successResponseLayout = findViewById(R.id.ll_success_response_layout);
            successResponseLayout.setVisibility(View.VISIBLE);


            srlDate          = findViewById(R.id.srl_date);
            srlPeriod        = findViewById(R.id.srl_peroid);
            srlNoOfHrs       = findViewById(R.id.srl_no_of_hrs);
            srlTotalStrength = findViewById(R.id.srl_total_strength);
            srlPresent       = findViewById(R.id.srl_present);

            srlOk            = findViewById(R.id.srl_ok);

            srlDate.setText(mDateSelected);
            srlPeriod.setText(String.valueOf(mPeriod));
            srlNoOfHrs.setText(String.valueOf(mHrs));
            srlTotalStrength.setText(String.valueOf(mStudentList.size()));
            srlPresent.setText(String.valueOf(presentStrength));

            srlOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        // Response message if attendance is already taken
        else if(responseCode == 0){

            LinearLayout attandaceAlreadyTakenLL;
            Button ok;
            TextView updateAttendace;

            attandaceAlreadyTakenLL = findViewById(R.id.ll_attandence_already_taken_response_layout);
            ok = findViewById(R.id.alert_ok);
            updateAttendace = findViewById(R.id.tv_update_attendance);

            attandaceAlreadyTakenLL.setVisibility(View.VISIBLE);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            updateAttendace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FetchStudentList.this,"stay tuned for this feature",Toast.LENGTH_SHORT).show();
                }
            });


        }
        //Default response layout
        else{

            LinearLayout defaultResponseLayout;
            Button defaultRetry;

            defaultResponseLayout = findViewById(R.id.default_response_layout);
            defaultRetry = findViewById(R.id.default_retry);

            defaultResponseLayout.setVisibility(View.VISIBLE);
            defaultRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

    }

}