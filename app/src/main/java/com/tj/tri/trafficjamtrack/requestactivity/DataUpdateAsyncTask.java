package com.tj.tri.trafficjamtrack.requestactivity;

import android.os.AsyncTask;
import android.util.Log;

import com.tj.tri.trafficjamtrack.config.AppConfig;
import com.tj.tri.trafficjamtrack.requestactivity.utils.RequestUtils;

import java.util.HashMap;

/**
 * Created by tri on 3/3/15.
 */
public class DataUpdateAsyncTask extends AsyncTask<HashMap<String, String>, Void, String> {
    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        return RequestUtils.PostDataToUpdateWifi(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(AppConfig.DEBUG_TAG, "result when update wifi: " + result);
        super.onPostExecute(result);
    }
}
