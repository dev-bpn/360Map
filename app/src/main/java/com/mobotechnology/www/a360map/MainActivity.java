package com.mobotechnology.www.a360map;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener
        , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    private BottomSheetBehavior behavior;
    private TextView bottomSheetTextView;
    private GoogleMap googleMap;

    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private double latitude = 0.0;
    private double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViews();

        initGoogleApiClient();
        initMapFragment();


    }


    /**
     * init map fragment and map async task
     */
    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * init google api client
     */
    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * views initialization
     */
    private void initViews() {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        View view = coordinatorLayout.findViewById(R.id.design_bottom_sheet);
        bottomSheetTextView = (TextView) coordinatorLayout.findViewById(R.id.bottomSheetTextView);
        bottomSheetTasks(view);

    }


    /**
     * BottomSheet task
     */
    private void bottomSheetTasks(View view) {

        behavior = BottomSheetBehavior.from(view);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
    }


    /**
     * Toolbar Initialization
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    /**
     * Override method called when map is ready
     */
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        initLatLangArray();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        for (int i = 0; i < latLngArrayList.size(); i++) {
            directZoomLabel();
            addingMultipleMarker(map, latLngArrayList, i);
        }

        /** Animate the change in camera view over 2 seconds*/
//        CameraPosition cameraPosition = CameraPosition.builder()
//                .target(new LatLng(latitude, longitude)).zoom(13)
//                .bearing(90) /** Rotate map*/
//                .build();
//
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
//                2000, null);

        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
    }


    /**
     * Initializing LatLang Array
     */
    private void initLatLangArray() {
//        latLngArrayList.add(new LatLng(27.674351, 85.314379));
        latLngArrayList.add(new LatLng(27.748050, 85.301468));
        latLngArrayList.add(new LatLng(27.694343, 85.321191));
        latLngArrayList.add(new LatLng(27.705630, 85.333299));
        latLngArrayList.add(new LatLng(27.728903, 85.331732));
    }


    /**
     * Animate Camera Initialization
     */
    private void animateCameraInitializing() {

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(latitude, longitude)).zoom(13)
                .bearing(90) /** Rotate map*/
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

    }


    /**
     * Adding multiple marker
     */
    private void addingMultipleMarker(GoogleMap map, ArrayList<LatLng> latLngArrayList, int i) {
        map.addMarker(new MarkerOptions()
                .position(latLngArrayList.get(i))
                .title("Marker at Lat/Long: "))
                .setSnippet(latLngArrayList.get(i).toString());
    }


    /**
     * Direct zoom to label 13
     */
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
        Log.v(TAG, "Behaviour gerState: " + behavior.getState());

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        switch (behavior.getState()) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                Log.v(TAG, "Behaviour STATE_COLLAPSED: " + behavior.getState());
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                Log.v(TAG, "Behaviour STATE_DRAGGING: " + behavior.getState());
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                Log.v(TAG, "Behaviour STATE_EXPANDED: " + behavior.getState());
                break;
            case BottomSheetBehavior.STATE_HIDDEN:
                Log.v(TAG, "Behaviour STATE_HIDDEN: " + behavior.getState());
                behavior.setPeekHeight(100);
                break;
            case BottomSheetBehavior.STATE_SETTLING:
                Log.v(TAG, "Behaviour STATE_SETTLING: " + behavior.getState());
                break;
            default:
                Log.v(TAG, "Default case: " + behavior.getState());
                break;

        }

        return false;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i(TAG, "Location services connected.");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        animateCameraInitializing();
        Log.i(TAG, "Latitude: " + latitude);
        Log.i(TAG, "Longitude: " + longitude);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i(TAG, "Location services onConnectionFailed.");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
