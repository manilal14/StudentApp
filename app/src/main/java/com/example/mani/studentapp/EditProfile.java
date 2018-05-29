package com.example.mani.studentapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static android.accounts.AccountManager.KEY_PASSWORD;
import static com.example.mani.studentapp.LoginSessionManager.KEY_BRANCH;
import static com.example.mani.studentapp.LoginSessionManager.KEY_CLASS;
import static com.example.mani.studentapp.LoginSessionManager.KEY_COLLEGE;
import static com.example.mani.studentapp.LoginSessionManager.KEY_CONTACT;
import static com.example.mani.studentapp.LoginSessionManager.KEY_DOB;
import static com.example.mani.studentapp.LoginSessionManager.KEY_EMAIL;
import static com.example.mani.studentapp.LoginSessionManager.KEY_GENDER;
import static com.example.mani.studentapp.LoginSessionManager.KEY_NAME;
import static com.example.mani.studentapp.LoginSessionManager.KEY_SEMESTER;

public class EditProfile extends AppCompatActivity {


    LoginSessionManager mLoginSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mLoginSession = new LoginSessionManager(getApplicationContext());


        setEditProfile();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_save){
            getFromEditProfile();
        }

        return super.onOptionsItemSelected(item);
    }


    private void setEditProfile(){

        EditText et_name, et_college,et_branch,et_sem, et_class;
        EditText et_password, et_dob, et_mobile, et_email;
        RadioButton rb_male, rb_female;

        et_name    = findViewById(R.id.edit_profile_name);
        et_college = findViewById(R.id.edit_profile_college);
        et_branch  = findViewById(R.id.edit_profile_branch);
        et_sem     = findViewById(R.id.edit_profile_sem);
        et_class   = findViewById(R.id.edit_profile_class);

        et_password = findViewById(R.id.edit_profile_password);
        et_dob      = findViewById(R.id.edit_profile_dob);
        et_mobile   = findViewById(R.id.edit_profile_mobile);
        et_email    = findViewById(R.id.edit_profile_email);

        rb_male     = findViewById(R.id.radio_button_male);
        rb_female   = findViewById(R.id.radio_button_female);


        HashMap<String, String> student = mLoginSession.getStudentDetailsFromSharedPreference();

        et_name.setText(student.get(KEY_NAME));
        et_college.setText(student.get(KEY_COLLEGE));
        et_branch.setText(student.get(KEY_BRANCH));
        et_sem.setText(student.get(KEY_SEMESTER));
        et_class.setText(student.get(KEY_CLASS));

        et_password.setText("12345678910");
        et_dob.setText(student.get(KEY_DOB));
        et_mobile.setText(student.get(KEY_CONTACT));
        et_email.setText(student.get(KEY_EMAIL));

        String gender = student.get(KEY_GENDER);

        if(gender.equals("0"))
            rb_male.setChecked(true);
        else
            rb_female.setChecked(true);
    }

    private void  getFromEditProfile(){

        EditText et_dob, et_mobile, et_email;
        RadioGroup rg_gender;


        et_dob      = findViewById(R.id.edit_profile_dob);
        et_mobile   = findViewById(R.id.edit_profile_mobile);
        et_email    = findViewById(R.id.edit_profile_email);

        rg_gender = findViewById(R.id.radio_group_gender);

        /*et_password.setClickable(true);
        et_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });*/



        //get gender
        int id_gender = rg_gender.getCheckedRadioButtonId();
        RadioButton rb_gender = findViewById(id_gender);
        String gender = rb_gender.getText().toString();

        // 0 = male , 1 = female
        int gender_int = 1;
        if(gender.equals("male"))
            gender_int = 0;



        Toast.makeText(EditProfile.this,"Gender "+gender_int,
                Toast.LENGTH_SHORT).show();
    }


    public void resetPassword(View view) {

        Context context = EditProfile.this;
        final AlertDialog.Builder alertDialog;

        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.reset_password_layout,null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(context,android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
        } else {
            alertDialog = new AlertDialog.Builder(context);
        }

        alertDialog.setView(v);

        final EditText et_current_pass,et_new_pass, et_confirm_pass;
        final TextView current_pass_error,new_pass_error,confirm_pass_error;
        TextView done,cancel;


        et_current_pass = v.findViewById(R.id.current_password);
        et_new_pass     = v.findViewById(R.id.new_password);
        et_confirm_pass = v.findViewById(R.id.confirm_password);

        current_pass_error = v.findViewById(R.id.current_password_error);
        new_pass_error     = v.findViewById(R.id.new_password_error);
        confirm_pass_error = v.findViewById(R.id.confirm_password_error);



        done   = v.findViewById(R.id.done);
        cancel = v.findViewById(R.id.cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentPass,newPass,confirmPass;

                currentPass = et_current_pass.getText().toString().trim();
                newPass     = et_new_pass.getText().toString().trim();
                confirmPass = et_confirm_pass.getText().toString().trim();

                //if old password is wrong
                if(!currentPass.equals(mLoginSession.getStudentDetailsFromSharedPreference()
                        .get(KEY_PASSWORD))){
                    current_pass_error.setVisibility(View.VISIBLE);
                    return;
                }

                //if new password is shorter than six character
                if(newPass.length()<6){
                    new_pass_error.setVisibility(View.VISIBLE);
                    return;
                }

                if(!newPass.equals(confirmPass)){
                    confirm_pass_error.setVisibility(View.VISIBLE);
                    return;
                }

            }

        });



        alertDialog.show();

    }
}
