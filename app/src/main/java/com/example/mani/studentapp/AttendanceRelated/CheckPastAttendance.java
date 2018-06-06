package com.example.mani.studentapp.AttendanceRelated;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mani.studentapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.retrySeconds;

public class CheckPastAttendance extends AppCompatActivity {

     List<PastAttendance> mPastAttendanceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_past_attendance);

        Bundle bundle = getIntent().getExtras();

        String date  = bundle.getString("date");
        int period   = bundle.getInt("period");
        int class_id = bundle.getInt("class_id");

        mPastAttendanceList = new ArrayList<>();

        /*Toast.makeText(CheckPastAttendance.this,""+date+" "+period+"  "+class_id,
                Toast.LENGTH_SHORT).show();*/

        fetchPastAttendance(date, period, class_id);
    }

    private void fetchPastAttendance(final String date, final int period, final int class_id) {


        String FETCH_PAST_ATTENDANCE_URL = BASE_URL_ATTENDANCE + "fetch_past_attendance.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_PAST_ATTENDANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       /* Toast.makeText(CheckPastAttendance.this,""+response,
                                Toast.LENGTH_SHORT).show();*/

                        try {

                            JSONArray  jsonArray      = new JSONArray(response);

                            if(jsonArray.length() == 0){
                                Toast.makeText(CheckPastAttendance.this,"Attendance not taken yet",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // comman variables to all

                            JSONObject zeroJsonObject =  jsonArray.getJSONObject(0);

                            int duration = zeroJsonObject.getInt("duration");
                            String sub   = zeroJsonObject.getString("subject_name");


                            for(int i=0; i<jsonArray.length();i++){

                                JSONObject temp = jsonArray.getJSONObject(i);
                                String name = temp.getString("student_name");
                                int status  = temp.getInt("status");

                                mPastAttendanceList.add(new PastAttendance(name,status));
                           }

                           List<String> studentList = new ArrayList<>();

                           for(int i =0;i<mPastAttendanceList.size();i++){
                               studentList.add(mPastAttendanceList.get(i).getName());
                           }

                           ListView listView    = findViewById(R.id.list_view_check_past_attendance);
                           ArrayAdapter adapter = new ArrayAdapter<>(CheckPastAttendance.this,
                                    android.R.layout.simple_list_item_1,studentList);

                           listView.setAdapter(adapter);









                           /*RecyclerView recyclerView = findViewById(R.id.recycler_view_check_past_attendance);

                            PastAttendanceAdapter pastAttendanceAdapter = new PastAttendanceAdapter(
                                    CheckPastAttendance.this,mPastAttendanceList);

                            recyclerView.setLayoutManager(new LinearLayoutManager(CheckPastAttendance.this));
                            recyclerView.setAdapter(pastAttendanceAdapter);*/




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

