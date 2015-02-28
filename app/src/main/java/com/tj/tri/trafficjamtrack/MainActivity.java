package com.tj.tri.trafficjamtrack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmapsextensions.Circle;
import com.androidmapsextensions.CircleOptions;
import com.androidmapsextensions.GoogleMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.androidmapsextensions.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.tj.tri.trafficjamtrack.config.AppConfig;
import com.tj.tri.trafficjamtrack.customlayout.CustomWindowAdapter;
import com.tj.tri.trafficjamtrack.datahelper.AsyncResponse;
import com.tj.tri.trafficjamtrack.datahelper.Helper;
import com.tj.tri.trafficjamtrack.requestactivity.DataPostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity implements GoogleMap.OnMapLongClickListener, LocationListener, GoogleMap.OnMyLocationChangeListener {
    private GoogleMap mMap;
    private LatLng mCurrentPosition;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Init map info
        Log.d(AppConfig.DEBUG_TAG,"Set up map");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 0, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        setUpMap();
        setUpMapIfNeeded();

        //Get the data from update activity and display on map
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String jsonData = extras.getString("JSON_WIFI_DATA");
            this.prepareDataToRender(jsonData);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapLongClick(LatLng point) {

//        // Get current location
//        if(checkWifiIsSetAtSamePosition()){
            LatLng latLng = this.mCurrentPosition;
            // Display the alert dialog and set up point if create successfully at current location
            showAlertDialogForPoint(latLng);
//        }

    }


    // Display the alert that adds the marker
    private void showAlertDialogForPoint(final LatLng point) {
        // inflate message_item.xml view
        View messageView = LayoutInflater.from(MainActivity.this).
                inflate(R.layout.message_item, null);
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // get bssid of the wifi (the wifi name)
        String bssid = MainActivity.this.getCurrentSsid(MainActivity.this);
        Log.d(AppConfig.DEBUG_TAG,"BSSID: " + bssid);
        // Extract content from alert dialog
        TextView wifiBssid = ((TextView) messageView.findViewById(R.id.etTitle));
        wifiBssid.setText(bssid);

        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean ipChecked = false;
                        // Define color of marker icon
                        BitmapDescriptor defaultMarker =
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

                        String wifiName = ((TextView) alertDialog.findViewById(R.id.etTitle)).getText().toString();
                        String wifiPassword= ((EditText) alertDialog.findViewById(R.id.etSnippet)).
                                getText().toString();
                        String description = ((EditText) alertDialog.findViewById(R.id.etDes)).
                                getText().toString();

                        // Creates and adds marker to the map


                        DataPostAsyncTask dataPostAsyncTask = new DataPostAsyncTask();
                        if(!wifiName.isEmpty()){
                            // transport the data to PostDataToCreateWifi in requestutil
                            HashMap<String, String> wifiInfo = new HashMap<String, String>();
                            wifiInfo.put("longtitude", String.valueOf(point.longitude));
                            wifiInfo.put("latitude", String.valueOf(point.latitude));
                            wifiInfo.put("wifiName", wifiName);
                            wifiInfo.put("wifiPassword", wifiPassword);
                            wifiInfo.put("description", description);
                            wifiInfo.put("bssid", wifiName);
                            try {
                                String output = dataPostAsyncTask.execute((HashMap) wifiInfo).get();
                                ipChecked = checkingWifiAlreadySet(output);
                            }catch (InterruptedException | ExecutionException e){
                                Log.e(AppConfig.DEBUG_TAG,"" + e);
                            }

                        }
                        if(ipChecked){
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title(wifiName)
                                    .snippet(wifiPassword)
                                    .icon(defaultMarker));
                            Toast.makeText(getApplicationContext(), "This wifi is shared by you, thanks for sharing!!!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "This wifi has already shared!!!", Toast.LENGTH_LONG).show();

                        }

                    }


                    private boolean checkingWifiAlreadySet(String jsonData){
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            String status = jsonObject.getString("result");

                            if (status.equals(AppConfig.SUCCESS)){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }catch (JSONException e){
                            Log.e(AppConfig.DEBUG_TAG, "unexpected JSON exception", e);
                            return false;
                        }

                    }
                });

        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        // Display the dialog
        alertDialog.show();
    }

    private void setUpMapIfNeeded() {
            // Check if we were successful in obtaining the map.
            if (this.mMap != null) {
                this.setUpLocationChange();
            }

    }



    private void setUpLocationChange(){
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        mMap.setMyLocationEnabled(true);
        // Get LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        String bestProvider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        Location location = locationManager.getLastKnownLocation(bestProvider);
        // Set up current location to focus


        if (location != null) {
            onMyLocationChange(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        // todo: need to improve this function (brief the functions)
        Log.d(AppConfig.DEBUG_TAG,"Current location: " + location);

    }

    @Override
    public void onMyLocationChange(Location location) {
        this.focusToCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void addWifiMarker(Double lng, Double lat, String wifiName, String wifiPass, String description){
        if (this.mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(wifiName).snippet(wifiPass));
        }
    }

    private void setUpMap(){
        // Do a null check to confirm that we have not already instantiated the map.
        if (this.mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            this.mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getExtendedMap();
            this.mMap.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
            this.mMap.setOnMapLongClickListener(this);
            this.mMap.setOnMyLocationChangeListener(this);
        }
    }



    private void prepareDataToRender(String jsonData){

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            String status = jsonObject.getString("result");

            if (status.equals(AppConfig.SUCCESS)) {
                JSONObject wifiData = jsonObject.getJSONObject("data");
                JSONArray wifiObjects = wifiData.getJSONArray("wifi_info");

                boolean result = Helper.checkIsJsonArrayOrNot(wifiObjects.toString());
                Log.d(AppConfig.DEBUG_TAG, "Check the data recieved is belong to json array: " + result);
                if (result) {

                    for (int i = 0; i < wifiObjects.length(); i++) {
                        String lng = wifiObjects.getJSONObject(i).getString("longtitude");
                        String lat = wifiObjects.getJSONObject(i).getString("latitude");
                        String wifiName = wifiObjects.getJSONObject(i).getString("wifiName");
                        String wifiPass = wifiObjects.getJSONObject(i).getString("wifiPass");
                        String description = wifiObjects.getJSONObject(i).getString("description");
                        Log.d(AppConfig.DEBUG_TAG, "Long: " + lng + "Lat: " + lat);
                        if (lng != null && lat != null) {
                            addWifiMarker(Double.parseDouble(lng), Double.parseDouble(lat), wifiName, wifiPass, description);
                        }
                    }
                }
            }
        }catch (JSONException e){
            Log.e(AppConfig.DEBUG_TAG, "unexpected JSON exception", e);
        }
    }

//    private boolean checkWifiIsSetAtSamePosition(){
//        //todo: this need to be solved
//        boolean check = true;
//        Circle circle = mMap.addCircle(new CircleOptions().center(this.mCurrentPosition).radius(0).strokeColor(Color.RED)
//                .fillColor(Color.BLUE));
//        List<Marker> listMarker = mMap.getDisplayedMarkers();
//        for (Marker marker : listMarker){
//            check = circle.contains(marker.getPosition());
//            if(check){
//                Toast.makeText(getApplicationContext(), "Please stand away from current point at least 5 meters", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        }
//       return true;
//    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    private void focusToCurrentLocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        this.mCurrentPosition = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }


}
