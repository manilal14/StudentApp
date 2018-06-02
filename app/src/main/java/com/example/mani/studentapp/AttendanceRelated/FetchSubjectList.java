package com.example.mani.studentapp.AttendanceRelated;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import static com.example.mani.studentapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.handleVolleyError;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.retrySeconds;

public class FetchSubjectList extends AppCompatActivity {

    RecyclerView recyclerView_class_List;
    List<Subject> mSubjectList;
    Integer mTeacher_id;

    SwipeRefreshLayout mSwipeRefreshLayout;
    SubjectAdapter mSubjectAdapter;

    LinearLayout mErrorLinearLayout;
    TextView mErrorTextView;
    Button mRetry;

    EditText mDialogDate;


    public static final String FETCH_CLASS_URL = BASE_URL_ATTENDANCE + "fetch_subjects_tought_by_teacher.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_subject_list);


       // mErrorTextView = findViewById(R.id.tv_error_message);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mErrorLinearLayout  = findViewById(R.id.ll_error_layout);
        mErrorTextView      = findViewById(R.id.tv_error_message);
        mRetry              = findViewById(R.id.btn_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSubjectsFromDatabase();
            }
        });

        recyclerView_class_List = findViewById(R.id.recycler_view_class_list);
        recyclerView_class_List.setHasFixedSize(true);

        mTeacher_id = getIntent().getIntExtra("teacher_id",0);
        mSubjectList = new ArrayList<>();

        loadSubjectsFromDatabase();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSubjectsFromDatabase();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fetch_subjects,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.menu_refresh :
                loadSubjectsFromDatabase();
                break;
            case R.id.menu_check_past_attendance :
                setDialogBox();

                //Toast.makeText(FetchSubjectList.this,"past attendence",Toast.LENGTH_SHORT).show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void setDialogBox() {

        Context context = FetchSubjectList.this;
        final AlertDialog.Builder alertDialog;

        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.dialog_check_past_attendance,null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(context,android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
        } else {
            alertDialog = new AlertDialog.Builder(context);
        }

        //alertDialog.setTitle("Check Past Attendance");
        alertDialog.setView(v);

        mDialogDate = v.findViewById(R.id.dialog_date);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    private void loadSubjectsFromDatabase() {

        mSwipeRefreshLayout.setRefreshing(true);
        mErrorLinearLayout.setVisibility(View.GONE);

        final StringRequest stringRequest =  new StringRequest(Request.Method.POST, FETCH_CLASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // First clear the adaptar if something is there
                        // useful on refreshing
                        if(mSubjectAdapter != null) {
                            mSubjectAdapter.clear();
                            mSubjectAdapter.addAll(mSubjectList);
                        }

                        try{

                            JSONArray allSubjects = new JSONArray(response);

                            if(allSubjects.length() == 0) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                mErrorLinearLayout.setVisibility(View.VISIBLE);
                                mErrorTextView.setText("No subjects found!");
                                mRetry.setVisibility(View.GONE);
                                return;
                            }

                            for (int i=0;i<allSubjects.length();i++)
                            {
                                JSONObject aSubject = allSubjects.getJSONObject(i);

                                int     subject_id   = aSubject.getInt("subject_id");
                                int     class_id     = aSubject.getInt("class_id");
                                String  subject_name = aSubject.getString("subject_name");
                                String  class_name   = aSubject.getString("class_name");
                                String  sem          = aSubject.getString("semester");

                                Subject subject = new Subject(subject_id,class_id,class_name,subject_name,sem);
                                mSubjectList.add(subject);
                            }

                            mSwipeRefreshLayout.setRefreshing(false);

                            mSubjectAdapter = new SubjectAdapter(FetchSubjectList.this, mSubjectList);
                            recyclerView_class_List.setLayoutManager(new LinearLayoutManager(FetchSubjectList.this));
                            recyclerView_class_List.setAdapter(mSubjectAdapter);

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
                params.put("teacher_id",String.valueOf(mTeacher_id));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds * 1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(FetchSubjectList.this).addToRequestQueue(stringRequest);
    }

    // On clicking select date in dialog
    public void launchCalender(View view) {

        final Calendar calendar =  Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(FetchSubjectList.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void updateLabel(Calendar calendar) {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        String date = sdf.format(calendar.getTime());
        mDialogDate.setText(date);
    }
}
