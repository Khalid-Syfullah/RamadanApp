package com.qubitech.ramadanapp.location;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

public class LocationServiceActivity extends AppCompatActivity {

    AlertDialog alertDialog;
    Intent intent;
    String intentName = "invisibleReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(intentName);
        locationServiceDialog();
    }

    //Ask to turn on location service
    private void locationServiceDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn on Location Service")
                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        alertDialog.dismiss();
                        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(settingsIntent, 2);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                        locationServiceDialog();
                    }
                });
        alertDialog = builder.create();

        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            Log.d("DialogActivity","activityResultCode: "+resultCode);

            if(resultCode == 0) {
                intent.putExtra("activityResultCode",0);
                sendBroadcast(intent);
                finish();
            }
            else {
                intent.putExtra("activityResultCode",1);
                sendBroadcast(intent);
                finish();
            }
        }

    }
}