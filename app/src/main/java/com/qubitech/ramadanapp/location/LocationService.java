package com.qubitech.ramadanapp.location;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Timer;

public class LocationService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    boolean serviceOn = true;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;

    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    Handler handler;
    long notify_interval = 1000;
    int activityResultCode, permissionResultCode;
    Intent intent, broadcastIntent;
    Context context;
    AlertDialog alertDialog;
    int broadcastType, loopCount=0;

    public static String str_receiver = "locationReceiver";
    public static String invisible_receiver = "invisibleReceiver";
    public static final String CHANNEL_ID = "LocationServiceChannel";


    public LocationService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(str_receiver);
        context = getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        handler = new Handler();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter locationServiceFilter = new IntentFilter(invisible_receiver);
        registerReceiver(locationServiceReceiver, locationServiceFilter);

        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        locationServiceCheck();

        Log.i("LocationService", "Service started (onStartCommand)");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(locationPermissionRunnable);
        stopLocationUpdates();
        unregisterReceiver(locationServiceReceiver);
        Log.i("LocationService", "Service destroyed (onDestroy)");

    }

    Runnable locationPermissionRunnable = new Runnable() {
        @Override
        public void run() {
            if(loopCount <= 5) {
                loopCount++;
                startLocationUpdates();
                handler.postDelayed(this, 1000);
            }
            else{
                loopCount = 0;
                handler.removeCallbacks(this);
                locationServiceCheck();
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {

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



    //Check whether Location Service is on or off
    public void locationServiceCheck() {


        if (isNetworkEnable || isGPSEnable) {
            locationPermissionCheck();

        }
        else{
            broadcastType = 0;
            Log.d("LocationService","Dialog Activity Launched");
            Intent dialogIntent = new Intent(context, LocationServiceActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        }
    }


    private void locationPermissionCheck(){

        Log.d("LocationService","Checking Permission");

        //Location Permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

            broadcastType = 1;
            Log.d("LocationService","Invisible Activity Launched");
            Intent invisibleIntent = new Intent(context, LocationPermissionActivity.class);
            invisibleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(invisibleIntent);


        }
        else{
            locationPermissionRunnable.run();

        }

    }




    BroadcastReceiver locationServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            broadcastIntent = intent;

            Log.d("LocationService","Broadcast Received");
            Log.d("LocationService","Broadcast Type: "+broadcastType);

            if(broadcastType == 0) {
                activityResultCode = broadcastIntent.getIntExtra("activityResultCode",2);
                Log.d("LocationService","activityResultCode: "+broadcastIntent.getIntExtra("activityResultCode",2));

                if (activityResultCode == 0) {
                    locationPermissionCheck();
                }
                else if (activityResultCode == 1) {

                    broadcastType = 0;
                    Intent dialogIntent = new Intent(context, LocationServiceActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);                }
            }

            if(broadcastType == 1) {
                permissionResultCode = broadcastIntent.getIntExtra("permissionResultCode", 2);
                Log.d("LocationService","activityPermissionCode: "+broadcastIntent.getIntExtra("permissionResultCode",2));

                if (permissionResultCode == 1) {
                    startLocationUpdates();
                }
                else if(permissionResultCode == 0){
                    locationPermissionCheck();
                }
            }


        }
    };




    private void startLocationUpdates() {

        Log.d("LocationService","Updating Location");

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnable) {
            location = null;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            if (locationManager!=null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location!=null){

                    Log.i("LocationService","LAT: "+location.getLatitude()+ " LNG: "+location.getLongitude()+" (Network Service)");


                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    sendLocation(location);

                }
            }

        }

        if (isGPSEnable) {

            location = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            if (locationManager!=null){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location!=null){
                    Log.i("LocationService","LAT: "+location.getLatitude()+ " LNG: "+location.getLongitude()+" (GPS Service)");

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();


                    sendLocation(location);
                }
            }
        }


    }

    public void stopLocationUpdates() {


        if (locationManager != null) {
            Log.d("LocationService","Stopping Location Updates");

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            serviceOn = false;
            locationManager.removeUpdates(LocationService.this);
        }
    }





    private void sendLocation(Location location){

        intent.putExtra("latitude",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent);
    }



}
