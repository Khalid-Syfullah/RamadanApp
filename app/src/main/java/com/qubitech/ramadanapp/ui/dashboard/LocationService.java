package com.qubitech.ramadanapp.ui.dashboard;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.qubitech.ramadanapp.MainActivity;
import com.qubitech.ramadanapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    boolean serviceOn = true;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    Thread streamThread;
    long notify_interval = 1000;
    Intent intent;

    public static String str_receiver = "servicetutorial.service.receiver";
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
        startListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("LocationService","Service started (onStartCommand)");
        startListener();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopListener();

        Log.i("LocationService","Service destroyed (onDestroy)");

    }
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




    private void startListener() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        }
        else {
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


            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
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

    }

    public void stopListener() {
        if (locationManager != null) {

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
