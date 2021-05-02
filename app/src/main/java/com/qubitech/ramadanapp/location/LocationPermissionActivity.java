package com.qubitech.ramadanapp.location;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.staticdata.StaticData;

public class LocationPermissionActivity extends AppCompatActivity {

    Intent intent;
    String intentName = "invisibleReceiver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invisible);
        intent = new Intent(intentName);

        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                1);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("InvisibleActivity","permissionResultCode: "+1);
                    StaticData.permissionGranted = true;
                    intent.putExtra("permissionResultCode",1);
                    sendBroadcast(intent);
                    finish();

                }
                else {
                    Log.d("InvisibleActivity","permissionResultCode: "+0);
                    intent.putExtra("permissionResultCode",0);
                    sendBroadcast(intent);
                    finish();
                }
                return;
            }

        }
    }
}