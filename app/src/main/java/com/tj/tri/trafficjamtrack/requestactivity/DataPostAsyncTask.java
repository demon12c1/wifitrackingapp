package com.tj.tri.trafficjamtrack.requestactivity;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tj.tri.trafficjamtrack.config.AppConfig;
import com.tj.tri.trafficjamtrack.datahelper.AsyncResponse;
import com.tj.tri.trafficjamtrack.datahelper.Helper;
import com.tj.tri.trafficjamtrack.requestactivity.utils.RequestUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tri on 2/10/15.
 */
public class DataPostAsyncTask extends AsyncTask<HashMap<String, String>, Void, String> {
    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        return RequestUtils.PostDataToCreateWifi(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(AppConfig.DEBUG_TAG,"result when create wifi: " + result);
        super.onPostExecute(result);

    }


}
