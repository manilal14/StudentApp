package com.example.mani.studentapp.NewsRelaled;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mani.studentapp.AttendanceRelated.AttendanceHomePage;
import com.example.mani.studentapp.R;
import com.example.mani.studentapp.TimeTableRelated.TimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFeed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Feeds> mFeedsList;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FeedsAdapter mFeedAdapter;

    TextView mErrorTextView;

    public static final String BASE_URL = "http://192.168.43.154/studentApp/";
    private static final String FETCHING_URL = BASE_URL + "fetch_from_database_to_app.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        // Error handling Views
        mErrorTextView      = findViewById(R.id.tv_error_message);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mFeedsList =  new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(NewsFeed.this,PostNewFeed.class));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // For Recycler View
        recyclerView = findViewById(R.id.news_feed_recycylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFeedsFromDatabase();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadFeedsFromDatabase();

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            ///NewsFeed.super.onBackPressed();
                            //android.os.Process.killProcess(android.os.Process.myPid());
                            finish();
                        }
                    }).create().show();
        }
    }

    @Override
    protected void onResume() {

        loadFeedsFromDatabase();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_refresh) {
            mSwipeRefreshLayout.setRefreshing(true);
            loadFeedsFromDatabase();
        }

        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_timetable) {
            startActivity(new Intent(NewsFeed.this,TimeTable.class));

        } else if (id == R.id.nav_attendence) {
            startActivity(new Intent(NewsFeed.this, AttendanceHomePage.class));

        } else if (id == R.id.nav_chatroom) {

        } else if (id == R.id.nav_certificate) {

        } else if (id == R.id.nav_syllabus) {

        } else if (id == R.id.nav_about) {



        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFeedsFromDatabase()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        mErrorTextView.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, FETCHING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // First clear the adaptar if something is there
                        // useful on refreshing
                        if(mFeedAdapter != null) {
                            mFeedAdapter.clear();
                            mFeedAdapter.addAll(mFeedsList);
                        }

                        try {

                            JSONArray products = new JSONArray(response);

                            for(int i=0;i<products.length();i++) {

                                JSONObject productObject = products.getJSONObject(i);

                                int id           = productObject.getInt("id");
                                String title     = productObject.getString("title");
                                String description = productObject.getString("description");
                                int imageInt     = productObject.getInt("image_path");
                                String image = Integer.toString(imageInt) + ".jpeg";

                                image = BASE_URL + "uploaded_image/" + image;

                                Feeds feeds = new Feeds(id,title,description,image);
                                mFeedsList.add(feeds);
                            }

                            mSwipeRefreshLayout.setRefreshing(false);

                            mFeedAdapter = new FeedsAdapter(NewsFeed.this,mFeedsList);
                            recyclerView.setAdapter(mFeedAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (10*1000),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public boolean amIConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void handleVolleyError(VolleyError error){

        mSwipeRefreshLayout.setRefreshing(false);
        mErrorTextView.setVisibility(View.VISIBLE);

        if(error instanceof TimeoutError){
            mErrorTextView.setText(R.string.connection_error);
        }
        else if (error instanceof NoConnectionError){
            mErrorTextView.setText(R.string.no_connection);
        }
        else if (error instanceof AuthFailureError){
            mErrorTextView.setText(R.string.auth_failure_error);
        }
        else if (error instanceof ServerError){
            mErrorTextView.setText(R.string.server_error);
        }
        else if (error instanceof NetworkError){
            mErrorTextView.setText(R.string.network_error);
        }
        else if(error instanceof ParseError){
            mErrorTextView.setText(R.string.parse_error);
        }
        else {
            mErrorTextView.setText(R.string.volley_error);
        }

    }

}


