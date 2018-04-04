package com.example.mani.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TimeTable extends AppCompatActivity {

    TextView mon1,mon2,mon3,mon4,mon5,mon6;
    TextView tue1,tue2,tue3,tue4,tue5,tue6;
    TextView wed1,wed2,wed3,wed4,wed5,wed6;
    TextView thr1,thr2,thr3,thr4,thr5,thr6;
    TextView fri1,fri2,fri3,fri4,fri5,fri6;

    public static final String MY_PREFERENCES = "MyPreferences" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        mon1 = findViewById(R.id.mon1);
        mon2 = findViewById(R.id.mon2);
        mon3 = findViewById(R.id.mon3);
        mon4 = findViewById(R.id.mon4);
        mon5 = findViewById(R.id.mon5);
        mon6 = findViewById(R.id.mon6);


        setTimeTable();




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

        SharedPreferences sharedPreferences = getSharedPreferences(TimeTable.MY_PREFERENCES,MODE_PRIVATE);

        String m1 = sharedPreferences.getString("mp1key","1");
        String m2 = sharedPreferences.getString("mp2key","2");
        String m3 = sharedPreferences.getString("mp3key","3");
        String m4 = sharedPreferences.getString("mp4key","4");
        String m5 = sharedPreferences.getString("mp5key","5");
        String m6 = sharedPreferences.getString("mp6key","6");

        String t1 = sharedPreferences.getString("t1key","7");
        String t2 = sharedPreferences.getString("t2key","8");
        String t3 = sharedPreferences.getString("t3key","9");
        String t4 = sharedPreferences.getString("t4key","10");
        String t5 = sharedPreferences.getString("t5key","11");
        String t6 = sharedPreferences.getString("t6key","12");

        String w1 = sharedPreferences.getString("w1key","13");
        String w2 = sharedPreferences.getString("w2key","14");
        String w3 = sharedPreferences.getString("w3key","15");
        String w4 = sharedPreferences.getString("w4key","16");
        String w5 = sharedPreferences.getString("w5key","17");
        String w6 = sharedPreferences.getString("w6key","18");

        String th1 = sharedPreferences.getString("th1key","19");
        String th2 = sharedPreferences.getString("th2key","20");
        String th3 = sharedPreferences.getString("th3key","21");
        String th4 = sharedPreferences.getString("th4key","22");
        String th5 = sharedPreferences.getString("th5key","23");
        String th6 = sharedPreferences.getString("th6key","24");

        String f1 = sharedPreferences.getString("f1key","25");
        String f2 = sharedPreferences.getString("f2key","26");
        String f3 = sharedPreferences.getString("f3key","27");
        String f4 = sharedPreferences.getString("f4key","28");
        String f5 = sharedPreferences.getString("f5key","29");
        String f6 = sharedPreferences.getString("f6key","30");


        mon1.setText(m1);
        mon2.setText(m2);
        mon3.setText(m3);
        mon4.setText(m4);
        mon5.setText(m5);
        mon6.setText(m6);

        tue1.setText(t1);
        tue2.setText(t2);
        tue3.setText(t3);
        tue4.setText(t4);
        tue5.setText(t5);
        tue6.setText(t6);

        wed1.setText(w1);
        wed2.setText(w2);
        wed3.setText(w3);
        wed4.setText(w4);
        wed5.setText(w5);
        wed6.setText(w6);

        thr1.setText(th1);
        thr2.setText(th2);
        thr3.setText(th3);
        thr4.setText(th4);
        thr5.setText(th5);
        thr6.setText(th6);

        fri1.setText(f1);
        fri2.setText(f2);
        fri3.setText(f3);
        fri4.setText(f4);
        fri5.setText(f5);
        fri6.setText(f6);

    }

}
