package com.tj.tri.trafficjamtrack;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.tj.tri.trafficjamtrack.config.AppConfig;
import com.tj.tri.trafficjamtrack.datahelper.AsyncResponse;
import com.tj.tri.trafficjamtrack.datahelper.Helper;
import com.tj.tri.trafficjamtrack.requestactivity.DataGetAsyncTask;
import com.tj.tri.trafficjamtrack.requestactivity.DataPostAsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tri on 2/9/15.
 */
public class UpdateActivity extends ActionBarActivity implements AsyncResponse {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_data);

        if(isConnected()){
            Log.d(AppConfig.DEBUG_TAG, "Connected");
        }
        else{
            Log.d(AppConfig.DEBUG_TAG, "NOT Connected");
        }
        Log.d(AppConfig.DEBUG_TAG, "run app");
        // call AsynTask to perform network operation on separate thread
        DataGetAsyncTask httpAsyncTask = new DataGetAsyncTask();
        httpAsyncTask.execute(AppConfig.URL_GET_WIFI_DATA_ALL);
        Log.d(AppConfig.DEBUG_TAG, "updating ...");
        httpAsyncTask.delegate = this;
    }

    @Override
    public void processFinish(String output) {
        Log.d(AppConfig.DEBUG_TAG, "Json data: " + output);
        finish();
        Intent mapPage = new Intent(UpdateActivity.this, MainActivity.class);
            mapPage.putExtra("JSON_WIFI_DATA", output);
            startActivity(mapPage);
            Log.d(AppConfig.DEBUG_TAG, "redirect to main activity");
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


}
