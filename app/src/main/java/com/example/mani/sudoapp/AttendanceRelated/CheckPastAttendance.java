package com.example.mani.sudoapp.AttendanceRelated;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mani.sudoapp.NewsRelaled.MySingleton;
import com.example.mani.sudoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mani.sudoapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.handleVolleyError;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.retrySeconds;

public class CheckPastAttendance extends AppCompatActivity {

    List<PastAttendance> mPastAttendanceList;

    LinearLayout mMainLayout;
    ProgressBar mProgressBar;
    LinearLayout mErrorLinearLayout;
    TextView mErrorTextView;
    Button mRetry;

    String mDate;
    int mPeriod;
    int mClass_id;
    String mSubjectName;
    int mDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init(){

        setContentView(R.layout.activity_check_past_attendance);

        Bundle bundle = getIntent().getExtras();

        mDate     = bundle.getString("date");
        mPeriod   = bundle.getInt("period");
        mClass_id = bundle.getInt("class_id");

        Log.e("tag",mDate+" "+mPeriod+" "+mClass_id);

        mPastAttendanceList = new ArrayList<>();

        mMainLayout         = findViewById(R.id.main_check_past_attendance_layout);
        mProgressBar        = findViewById(R.id.progress_bar);
        mErrorLinearLayout  = findViewById(R.id.ll_error_layout);
        mErrorTextView      = findViewById(R.id.tv_error_message);
        mRetry              = findViewById(R.id.btn_retry);

        fetchPastAttendance();

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPastAttendance();
                mErrorLinearLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_past_attendance,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.menu_check_past_attendance_edit:
                editPastAttendanceDialog();
                break;

            case R.id.menu_check_past_attendance_info :
                creatDialogForMoreInfo();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editPastAttendanceDialog() {

        final AlertDialog alertDialog;
        Context context = CheckPastAttendance.this;

        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.dialog_edit_past_attendance,null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(context,android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth).create();
        } else {
            alertDialog = new AlertDialog.Builder(context).create();
        }

        alertDialog.setView(v);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_edit_past_attendance);
        EditPastAttendanceAdapter adapter = new EditPastAttendanceAdapter(context,mPastAttendanceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckPastAttendance.this));
        recyclerView.setAdapter(adapter);

        TextView done   = v.findViewById(R.id.dialog_done);
        TextView cancle = v.findViewById(R.id.dialog_cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                   updatePastAttendance();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                alertDialog.dismiss();

                mMainLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);


            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();

    }

    private void updatePastAttendance() throws JSONException {

        final JSONArray jsonArray = new JSONArray();

        for(int i=0;i<mPastAttendanceList.size();i++){

            PastAttendance p = mPastAttendanceList.get(i);

            int status = 0 ;
            if(p.getStatus() == true)
                status = 1;

            JSONObject tempObject = new JSONObject();

            tempObject.put("student_name",p.getName());
            tempObject.put("attendance_id",p.getAttendance_id());
            tempObject.put("status",status);

            jsonArray.put(tempObject);
        }

        //Log.e("updated attendance",jsonArray.toString());

        final String URL_EDIT_PAST_ATTENDANCE = BASE_URL_ATTENDANCE+"update_past_attendance.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PAST_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("tag","onResponse "+response);
                        fetchPastAttendance();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.e("tag","params sending");

                Map<String,String> params = new HashMap<>();
                params.put("past_attendance_to_be_edited",jsonArray.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(retrySeconds*1000,maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(CheckPastAttendance.this).addToRequestQueue(stringRequest);
    }


    private void creatDialogForMoreInfo() {

        final AlertDialog.Builder alertDialog;
        Context mCtx = CheckPastAttendance.this;

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View v = inflater.inflate(R.layout.dialog_past_attandance_info,null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(mCtx,android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
        } else {
            alertDialog = new AlertDialog.Builder(mCtx);
        }

        alertDialog.setView(v);

        TextView tv_date    = v.findViewById(R.id.date);
        TextView tv_subject = v.findViewById(R.id.subject);
        TextView tv_period  = v.findViewById(R.id.peroid);
        TextView tv_no_of_hrs      = v.findViewById(R.id.no_of_hrs);
        TextView tv_total_strength = v.findViewById(R.id.total_strength);
        TextView tv_present        = v.findViewById(R.id.present);

        int total_strength = mPastAttendanceList.size();
        int present = 0;

        for(int i=0;i<mPastAttendanceList.size();i++){
            if(mPastAttendanceList.get(i).getStatus() == true)
                present ++;
        }

        tv_date.setText(mDate);
        tv_subject.setText(mSubjectName);
        tv_period.setText(String.valueOf(mPeriod));
        tv_no_of_hrs.setText(String.valueOf(maxNoOfTries));
        tv_total_strength.setText(String.valueOf(total_strength));
        tv_present.setText(String.valueOf(present));

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    private void fetchPastAttendance() {

        mMainLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        //mPastAttendanceList = new ArrayList<>();

        mPastAttendanceList.clear();

        final String FETCH_PAST_ATTENDANCE_URL = BASE_URL_ATTENDANCE + "fetch_past_attendance.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_PAST_ATTENDANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        PastAttendanceAdapter pastAttendanceAdapter = null;

                        if(pastAttendanceAdapter != null) {
                            pastAttendanceAdapter.clear();
                            pastAttendanceAdapter.addAll(mPastAttendanceList);
                        }

                       try {

                           JSONArray  jsonArray = new JSONArray(response);

                           if(jsonArray.length() == 0){

                               mProgressBar.setVisibility(View.GONE);
                               mErrorLinearLayout.setVisibility(View.VISIBLE);
                               getSupportActionBar().hide();
                               mErrorTextView.setText("Attendence yet not taken");
                               mRetry.setText("OK");
                               mRetry.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       finish();
                                   }
                               });
                               return;
                           }

                            // common variables to all
                           JSONObject zeroJsonObject =  jsonArray.getJSONObject(0);
                           mDuration    = zeroJsonObject.getInt("duration");
                           mSubjectName = zeroJsonObject.getString("subject_name");


                           for(int i=0; i<jsonArray.length();i++){

                               JSONObject temp = jsonArray.getJSONObject(i);
                               int attendance_id = temp.getInt("attendance_id");
                               String name       = temp.getString("student_name");
                               int status        = temp.getInt("status");

                               mPastAttendanceList.add(new PastAttendance(attendance_id,i+1,name,status));
                           }

                           TextView tv_date;
                           TextView tv_period;
                           TextView tv_subject;
                           TextView tv_duration;

                           tv_date     = findViewById(R.id.tv_date);
                           tv_period   = findViewById(R.id.tv_period);
                           tv_subject  = findViewById(R.id.tv_subject);
                           tv_duration = findViewById(R.id.tv_duration);

                           mProgressBar.setVisibility(View.GONE);
                           mMainLayout.setVisibility(View.VISIBLE);

                           tv_date.setText(mDate);
                           tv_period.setText(String.valueOf("P"+mPeriod));
                           tv_subject.setText(mSubjectName);
                           tv_duration.setText(String.valueOf(mDuration+" hrs"));

                           RecyclerView recyclerView = findViewById(R.id.recycler_view_check_past_attendance);
                           pastAttendanceAdapter = new PastAttendanceAdapter(
                                    CheckPastAttendance.this,mPastAttendanceList);
                           recyclerView.setLayoutManager(new LinearLayoutManager(CheckPastAttendance.this));
                           recyclerView.setAdapter(pastAttendanceAdapter);

                       } catch (JSONException e) {
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

                params.put("date",mDate);
                params.put("class_id",String.valueOf(mClass_id));
                params.put("period",String.valueOf(mPeriod));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds*1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CheckPastAttendance.this).addToRequestQueue(stringRequest);
    }
}

