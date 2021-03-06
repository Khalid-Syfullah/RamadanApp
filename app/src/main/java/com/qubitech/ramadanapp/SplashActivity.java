package com.qubitech.ramadanapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.qubitech.ramadanapp.location.LocationService;
import com.qubitech.ramadanapp.staticdata.StaticData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {

    Double latitude,longitude;
    Intent locationIntent;
    String prayerUrl ="", prayerUrlNext="";
    String sunrise="",sunset="",midnight="",fajr="",dhuhr="",asr="",maghrib="",isha="",imsak="", city="",district="",division="";
    String sunriseNext="",sunsetNext="",midnightNext="",fajrNext="",dhuhrNext="",asrNext="",maghribNext="",ishaNext="",imsakNext="";

    String[] salahTimes;
    String[] salahWaqts;
    String [] division_bn,division_en,district_bn,district_en,upazilla_bn,upazilla_en;

    int progress=0;
    boolean dialogPresented = false;

    Intent broadcastIntent;

    LocationTask locationTask;
    Geocoder geocoder;
    HandlerThread backgroundThread;
    SimpleDateFormat simpleDateFormat;
    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.splash_logo);
        progressBar = findViewById(R.id.splash_progressBar);

        locationIntent = new Intent(SplashActivity.this, LocationService.class);

        simpleDateFormat = new SimpleDateFormat("HH:mm");
        salahWaqts = new String[]{getResources().getString(R.string.fajr), getResources().getString(R.string.dhuhr),
                getResources().getString(R.string.asr), getResources().getString(R.string.maghrib), getResources().getString(R.string.isha), getResources().getString(R.string.fajr)};

        //Getting Division, District and Upazilla Names from String Array Resources for Translating Locale from Bangla to English
        division_bn = getResources().getStringArray(R.array.division_bn);
        division_en = getResources().getStringArray(R.array.division_en);

        district_bn = getResources().getStringArray(R.array.district_bn);
        district_en = getResources().getStringArray(R.array.district_en);

        upazilla_bn = getResources().getStringArray(R.array.upazilla_bn);
        upazilla_en = getResources().getStringArray(R.array.upazilla_en);


        Animation rotateAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.accelerate_rotate);
        imageView.startAnimation(rotateAnimation);

//        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "rotationY", 180f, 360f);
//        animation.setDuration(600);
//        animation.setInterpolator(new AccelerateDecelerateInterpolator());
//        animation.start();
//        animation.setRepeatCount(0);
//        animation.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                imageView.setImageResource(R.drawable.mainicon);
//                Animation rotateAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.accelerate_rotate);
//                imageView.startAnimation(rotateAnimation);
//            }
//        });


        //locationServiceStatusCheck();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(locationIntent);
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(locationIntent != null) {
            stopService(locationIntent);
        }
        if(backgroundThread != null) {
            backgroundThread.quit();
        }


        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Splash","Result: "+resultCode);
        if (requestCode == 2) {
            if(resultCode == 0) {
              gotoDashboard();

            }

        }

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            broadcastIntent = intent;
            if(dialogPresented == false) {
                checkConnection();
                dialogPresented = true;
            }


        }

    };


    private void gotoDashboard(){
        Handler handler =new Handler(Looper.getMainLooper());
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
                progressBar.setVisibility(View.GONE);
                Intent intent =new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        },600);
    }

    private void checkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                progressBar.setVisibility(View.VISIBLE);
                getLatLng();

                backgroundThread = new HandlerThread("locationThread");
                backgroundThread.start();
                Handler backgroundHandler = new Handler(backgroundThread.getLooper());
                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromLatLng();
                        city = translateDataToEn(city,upazilla_bn,upazilla_en);
                        prepareApiUrl(city);
                        if(testApiCall()) {
                            //Calling the API using AsyncTask
                            locationTask = new LocationTask();
                            locationTask.execute();
                        }
                        else{
                            district = translateDataToEn(district,district_bn,district_en);
                            prepareApiUrl(district);
                            if(testApiCall()) {
                                //Calling the API using AsyncTask
                                locationTask = new LocationTask();
                                locationTask.execute();
                            }
                            else{
                                division = translateDataToEn(division,division_bn,division_en);
                                prepareApiUrl(division);
                                if(testApiCall()) {
                                    //Calling the API using AsyncTask
                                    locationTask = new LocationTask();
                                    locationTask.execute();
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    retryDialogOnServerIssue();
                                    return;
                                }
                            }
                        }

                    }
                });
            }
            else{
                progressBar.setVisibility(View.GONE);
                retryDialog();
            }

        }
        else{
            progressBar.setVisibility(View.GONE);
            retryDialog();
        }
    }
    //Ask to turn on location service
    private void retryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet access")
                .setMessage("Enable your Internet connection and tap Retry")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkConnection();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(locationIntent != null) {
                            stopService(locationIntent);
                        }
                        if(broadcastReceiver != null){
                            unregisterReceiver(broadcastReceiver);
                        }
                        finish();
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Ask to turn on location service
    private void retryDialogOnServerIssue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No response from server")
                .setMessage("Tap retry or try again after a while")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkConnection();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(locationIntent != null) {
                            stopService(locationIntent);
                        }
                        if(broadcastReceiver != null){
                            unregisterReceiver(broadcastReceiver);
                        }
                        finish();
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getLatLng(){

        try {

            //Get Latitude and Longitude from Location Service Broadcast. Need it for selecting City name in API calls
            latitude = Double.valueOf(broadcastIntent.getStringExtra("latitude"));
            longitude = Double.valueOf(broadcastIntent.getStringExtra("longitude"));


            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(3);

            latitude = Double.parseDouble(df.format(latitude));
            longitude = Double.parseDouble(df.format(longitude));

            StaticData.latitude = latitude;
            StaticData.longitude = longitude;

            Log.d("Location", latitude + "");
            Log.d("Location", longitude + "");

            stopService(locationIntent);
            unregisterReceiver(broadcastReceiver);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    private void getDataFromLatLng(){

        //Create a new Geocoder for getting Location metadata
        geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); //Using Lat-Lng to set Geocoder sets error sometimes.
            city = addresses.get(0).getLocality();
            district = addresses.get(0).getSubAdminArea();
            division = addresses.get(0).getAdminArea();

            if(district.contains(" District")){
                district = district.replace(" District","");
            }
            else if(district.contains(" ????????????")){
                district = district.replace(" ????????????","");
            }

            if(division.contains(" Division")){
                division = division.replace(" Division","");
            }
            else if(division.contains(" ???????????????")){
                division = division.replace(" ???????????????","");
            }



            //Log in console

            Log.d("Location","City Name: "+city);
            Log.d("Location","District Name: "+district);
            Log.d("Location","Division Name: "+division);
            Log.d("Location","Addresses: "+addresses);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String translateDataToEn(String keyword, String[] array_bn, String[] array_en){

        //Hashmap for Locale translation
        HashMap<String, String> hashMap = new HashMap<String, String>();

        for(int i=0;i<array_bn.length;i++) {
            hashMap.put(array_bn[i], array_en[i]);
        }

        //Finding the English Value using Bangla Keywords from each entrySet.
        for (Map.Entry<String, String> entry :
                hashMap.entrySet()) {
            if (entry.getKey().equals(keyword)) {
                keyword = entry.getValue();
                break;
            }

        }

        Log.d("Location","Keyword Translated: "+keyword);

        return keyword;
    }

    private void prepareApiUrl(String key){

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);

            //Selecting city and date for API calls using Geocoder data

            prayerUrl = "https://api.pray.zone/v2/times/today.json?city=" + key + "&juristic=1&school=9";
            prayerUrlNext = "https://api.pray.zone/v2/times/day.json?city=" + key + "&juristic=1&school=9&date=" + simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean testApiCall() {

        boolean bool = false;

        //API call for Dashboard Activity
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(prayerUrl)
                .build();

        Response response;

        try {
            response = client.newCall(request).execute();

            if(response.isSuccessful()){
                bool = true;
            }
            else{
                bool = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            retryDialog();

        }
        Log.d("Dashboard","Response: "+bool);
        return bool;

    }


    private class LocationTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(Void... voids) {


            //API call for City-based Prayer Time
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(prayerUrl)
                    .build();

            try {
                Response response = client.newCall(request).execute();


                //Getting Today's times from API
                if(response.body() != null){
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("results");
                    JSONObject jsonObject2 = jsonObject1.getJSONArray("datetime").getJSONObject(0);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("times");
                    Log.d("Response", "Sunrise Today: " + jsonObject3.getString("Sunrise"));

                    sunrise = jsonObject3.getString("Sunrise");
                    sunset = jsonObject3.getString("Sunset");
                    midnight = jsonObject3.getString("Midnight");
                    fajr = jsonObject3.getString("Fajr");
                    dhuhr = jsonObject3.getString("Dhuhr");
                    asr = jsonObject3.getString("Asr");
                    maghrib = jsonObject3.getString("Maghrib");
                    isha = jsonObject3.getString("Isha");
                    imsak = jsonObject3.getString("Imsak");


                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            request = new Request.Builder()
                    .url(prayerUrlNext)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                //Getting Tomorrow's Times from API
                if(response.body() != null){
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("results");
                    JSONObject jsonObject2 = jsonObject1.getJSONArray("datetime").getJSONObject(0);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("times");
                    Log.d("Response", "Sunrise Tomorrow: " + jsonObject3.getString("Sunrise"));


                    sunriseNext = jsonObject3.getString("Sunrise");
                    sunsetNext = jsonObject3.getString("Sunset");
                    midnightNext = jsonObject3.getString("Midnight");
                    fajrNext = jsonObject3.getString("Fajr");
                    dhuhrNext = jsonObject3.getString("Dhuhr");
                    asrNext = jsonObject3.getString("Asr");
                    maghribNext = jsonObject3.getString("Maghrib");
                    ishaNext = jsonObject3.getString("Isha");
                    imsakNext = jsonObject3.getString("Imsak");

                    StaticData.fajrTime = fajr;
                    StaticData.dhuhrTime = dhuhr;
                    StaticData.asrTime = asr;
                    StaticData.maghribTime = maghrib;
                    StaticData.ishaTime = isha;
                    StaticData.imsakTime = imsak;

                    StaticData.fajrNextTime = fajrNext;
                    StaticData.dhuhrNextTime = dhuhrNext;
                    StaticData.asrNextTime = asrNext;
                    StaticData.maghribNextTime = maghribNext;
                    StaticData.ishaNextTime = ishaNext;
                    StaticData.imsakNextTime = imsakNext;

                    //An array containing consequent Salah Times
                    salahTimes = new String[]{fajr, dhuhr, asr, maghrib, isha, fajrNext};

                    if(salahTimes != null) {
                        //Update the UI on UI thread
                        nextWaqtTime();
                        nextSahriTime();
                        nextIftarTIme();
                    }

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gotoDashboard();


        }

    }


    private void nextWaqtTime() {

        try {

            String currentTimeString = simpleDateFormat.format(Calendar.getInstance().getTime());
            String upcomingWaqt = "", upcomingWaqtTime = "", timeLeft = "";
            int minutesLeft = 0, totalMinutes = 0;
            long millsPrev;
            long millsTotal;
            long millsNext;

            for (int i = 0; i < 5; i++) {

                String prevWaqtTime = salahTimes[i];
                String nextWaqtTime = salahTimes[i + 1];

                Date prevTime = simpleDateFormat.parse(prevWaqtTime);
                Date currentTime = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
                Date nextTime = simpleDateFormat.parse(nextWaqtTime);


                millsPrev = prevTime.getTime() - currentTime.getTime();
                millsTotal = nextTime.getTime() - prevTime.getTime();


                if (millsPrev < 0) {
                    upcomingWaqt = salahWaqts[i + 1];
                    upcomingWaqtTime = nextWaqtTime;

                    millsNext = nextTime.getTime() - currentTime.getTime();

                    if (millsNext > 0) {
                        int hoursNext = (int) (millsNext / (1000 * 60 * 60));
                        int minsNext = (int) (millsNext / (1000 * 60)) % 60;
                        timeLeft = hoursNext + ":" + minsNext;
                        minutesLeft = hoursNext * 60 + minsNext;

                        hoursNext = (int) (millsTotal/(1000 * 60 * 60));
                        minsNext = (int) (millsTotal/(1000*60)) % 60;
                        totalMinutes = hoursNext * 60 + minsNext;
                        progress = (minutesLeft * 100) / totalMinutes;

                        break;
                    }

                    else if( millsNext <= 0 && i == 4){

                        millsNext = 1000*60*60*24 - currentTime.getTime() + nextTime.getTime();

                        int hoursNext = (int) (millsNext / (1000 * 60 * 60));
                        int minsNext = (int) (millsNext / (1000 * 60)) % 60;
                        timeLeft = hoursNext + ":" + minsNext;
                        minutesLeft = hoursNext * 60 + minsNext;

                        millsTotal = 1000*60*60*24 - prevTime.getTime() + nextTime.getTime();

                        hoursNext = (int) (millsTotal/(1000 * 60 * 60));
                        minsNext = (int) (millsTotal/(1000*60)) % 60;
                        totalMinutes = hoursNext * 60 + minsNext;
                        progress = (minutesLeft * 100) / totalMinutes;

                        break;
                    }


                }
                else if(millsPrev >= 0){
                    if(getApplicationContext() == null){
                        return;
                    }
                    upcomingWaqt = getResources().getString(R.string.fajr);
                    upcomingWaqtTime = fajr;

                    prevWaqtTime = isha;
                    nextWaqtTime = fajr;

                    prevTime = simpleDateFormat.parse(prevWaqtTime);
                    currentTime = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
                    nextTime = simpleDateFormat.parse(nextWaqtTime);

                    millsNext = nextTime.getTime() - currentTime.getTime();

                    int hoursNext = (int) (millsNext / (1000 * 60 * 60));
                    int minsNext = (int) (millsNext / (1000 * 60)) % 60;
                    timeLeft = hoursNext + ":" + minsNext;
                    minutesLeft = hoursNext * 60 + minsNext;

                    millsTotal = 1000*60*60*24 - prevTime.getTime() + nextTime.getTime();

                    hoursNext = (int) (millsTotal/(1000 * 60 * 60));
                    minsNext = (int) (millsTotal/(1000*60)) % 60;
                    totalMinutes = hoursNext * 60 + minsNext;
                    progress = (minutesLeft * 100) / totalMinutes;

                    break;

                }





                Log.d("DateTime", "Waqt " + i + " Time: " + prevWaqtTime);

            }


            Log.d("DateTime", "Current Time: " + currentTimeString);
            Log.d("DateTime", "Upcoming Waqt: " + upcomingWaqt);
            Log.d("DateTime", "Upcoming Waqt Time: " + upcomingWaqtTime);
            Log.d("DateTime", "Upcoming Waqt Time Left: " + timeLeft);
            Log.d("DateTime", "Upcoming Waqt Minutes Left: " + minutesLeft);
            Log.d("DateTime", "Upcoming Waqt Total Minutes: " + totalMinutes);
            Log.d("DateTime", "Upcoming Waqt Progress: " + progress);


            progress = 100 - progress;
            StaticData.waqtNext = upcomingWaqt;
            StaticData.waqtNextTime = upcomingWaqtTime;
            StaticData.waqtNextTimeLeft = timeLeft;
            StaticData.waqtNextProgress = progress;


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void nextSahriTime(){

        try {
            Date currentTime = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            Date date1 = simpleDateFormat.parse("00:00");
            Date date2 = simpleDateFormat.parse(imsak);
            Date date3 = simpleDateFormat.parse("23:59");


            long mills1 = date1.getTime() - currentTime.getTime();
            long mills2 = date2.getTime() - currentTime.getTime();
            long mills3 = date3.getTime() - currentTime.getTime();


            if(mills1 <= 0 && mills2 >= 0){

                StaticData.sahriNextTime = imsak;
            }

            else if(mills2 < 0 && mills3 >= 0){

                StaticData.sahriNextTime = imsakNext;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    public void nextIftarTIme() {


        try {
            Date currentTime = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            Date prevTime = simpleDateFormat.parse(maghrib);
            Date nextTime = simpleDateFormat.parse(maghribNext);

            String timeLeft="";
            int minutesLeft=0, totalMinutes=0;

            long mills = prevTime.getTime() - currentTime.getTime();
            long mills2 = nextTime.getTime() - currentTime.getTime();
            long mills3 = (1000*60*60*24) + mills2;

            long millsTotal = 1000*60*60*24;


            if(mills >= 0){

                int hours = (int) (mills/(1000 * 60 * 60));
                int mins = (int) (mills/(1000*60)) % 60;
                timeLeft = hours+":"+mins;
                minutesLeft = hours * 60 + mins;

                hours = (int) (millsTotal/(1000 * 60 * 60));
                mins = (int) (millsTotal/(1000*60)) % 60;
                totalMinutes = hours * 60 + mins;

                StaticData.iftarNextTime = maghrib;



            }
            else if(mills < 0){


                if(mills2 >= 0) {
                    int hours = (int) (mills2 / (1000 * 60 * 60));
                    int mins = (int) (mills2 / (1000 * 60)) % 60;

                    timeLeft = hours + ":" + mins;
                    minutesLeft = hours * 60 + mins;

                    hours = (int) (millsTotal / (1000 * 60 * 60));
                    mins = (int) (millsTotal / (1000 * 60)) % 60;
                    totalMinutes = hours * 60 + mins;

                    StaticData.iftarNextTime = maghrib;


                }

                else if(mills2 < 0){

                    int hours = (int) (mills3 / (1000 * 60 * 60));
                    int mins = (int) (mills3 / (1000 * 60)) % 60;

                    timeLeft = hours + ":" + mins;
                    minutesLeft = hours * 60 + mins;

                    hours = (int) (millsTotal / (1000 * 60 * 60));
                    mins = (int) (millsTotal / (1000 * 60)) % 60;
                    totalMinutes = hours * 60 + mins;

                    StaticData.iftarNextTime = maghribNext;

                }

            }

            progress = (minutesLeft * 100) / totalMinutes;
            StaticData.iftarNextProgress = progress;
            StaticData.iftarNextTimeLeft = timeLeft;


        } catch (ParseException e) {
            e.printStackTrace();
        }


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