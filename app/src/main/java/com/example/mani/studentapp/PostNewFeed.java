package com.example.mani.studentapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostNewFeed extends AppCompatActivity {

    private ImageButton mImageView;
    private EditText mTitle,mDescription;
    private Button mSubmitbtn;
    private Bitmap bitmap = null;
    private static final int GALLARY_REQUEST = 1;

    private static final  String uploadUrl ="http://192.168.0.106/imageUploadApp/updateinfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_feed);

        mImageView = findViewById(R.id.post_new_feed_imageview);
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
        if(requestCode == GALLARY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri imageUri = data.getData();
            // Cropping using 3rd party library
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    mImageView.setImageBitmap(bitmap);
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

    /*
        In this method, if title and description field are not empty the
        data will be send to database.
        And then updated information will be fetched and will be shown in News Feed.
     */

    private void sendDataToDatabase()
    {
        //Todo 1. send data to database  done
        //Todo 2. Fetch from database

        final String title,description;

        title = mTitle.getText().toString().trim();
        description = mDescription.getText().toString().trim();

        if(title.equals("") || description.equals(""))
        {
            Toast.makeText(PostNewFeed.this,"Required fields are empty",Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("resposne", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(PostNewFeed.this,Response,Toast.LENGTH_LONG).show();

                            mTitle.setText("");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostNewFeed.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> params = new HashMap<>();
                params.put("title",title);
                params.put("description",description);
                params.put("image",imageToString(bitmap));
                return params;
            }
        };

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
