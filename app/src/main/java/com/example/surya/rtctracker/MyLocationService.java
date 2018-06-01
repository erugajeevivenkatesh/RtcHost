package com.example.surya.rtctracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


public class MyLocationService extends Service {
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    public Location mCurrentLocation;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation=location;
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }


    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

//
//    LocationListener[] mLocationListeners = new LocationListener[]{
//            new LocationListener(LocationManager.PASSIVE_PROVIDER)
//    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
     //   super.onStartCommand(intent, flags, startId);


        Log.e(TAG, "onCreate");

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Toast.makeText(getApplicationContext(), "fail to request location update, ignore", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException ex) {
            //  Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            Toast.makeText(getApplicationContext(), "network provider does not exist", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(), "you are in create", Toast.LENGTH_SHORT).show();

//        double lat=  mCurrentLocation.getLatitude();
//        Toast.makeText(getApplicationContext(),Double.toString(lat),Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

//    @Override
//    public void onCreate() {
//
//        Log.e(TAG, "onCreate");
//
//        initializeLocationManager();
//
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.PASSIVE_PROVIDER,
//                    LOCATION_INTERVAL,
//                    LOCATION_DISTANCE,
//                    mLocationListeners[0]
//            );
//        } catch (java.lang.SecurityException ex) {
//          //  Log.i(TAG, "fail to request location update, ignore", ex);
//            Toast.makeText(getApplicationContext(), "fail to request location update, ignore", Toast.LENGTH_SHORT).show();
//        } catch (IllegalArgumentException ex) {
//          //  Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//            Toast.makeText(getApplicationContext(), "network provider does not exist", Toast.LENGTH_SHORT).show();
//        }
//
//        /*try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    LOCATION_INTERVAL,
//                    LOCATION_DISTANCE,
//                    mLocationListeners[1]
//            );
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }*/
//    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                 //   Log.i(TAG, "fail to remove location listener, ignore", ex);
                    Toast.makeText(getApplicationContext(), "fail to remove location listener, ignoret", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}