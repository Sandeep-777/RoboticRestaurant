package com.example.sandeep.parsingtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sandeep.parsingtest.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity{

    private final  String URL = "http://data.d-codepages.com/nexus.json";
    private final OkHttpClient client = new OkHttpClient();
    private TextView jsonview, device, os, chipset;
    private TableLayout table;
    private ProgressBar progress;
    private ImageView device_image;
    private String json=null;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table= (TableLayout) findViewById(R.id.table);
        jsonview = (TextView) findViewById(R.id.jsonview);
        layout = (LinearLayout) findViewById(R.id.container);
        device = (TextView) findViewById(R.id.device);
        os = (TextView) findViewById(R.id.os);
        device_image = (ImageView) findViewById(R.id.device_image);
        chipset = (TextView) findViewById(R.id.chipset);
        jsonview.setMovementMethod(new ScrollingMovementMethod());
        progress = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void set_json(String s) {
        progress.setVisibility(View.GONE);
        jsonview.setText(s);
        json = s;
    }
    private void  parse_json() {
        if (json !=null){
            JSONObject object=null;
            String ImageUrl = null;
            try {
                object = new JSONObject(json);
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"JSON OBJECT EXCEPTION"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
            try {
                if(object!=null) {
                    device.setText( object.getString("Device"));
                    os.setText(object.getString("OS"));
                    chipset.setText(object.getString("Chipset"));
                    ImageUrl = object.getString("image");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Picasso.with(MainActivity.this).load(ImageUrl).into(device_image);
            layout.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.get_json) {
            BackgroundTask task = new BackgroundTask();
            task.execute();
            return true;
        } else if (id == R.id.parse) {

            parse_json();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class BackgroundTask extends AsyncTask<String,String,String>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                return  response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String data) {
            if(data != null)
            {
                set_json(data);
            }
        }
    }


}
