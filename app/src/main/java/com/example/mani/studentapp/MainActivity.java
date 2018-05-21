package com.example.mani.studentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mani.studentapp.NewsRelaled.NewsFeed;

public class MainActivity extends AppCompatActivity {

    TextView toSkip,toLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        toSkip = findViewById(R.id.to_skip);
        toLogin = findViewById(R.id.to_login);

        toSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NewsFeed.class));
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Implementing...",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
    }
}
