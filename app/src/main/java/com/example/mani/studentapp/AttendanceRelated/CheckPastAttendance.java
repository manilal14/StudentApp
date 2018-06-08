package com.example.mani.studentapp.AttendanceRelated;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.mani.studentapp.NewsRelaled.MySingleton;
import com.example.mani.studentapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mani.studentapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.handleVolleyError;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.retrySeconds;

public class CheckPastAttendance extends AppCompatActivity {

    List<PastAttendance> mPastAttendanceList;

    LinearLayout mMainLayout;
    ProgressBar mProgressBar;
    LinearLayout mErrorLinearLayout;
    TextView mErrorTextView;
    Button mRetry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_past_attendance);

        Bundle bundle = getIntent().getExtras();

        final String date  = bundle.getString("date");
        final int period   = bundle.getInt("period");
        final int class_id = bundle.getInt("class_id");

        mPastAttendanceList = new ArrayList<>();

        mMainLayout         = findViewById(R.id.main_check_past_attendance_layout);
        mProgressBar        = findViewById(R.id.progress_bar);
        mErrorLinearLayout  = findViewById(R.id.ll_error_layout);
        mErrorTextView      = findViewById(R.id.tv_error_message);
        mRetry              = findViewById(R.id.btn_retry);

        fetchPastAttendance(date, period, class_id);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPastAttendance(date,period,class_id);
                mErrorLinearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void fetchPastAttendance(final String date, final int period, final int class_id) {

        mMainLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        String FETCH_PAST_ATTENDANCE_URL = BASE_URL_ATTENDANCE + "fetch_past_attendance.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_PAST_ATTENDANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       /*Toast.makeText(CheckPastAttendance.this,""+response,
                                Toast.LENGTH_SHORT).show();*/
                       try {

                           JSONArray  jsonArray = new JSONArray(response);

                           if(jsonArray.length() == 0){

                               mProgressBar.setVisibility(View.GONE);
                               mErrorLinearLayout.setVisibility(View.VISIBLE);
                               getSupportActionBar().hide();
                               mErrorTextView.setText("Attendence yet not taken.");
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

                           int duration    = zeroJsonObject.getInt("duration");
                           String subject  = zeroJsonObject.getString("subject_name");


                           for(int i=0; i<jsonArray.length();i++){

                               JSONObject temp = jsonArray.getJSONObject(i);
                               String name = temp.getString("student_name");
                               int status  = temp.getInt("status");

                                mPastAttendanceList.add(new PastAttendance(i+1,name,status));
                           }

                           /*Toast.makeText(CheckPastAttendance.this,"sfsr",
                                    Toast.LENGTH_SHORT).show();*/

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

                           tv_date.setText(date);
                           tv_period.setText(String.valueOf("P"+period));
                           tv_subject.setText(subject);
                           tv_duration.setText(String.valueOf(duration+" hrs"));

                           RecyclerView recyclerView = findViewById(R.id.recycler_view_check_past_attendance);
                           PastAttendanceAdapter pastAttendanceAdapter = new PastAttendanceAdapter(
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

                params.put("date",date);
                params.put("class_id",String.valueOf(class_id));
                params.put("period",String.valueOf(period));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds*1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CheckPastAttendance.this).addToRequestQueue(stringRequest);
    }
}

