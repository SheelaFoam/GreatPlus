package com.erp.sheelafoam.utils;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.erp.sheelafoam.interfaces.LocationInterface;

/**
 * Created by priyanka.sharma on 9/8/2016.
 */

public class ForCurrentLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 10;
    private Activity activity;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public static final String TAG = ForCurrentLocation.class.getSimpleName();
    private LocationInterface locationInterface;

    public ForCurrentLocation(Activity activity) {
        this.activity = activity;
        try {
            locationInterface = (LocationInterface) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        ForLocation();
    }

    private void ForLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

    }

    public boolean onGoogleApiConnection() {
        mGoogleApiClient.connect();

        return true;
    }

    public void onGoogleApiDisconnect() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Log.i(TAG, "Location services connected.");
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

            } else {
                handleNewLocation(location);
            }
        } catch (SecurityException e) {

        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        Log.e("FromInterface", "" + currentLatitude);
        Log.e("FromInterface", "" + currentLongitude);
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);


        if (locationInterface != null) {
            locationInterface.getCurrentLatLong(currentLatitude, currentLongitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}