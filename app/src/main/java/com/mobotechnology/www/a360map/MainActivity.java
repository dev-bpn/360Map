package com.mobotechnology.www.a360map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    private BottomSheetBehavior behavior;
    private TextView bottomSheetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViews();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * views initialization
     * */
    private void initViews() {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        View view = coordinatorLayout.findViewById(R.id.design_bottom_sheet);
        bottomSheetTextView = (TextView) coordinatorLayout.findViewById(R.id.bottomSheetTextView);

        bottomSheetTasks(view);

    }

    /**
     * BottomSheet task
     * */
    private void bottomSheetTasks(View view) {

        behavior = BottomSheetBehavior.from(view);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                Log.v(TAG , "onStateChanged: " + bottomSheet + " " + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
    }


    /**
     * Toolbar Initialization
     * */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    /**
     * Override method called when map is ready
     * */
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


    /**
     * Initializing LatLang Array
     * */
    private void initLatLangArray() {
        latLngArrayList.add(new LatLng(27.674351, 85.314379));
        latLngArrayList.add(new LatLng(27.748050, 85.301468));
        latLngArrayList.add(new LatLng(27.694343, 85.321191));
        latLngArrayList.add(new LatLng(27.705630, 85.333299));
        latLngArrayList.add(new LatLng(27.728903, 85.331732));
    }


    /**
     * Animate Camera Initialization
     * */
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



    /**
     *  Adding multiple marker
     *  */
    private void addingMultipleMarker(GoogleMap map, ArrayList<LatLng> latLngArrayList, int i) {
        map.addMarker(new MarkerOptions()
                .position(latLngArrayList.get(i))
                .title("Marker at Lat/Long: "))
                .setSnippet(latLngArrayList.get(i).toString());
    }


    /**
     *  Direct zoom to label 13
     *  */
    private void directZoomLabel() {
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
        String description = marker.getTitle() + "\n" + marker.getSnippet();
        bottomSheetTextView.setText(description);
        Log.v(TAG , "Behaviour gerState: " + behavior.getState());

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED );

        switch (behavior.getState()){
            case BottomSheetBehavior.STATE_COLLAPSED:
                Log.v(TAG , "Behaviour STATE_COLLAPSED: " + behavior.getState());
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                Log.v(TAG , "Behaviour STATE_DRAGGING: " + behavior.getState());
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                Log.v(TAG , "Behaviour STATE_EXPANDED: " + behavior.getState());
                break;
            case BottomSheetBehavior.STATE_HIDDEN:
                Log.v(TAG , "Behaviour STATE_HIDDEN: " + behavior.getState());
                behavior.setPeekHeight(100);
                break;
            case BottomSheetBehavior.STATE_SETTLING:
                Log.v(TAG , "Behaviour STATE_SETTLING: " + behavior.getState());
                break;
            default:
                Log.v(TAG , "Default case: " + behavior.getState());
                break;

        }

        return false;
    }
}
