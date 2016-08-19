package com.mobotechnology.www.a360map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap map) {


        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        latLngArrayList.add(new LatLng(27.674351, 85.314379));
        latLngArrayList.add(new LatLng(27.748050, 85.301468));
        latLngArrayList.add(new LatLng(27.694343, 85.321191));
        latLngArrayList.add(new LatLng(27.705630, 85.333299));
        latLngArrayList.add(new LatLng(27.728903, 85.331732));

        CameraPosition cameraPosition = null;
        for (int i = 0; i < latLngArrayList.size(); i++) {

            /** Direct zoom to label 13*/
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngArrayList.get(i), 13));


            /**
             * Camera position initializing
             * */
            cameraPosition = CameraPosition.builder()
                    .target(latLngArrayList.get(i)).zoom(13)
                    .bearing(90) /** Rotate map*/
                    .build();

            /** Adding multiple marker*/
            map.addMarker(new MarkerOptions()
                    .position(latLngArrayList.get(i))
                    .title("Marker"));
        }


        /** Animate the change in camera view over 3 seconds*/
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                3000, null);

    }

}
