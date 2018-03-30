package com.example.mani.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

       




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_edit_time_table) {
           startActivity(new Intent(TimeTable.this,EditTimeTable.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTimeTable()
    {
        TextView mon1 = findViewById(R.id.mon1);
        TextView mon2 = findViewById(R.id.mon2);
        TextView mon3 = findViewById(R.id.mon3);
        TextView mon4 = findViewById(R.id.mon4);
        TextView mon5 = findViewById(R.id.mon5);
        TextView mon6 = findViewById(R.id.mon6);

        TextView tue1 = findViewById(R.id.tue1);
        TextView tue2 = findViewById(R.id.tue2);
        TextView tue3 = findViewById(R.id.tue3);
        TextView tue4 = findViewById(R.id.tue4);
        TextView tue5 = findViewById(R.id.tue5);
        TextView tue6 = findViewById(R.id.tue6);

        TextView wed1 = findViewById(R.id.wed1);
        TextView wed2 = findViewById(R.id.wed2);
        TextView wed3 = findViewById(R.id.wed3);
        TextView wed4 = findViewById(R.id.wed4);
        TextView wed5 = findViewById(R.id.wed5);
        TextView wed6 = findViewById(R.id.wed6);

        TextView thr1 = findViewById(R.id.thr1);
        TextView thr2 = findViewById(R.id.thr2);
        TextView thr3 = findViewById(R.id.thr3);
        TextView thr4 = findViewById(R.id.thr4);
        TextView thr5 = findViewById(R.id.thr5);
        TextView thr6 = findViewById(R.id.thr6);

        TextView fri1 = findViewById(R.id.fri1);
        TextView fri2 = findViewById(R.id.fri2);
        TextView fri3 = findViewById(R.id.fri3);
        TextView fri4 = findViewById(R.id.fri4);
        TextView fri5 = findViewById(R.id.fri5);
        TextView fri6 = findViewById(R.id.fri6);
    }

}
