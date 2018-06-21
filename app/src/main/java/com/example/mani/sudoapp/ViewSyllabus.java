package com.example.mani.sudoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.mani.sudoapp.CommonVariablesAndFunctions.BASE_URL_SYLLABUS;

public class ViewSyllabus extends AppCompatActivity {

    PDFView mPdfView;
    String SYLLABUS_URL;
    LinearLayout progressBar;
    InputStream mInputStream = null;

    //final String filename = "MY_FILE.srl";
    //File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_syllabus);

        init();
        new RetrievePDFSteam().execute(SYLLABUS_URL);
        /*if(file!= null){
            Toast.makeText(ViewSyllabus.this,"File not empty",Toast.LENGTH_SHORT).show();
            //mPdfView.fromFile(file);
        }*/
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();

        String branch_short_name = bundle.getString("branch");
        int semester_id          = bundle.getInt("semester");

        //Toast.makeText(ViewSyllabus.this,semester_id+"",Toast.LENGTH_SHORT).show();

        SYLLABUS_URL = BASE_URL_SYLLABUS + branch_short_name + "/" + semester_id+".pdf";

        mPdfView    = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progress_bar);

        //file = new File(getCacheDir(),filename);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_syllabus,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.menu_syllabus_update:
                new RetrievePDFSteam().execute(SYLLABUS_URL);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class RetrievePDFSteam extends AsyncTask<String, Void,InputStream> {


        @Override
        protected void onPreExecute() {
            //Toast.makeText(ViewSyllabus.this,"PreExecute",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(String... strings) {

            try {

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    mInputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //copyInputStreamToFile();


            return mInputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            //Toast.makeText(ViewSyllabus.this,"PostExecute",Toast.LENGTH_SHORT).show();

            if(inputStream!=null)
                mPdfView.fromStream(inputStream).load();
            else
                Toast.makeText(ViewSyllabus.this,"Syllabus not available", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);


        }
    }

    /*private void copyInputStreamToFile() {


        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=mInputStream.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                //mInputStream.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }*/

    /*private void readFromFile(){

        file = new File(getCacheDir(),filename);
        InputStream in = null;

        try {
            in = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadPdf(in);

    }*/
}
