package com.tj.tri.trafficjamtrack.customlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.tj.tri.trafficjamtrack.R;

/**
 * Created by tri on 2/9/15.
 */
public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater mInflater;

    public CustomWindowAdapter(LayoutInflater i){
        mInflater = i;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.custom_info_window, null);
        // Populate fields
        TextView title = (TextView) v.findViewById(R.id.tv_info_window_id);
        title.setText(marker.getTitle());

        TextView description = (TextView) v.findViewById(R.id.tv_info_window_password);
        description.setText(marker.getSnippet());
        // Return info window contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
