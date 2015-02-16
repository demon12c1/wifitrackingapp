package com.tj.tri.trafficjamtrack.requestactivity;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tj.tri.trafficjamtrack.config.AppConfig;
import com.tj.tri.trafficjamtrack.datahelper.AsyncResponse;
import com.tj.tri.trafficjamtrack.requestactivity.utils.RequestUtils;

/**
 * Created by tri on 2/11/15.
 */
public class DataGetAsyncTask extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate=null;
    @Override
    protected String doInBackground(String... urls) {
        Log.d(AppConfig.DEBUG_TAG, "urls to get: " + urls);
        return RequestUtils.PostDataToGetWifi(urls[0]);
    }
    // onPostExecute displays the results of the AsyncTask.-
    @Override
    protected void onPostExecute(String result) {
        Log.d(AppConfig.DEBUG_TAG, "Json result: " + result);
        delegate.processFinish(result);

    }
}
