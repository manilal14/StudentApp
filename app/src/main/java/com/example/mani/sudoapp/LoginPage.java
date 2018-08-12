package com.example.mani.sudoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mani.sudoapp.NewsRelaled.MySingleton;
import com.example.mani.sudoapp.NewsRelaled.NewsFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mani.sudoapp.CommonVariablesAndFunctions.BASE_URL_ATTENDANCE;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.handleVolleyError;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.retrySeconds;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.skipedLogin;

public class LoginPage extends AppCompatActivity {

    TextView btn_login,btn_register;
    TextView skip;

    boolean doubleBackToExitPressedOnce = false;

    FrameLayout mMainLoginLayout;
    LinearLayout mErrorLinearLayout;
    TextView mErrorTextView;
    Button mRetry;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // When loginPage will open it will be always false
        skipedLogin = false;

        // Error handling
        mMainLoginLayout   = findViewById(R.id.main_login_layout);
        mProgressBar       = findViewById(R.id.progress_bar);
        mErrorLinearLayout = findViewById(R.id.ll_error_layout);
        mErrorTextView     = findViewById(R.id.tv_error_message);
        mRetry             = findViewById(R.id.btn_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainLoginLayout.setVisibility(View.VISIBLE);
                mErrorLinearLayout.setVisibility(View.GONE);
            }
        });

        getSupportActionBar().hide();

        btn_login    = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        skip         = findViewById(R.id.tv_skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipedLogin = true;
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
                loginToSudoApp();
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Snackbar.make(LoginPage.this, "Replace with your own action", Snackbar.LENGTH_LONG).show();

        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void loginToSudoApp() {

        final EditText et_email, et_password;
        final String email;
        final String password;

        final String URL_LOGIN = BASE_URL_ATTENDANCE + "sudo_login.php";

        et_email    = findViewById(R.id.et_login_userid);
        et_password = findViewById(R.id.et_login_password);

        email    = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if(email.equals("") || password.equals("")){
            Toast.makeText(LoginPage.this, "Enter user_id and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mMainLoginLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mProgressBar.setVisibility(View.GONE);

                        try {

                            JSONArray jsonArray   = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            int responseCode = jsonObject.getInt("response_code");
                            String message   = jsonObject.getString("message");

                            //Toast.makeText(LoginPage.this,""+responseCode,Toast.LENGTH_SHORT).show();

                            if(responseCode == 0) {

                                mErrorLinearLayout.setVisibility(View.VISIBLE);
                                mErrorTextView.setText("UserId or password is incorrect");
                                //Toast.makeText(LoginPage.this, "" + message, Toast.LENGTH_SHORT).show();
                                et_password.setText("");

                                return;
                            }

                            if(responseCode == 1){

                                String sudo_id   = jsonObject.getString("sudo_id");
                                String sudouser  = jsonObject.getString("sudouser");
                                String sudoemail = jsonObject.getString("email");



                                // Need to save all these in sharedPreference

                                LoginSessionManager session = new LoginSessionManager(LoginPage.this);

                                session.createLoginSession(sudo_id,sudouser,sudoemail);

                                // I think there is no need for this as it will be automatically disable
                                // as soon as new activity will start
                                mProgressBar.setVisibility(View.GONE);

                                startActivity(new Intent(LoginPage.this,NewsFeed.class));
                                Toast.makeText(LoginPage.this,"Welcome "+sudouser,Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                mErrorLinearLayout.setVisibility(View.VISIBLE);
                                mErrorTextView.setText("Something went wrong");
                            }

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
                params.put("email",email);
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
