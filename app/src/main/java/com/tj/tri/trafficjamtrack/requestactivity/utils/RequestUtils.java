package com.tj.tri.trafficjamtrack.requestactivity.utils;

import android.util.Log;

import com.tj.tri.trafficjamtrack.config.AppConfig;
import com.tj.tri.trafficjamtrack.datahelper.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tri on 2/11/15.
 */
public class RequestUtils {

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = Helper.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String PostDataToGetWifi(String url){
        InputStream inputStream = null;
        String result = "";
        Log.d(AppConfig.DEBUG_TAG, "url post: " + url);
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // make GET request to the given URL


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("type", AppConfig.WIFI_GET_TYPE_ALL));
            nameValuePairs.add(new BasicNameValuePair("API_KEY", AppConfig.API_KEY));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.d(AppConfig.DEBUG_TAG, "http data: " + httpPost.toString());
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = Helper.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String PostDataToCreateWifi(HashMap<String, String> data){
        InputStream inputStream = null;
        String result = "";
        Log.d(AppConfig.DEBUG_TAG, "data to post: " + data);
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppConfig.URL_CREATE_WIFI_DATA);
            // make GET request to the given URL


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("API_KEY", AppConfig.API_KEY));
            nameValuePairs.add(new BasicNameValuePair("longtitude", data.get("longtitude")));
            nameValuePairs.add(new BasicNameValuePair("latitude", data.get("latitude")));
            nameValuePairs.add(new BasicNameValuePair("wifiName", data.get("wifiName")));
            nameValuePairs.add(new BasicNameValuePair("bssid", data.get("bssid")));
            nameValuePairs.add(new BasicNameValuePair("wifiPass", data.get("wifiPassword")));
            nameValuePairs.add(new BasicNameValuePair("description", data.get("description")));
            nameValuePairs.add(new BasicNameValuePair("mac", data.get("mac")));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.d(AppConfig.DEBUG_TAG, "http data: " + httpPost.toString());
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = Helper.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String PostDataToUpdateWifi(HashMap<String, String> data){
        InputStream inputStream = null;
        String result = "";
        Log.d(AppConfig.DEBUG_TAG, "data to post: " + data);
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppConfig.URL_UPDATE_WIFI_DATA);
            // make GET request to the given URL


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("API_KEY", AppConfig.API_KEY));
            nameValuePairs.add(new BasicNameValuePair("bssid", data.get("bssid")));
            nameValuePairs.add(new BasicNameValuePair("wifiPass", data.get("wifiPassword")));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.d(AppConfig.DEBUG_TAG, "http data: " + httpPost.toString());
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = Helper.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
}
