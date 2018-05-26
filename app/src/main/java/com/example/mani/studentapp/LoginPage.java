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

import com.example.mani.studentapp.NewsRelaled.NewsFeed;

import java.util.List;

public class LoginPage extends AppCompatActivity {

    TextView btn_login,btn_register;
    TextView skip;
    EditText mLoginEt,mPassEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mLoginEt = findViewById(R.id.et_login_userid);
        mPassEt  = findViewById(R.id.et_login_password);

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

        Toast.makeText(LoginPage.this,"Need to implement",Toast.LENGTH_SHORT).show();
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
