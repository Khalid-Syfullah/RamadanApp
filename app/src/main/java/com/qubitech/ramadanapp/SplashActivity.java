package com.qubitech.ramadanapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startActivity();

    }

    private void startActivity(){
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences localePreferences = getSharedPreferences("Language", MODE_PRIVATE);

                if(localePreferences.contains("Current_Language")){
                    setLocale(localePreferences.getString("Current_Language",""), localePreferences);
                }
                else{
                    setLocale("bn", localePreferences);
                }
                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        },1000);
    }

    private void setLocale(String localeString, SharedPreferences localePreferences){

        Locale locale = new Locale(localeString);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);

        SharedPreferences.Editor localePreferencesEditor = localePreferences.edit();
        localePreferencesEditor.putString("Current_Language", localeString);
        localePreferencesEditor.apply();
    }

}