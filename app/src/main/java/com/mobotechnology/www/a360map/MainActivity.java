package com.mobotechnology.www.a360map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private ArrayList<LatLng> latLngArrayList = new ArrayList<>();

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


        initLatLangArray();


        CameraPosition cameraPosition = null;
        for (int i = 0; i < latLngArrayList.size(); i++) {
            directZoomLabel();
            cameraPosition = animateCameraInitializing(latLngArrayList, i);
            addingMultipleMarker(map, latLngArrayList, i);
        }

        /** Animate the change in camera view over 3 seconds*/
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                3000, null);

        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    private void initLatLangArray() {
        latLngArrayList.add(new LatLng(27.674351, 85.314379));
        latLngArrayList.add(new LatLng(27.748050, 85.301468));
        latLngArrayList.add(new LatLng(27.694343, 85.321191));
        latLngArrayList.add(new LatLng(27.705630, 85.333299));
        latLngArrayList.add(new LatLng(27.728903, 85.331732));
    }

    @NonNull
    private CameraPosition animateCameraInitializing(ArrayList<LatLng> latLngArrayList, int i) {
        CameraPosition cameraPosition; /**
         * animate camera position initializing
         * */
        cameraPosition = CameraPosition.builder()
                .target(latLngArrayList.get(i)).zoom(13)
                .bearing(90) /** Rotate map*/
                .build();
        return cameraPosition;
    }

    private void addingMultipleMarker(GoogleMap map, ArrayList<LatLng> latLngArrayList, int i) {
        /** Adding multiple marker*/
        map.addMarker(new MarkerOptions()
                .position(latLngArrayList.get(i))
                .title("Marker at Lat/Long: "))
                .setSnippet(latLngArrayList.get(i).toString());
    }

    private void directZoomLabel() {
        /** Direct zoom to label 13*/
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngArrayList.get(i), 13));
    }

    /**
     * Window info click listener
     */
    @Override
    public void onInfoWindowClick(Marker marker) {

        Toast.makeText(this, "Clicked at: " + marker.getTitle(), Toast.LENGTH_SHORT).show();

    }


    /**
     * Marker Click listener
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, "Clicked at: " + marker.getTitle(), Toast.LENGTH_SHORT).show();

        return false;
    }
}
