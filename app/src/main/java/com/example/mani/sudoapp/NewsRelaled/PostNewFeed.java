package com.example.mani.sudoapp.NewsRelaled;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mani.sudoapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.mani.sudoapp.CommonVariablesAndFunctions.BASE_URL;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.handleVolleyError;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.maxNoOfTries;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.postingNewFeed;
import static com.example.mani.sudoapp.CommonVariablesAndFunctions.retrySeconds;

public class PostNewFeed extends AppCompatActivity {

    private ImageButton mImageView;
    private EditText mTitle,mDescription;
    private Button mSubmitbtn;
    private Bitmap  mBitmap = null;
    private static final int GALLARY_REQUEST = 1;

    LinearLayout mErrorLinearLayout;
    TextView mErrorTextView;

    ProgressBar mProgressBar;

    private static final  String uploadUrl = BASE_URL +  "upload_data_from_app_to_database.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_feed);

        mProgressBar = findViewById(R.id.progress_bar);
       // mErrorTextView = findViewById(R.id.tv_error_message);

        mErrorLinearLayout  = findViewById(R.id.ll_error_layout);
        mErrorTextView      = findViewById(R.id.tv_error_message);
        Button mRetry       = findViewById(R.id.btn_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorLinearLayout.setVisibility(View.GONE);
                RelativeLayout postNewFeedMainLayout = findViewById(R.id.post_new_fee_layout);
                postNewFeedMainLayout.setVisibility(View.VISIBLE);

            }
        });


        mImageView    = findViewById(R.id.post_new_feed_imageview);
        mTitle        = findViewById(R.id.post_new_feed_title);
        mDescription  = findViewById(R.id.post_new_feed_describtion);
        mSubmitbtn    = findViewById(R.id.post_new_feed_submit_btn);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent gallaryIntent = new Intent(Intent.ACTION_PICK);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent,GALLARY_REQUEST);
            }
        });

        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToDatabase();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLARY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            // Cropping using 3rd party library
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    mImageView.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(PostNewFeed.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }



   /*
        In this method, if title and description field are not empty the
        data will be send to database.
        And then updated information will be fetched and will be shown in News Feed.
        App will return to News Feed page.
     */

    private void sendDataToDatabase() {

        final String title, description;

        title = mTitle.getText().toString().trim();
        description = mDescription.getText().toString().trim();

        if (title.equals("") || description.equals("") || mBitmap == null) {
            Toast.makeText(PostNewFeed.this, "Required fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");

                            Toast.makeText(PostNewFeed.this, Response, Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                            postingNewFeed = true;
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error,mProgressBar,mErrorTextView,mErrorLinearLayout);

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("image", imageToString(mBitmap));
                params.put("title", mTitle.getText().toString().trim());
                params.put("description",mDescription.getText().toString().trim());
                return params;
            }
        };

        RelativeLayout postNewFeedMainLayout = findViewById(R.id.post_new_fee_layout);
        postNewFeedMainLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (retrySeconds*1000),maxNoOfTries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(PostNewFeed.this).addToRequestQueue(stringRequest);
    }


    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }

}
