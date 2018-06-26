package com.example.mani.sudoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mani.sudoapp.NewsRelaled.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.mani.sudoapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;

public class AdminSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_singnup);

        Button adminSingnupBtn = findViewById(R.id.admin_signup_btn);
        adminSingnupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminSignup();
            }
        });
    }

    private void adminSignup(){

        final String ADMIN_SIGNUPURL = BASE_URL_ATTENDANCE + "admins_signup.php";
        EditText et_name, et_email, et_pass,et_con_pass;

        et_name     = findViewById(R.id.admin_signup_name);
        et_email    = findViewById(R.id.admin_signup_email);
        et_pass     = findViewById(R.id.admin_signup_password);
        et_con_pass = findViewById(R.id.admin_signup_confirm_password);

        String name     = et_name.getText().toString().trim();
        String email    = et_email.getText().toString().trim();
        String pass     = et_pass.getText().toString().trim();
        String con_pass = et_con_pass.getText().toString().trim();

        if(name.equals("") || email.equals("") || pass.equals("") || con_pass.equals("")){
            Toast.makeText(AdminSignup.this,"All fields are required",Toast.LENGTH_SHORT).show();
            name     = "";
            email    = "";
            pass     = "";
            con_pass = "";
            return;
        }

        if(!pass.equals(con_pass)){
            Toast.makeText(AdminSignup.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
            pass     = "";
            con_pass = "";
            return;

        }

        final String finalName = name;
        final String finalPass = pass;
        final String finalEmail = email;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADMIN_SIGNUPURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            int code       = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");

                            Toast.makeText(AdminSignup.this,message,Toast.LENGTH_SHORT).show();
                            finish();

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

                params.put("name", finalName);
                params.put("email", finalEmail);
                params.put("password", finalPass);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (10*1000),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AdminSignup.this).addToRequestQueue(stringRequest);

    }
}
