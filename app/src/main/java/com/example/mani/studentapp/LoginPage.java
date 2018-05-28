package com.example.mani.studentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mani.studentapp.NewsRelaled.MySingleton;
import com.example.mani.studentapp.NewsRelaled.NewsFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mani.studentapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.studentapp.CommonVariablesAndFunctions.retrySeconds;

public class LoginPage extends AppCompatActivity {

    TextView btn_login,btn_register;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        btn_login  = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        skip       = findViewById(R.id.tv_skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, NewsFeed.class));
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpAleartBox();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToStudentApp();
            }
        });


    }

    private void loginToStudentApp() {

        final EditText et_login, etPassword;
        final String student_id;
        final String password;

        final String URL_LOGIN = BASE_URL_ATTENDANCE + "login.php";

        et_login    = findViewById(R.id.et_login_userid);
        etPassword  = findViewById(R.id.et_login_password);

        student_id = et_login.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(student_id.equals("") || password.equals("")){
            Toast.makeText(LoginPage.this, "Enter user_id and password", Toast.LENGTH_SHORT).show();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray studentArray = new JSONArray(response);
                            JSONObject studentObj = studentArray.getJSONObject(0);

                            int responseCode = studentObj.getInt("response_code");
                            String message   = studentObj.getString("message");

                            //Toast.makeText(LoginPage.this,""+responseCode,Toast.LENGTH_SHORT).show();

                            if(responseCode == 0) {
                                Toast.makeText(LoginPage.this, "" + message, Toast.LENGTH_SHORT).show();
                                etPassword.setText("");
                                return;
                            }

                            if(responseCode == 1){

                                String student_id = studentObj.getString("student_id");
                                String name   = studentObj.getString("name");
                                String semester = studentObj.getString("semester");
                                String college_name   = studentObj.getString("college_name");
                                String branch_name   = studentObj.getString("branch_name");
                                String class_name   = studentObj.getString("class_name");

                                String dob   = studentObj.getString("dob");
                                String contact_no   = studentObj.getString("contact_no");

                                String email   = studentObj.getString("email");
                                String password   = studentObj.getString("password");

                                String gender = studentObj.getString("gender");

                                if(gender.equals("0"))
                                    gender = "male";
                                else
                                    gender = "female";

                                Toast.makeText(LoginPage.this,
                                                ""+student_id
                                                +" "+name
                                                +" "+semester
                                                +" "+class_name
                                                +" "+college_name
                                                +" "+branch_name
                                                +" "+dob
                                                        +" "+contact_no
                                                +" "+email
                                                +" "+password
                                                        +" "+gender
                                        ,Toast.LENGTH_LONG).show();

                            }

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
                params.put("student_id",student_id);
                params.put("password",password);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds*1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(LoginPage.this).addToRequestQueue(stringRequest);

    }

    private void setUpAleartBox(){

        final Context context = LoginPage.this;
        final AlertDialog.Builder alertDialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(context,android.R.style.Theme_Holo_Dialog_MinWidth);
        } else {
            alertDialog = new AlertDialog.Builder(context);
        }

        final String email_id = "14kasera.mani@gmail.com";
        String message = "Registration option is not available due to security reasons. " +
                "Contact your Class-cordinator or email us at " + email_id+" with your credentials.";
        final String email_subject = "Register for StudentApp";
        final String email_text = "Mention your name, date-of-birth, college, branch, class and current semester" +
                " and attach a college id with this mail.\n\n";

        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("email us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent emailIntent =  new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto",email_id,null));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, email_text);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if(isIntentSafe)
                    startActivity(Intent.createChooser(emailIntent,"Send email via ..."));
                else
                    Toast.makeText(context,"Email can't be send from here",Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }



}
