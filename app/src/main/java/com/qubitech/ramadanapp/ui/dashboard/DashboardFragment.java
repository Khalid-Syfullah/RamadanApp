package com.qubitech.ramadanapp.ui.dashboard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.qubitech.ramadanapp.ComingSoonActivity;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.SplashActivity;
import com.qubitech.ramadanapp.staticdata.StaticData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class DashboardFragment extends Fragment implements View.OnClickListener{



    DashboardViewModel dashboardViewModel;
    NavController navController;

    Double latitude,longitude;
    Intent locationIntent;
    String prayerUrl ="", prayerUrlNext="";
    TextView fajrTextView, dhuhrTextView, asrTextView, maghribTextView, ishaTextView,
            currentWaqtTextView, currentWaqtTimeTextView, waqtTimeLeftTextView,
            nextSahriTextView, nextIftarTextView, nextIftarTimeLeftTextView;
    String sunrise="",sunset="",midnight="",fajr="",dhuhr="",asr="",maghrib="",isha="",imsak="", city="";
    String sunriseNext="",sunsetNext="",midnightNext="",fajrNext="",dhuhrNext="",asrNext="",maghribNext="",ishaNext="",imsakNext="";

    String[] salahTimes;
    String[] salahWaqts;

    int progress=0;
    SharedPreferences localePreferences;
    Intent broadcastIntent;
    Context broadcastContext;
    LocationTask locationTask;
    Geocoder geocoder;
    HandlerThread backgroundThread;

    Compass compass;
    ImageView arrowView;
    TextView sotwLabel;
    CardView cardView,cardView2,cardView3,cardView4,cardView5,cardView6,cardView7,cardView8,cardView9;
    Button waqtBtn,waqtBtn2, calibrateBtn, alarmBtn;
    ProgressBar waqtTimeLeftProgressBar, nextIftarTimeLeftProgressBar;
    SimpleDateFormat simpleDateFormat;

    float currentAzimuth;
    private static final int[] sides = {0, 45, 90, 135, 180, 225, 270, 315, 360};
    private static String[] names = null;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        fajrTextView = root.findViewById(R.id.textView6);
        dhuhrTextView = root.findViewById(R.id.textView8);
        asrTextView = root.findViewById(R.id.textView10);
        maghribTextView = root.findViewById(R.id.textView12);
        ishaTextView = root.findViewById(R.id.textView14);
        currentWaqtTextView = root.findViewById(R.id.textView16);
        currentWaqtTimeTextView = root.findViewById(R.id.textView43);
        waqtTimeLeftTextView = root.findViewById(R.id.textView45);

        nextSahriTextView = root.findViewById(R.id.textView24);
        nextIftarTextView = root.findViewById(R.id.textView2);
        nextIftarTimeLeftTextView = root.findViewById(R.id.textView25);

        waqtTimeLeftProgressBar = root.findViewById(R.id.progressBar2);
        nextIftarTimeLeftProgressBar = root.findViewById(R.id.progressBar);

        cardView = root.findViewById(R.id.cardView);
        cardView2 = root.findViewById(R.id.cardView2);
        cardView3 = root.findViewById(R.id.cardView3);
        cardView4 = root.findViewById(R.id.cardView4);
        cardView5 = root.findViewById(R.id.cardView5);
        cardView6 = root.findViewById(R.id.cardView6);
        cardView7 = root.findViewById(R.id.cardView7);
        cardView8 = root.findViewById(R.id.cardView8);
        cardView9 = root.findViewById(R.id.cardView9);

        waqtBtn = root.findViewById(R.id.button);
        waqtBtn2 = root.findViewById(R.id.button4);
        calibrateBtn = root.findViewById(R.id.button2);
        alarmBtn = root.findViewById(R.id.button3);

        arrowView = root.findViewById(R.id.imageView25);
        sotwLabel = root.findViewById(R.id.dashboard_quibla_sotw);

        salahWaqts = new String[]{getResources().getString(R.string.fajr), getResources().getString(R.string.dhuhr),
                getResources().getString(R.string.asr), getResources().getString(R.string.maghrib), getResources().getString(R.string.isha), getResources().getString(R.string.fajr)};

        simpleDateFormat = new SimpleDateFormat("HH:mm");

        //Fixing CardView corners
        cardView.setBackgroundResource(R.drawable.cardview_topleftcorner);
        cardView2.setBackgroundResource(R.drawable.cardview_topleftcorner);
        cardView3.setBackgroundResource(R.drawable.cardview_toprightcorner);
        cardView6.setBackgroundResource(R.drawable.cardview_bottomleftcorner);
        cardView7.setBackgroundResource(R.drawable.cardview_bottomrightcorner);
        cardView8.setBackgroundResource(R.drawable.cardview_leftcorner);
        cardView9.setBackgroundResource(R.drawable.cardview_rightcorner);


        waqtBtn.setOnClickListener(this);
        waqtBtn2.setOnClickListener(this);
        calibrateBtn.setOnClickListener(this);
        alarmBtn.setOnClickListener(this);
        cardView.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
        cardView7.setOnClickListener(this);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Locale and Location Broadcast Service Initialization
        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);

        locationPermissionCheck();

        //Compass Initialization
        initLocalizedNames(getActivity().getApplicationContext());
        setupCompass();


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Dashboard", "Compass Starting");
        compass.start();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
        getActivity().startService(locationIntent);
    }




    @Override
    public void onResume() {
        super.onResume();

        initializeTimeData();
        compass.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        compass.stop();
//        getActivity().unregisterReceiver(broadcastReceiver);
//        getActivity().stopService(locationIntent);

    }

    @Override
    public void onStop() {
        super.onStop();
        compass.stop();
        Log.d("Dashboard", "Compass Stopped");

        if(backgroundThread != null) {
            backgroundThread.quit();
        }

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.button:
                cardView.setVisibility(View.VISIBLE);
                cardView2.setVisibility(View.INVISIBLE);
                break;
            case R.id.button2:
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_calibrate);
                break;
            case R.id.button3:
                Intent intent = new Intent(getActivity().getApplicationContext(),ComingSoonActivity.class);
                startActivity(intent);
                break;
            case R.id.button4:
                cardView.setVisibility(View.INVISIBLE);
                cardView2.setVisibility(View.VISIBLE);
                break;
            case R.id.cardView4:
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_mosques);
                break;
            case R.id.cardView5:
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_tasbih);
                break;
            case R.id.cardView6:
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_checklist);
                break;
            case R.id.cardView7:
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_dua);
                break;
        }
    }

    private void locationPermissionCheck(){

        //Location Permission
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }
        else{
            getActivity().startService(locationIntent);
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getActivity().startService(locationIntent);
                    getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Location permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            broadcastIntent = intent;
            broadcastContext = context;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    getLatLng();

                    backgroundThread = new HandlerThread("locationThread");
                    backgroundThread.start();
                    Handler backgroundHandler = new Handler(backgroundThread.getLooper());
                    backgroundHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            nameCheck();
                            prepareApiUrl();

                            //Calling the API using AsyncTask
                            locationTask = new LocationTask();
                            locationTask.execute();
                        }
                    });
                }
            }


        }

    };

    private void getLatLng(){

        try {
            //Get Latitude and Longitude from Location Service Broadcast. Need it for selecting City name in API calls

            latitude = Double.valueOf(broadcastIntent.getStringExtra("latitude"));
            longitude = Double.valueOf(broadcastIntent.getStringExtra("longitude"));

            if(latitude == null && longitude == null) {
                latitude = StaticData.latitude;
                longitude = StaticData.longitude;
            }

            StaticData.latitude = latitude;
            StaticData.longitude = longitude;

            Log.d("Location", latitude + "");
            Log.d("Location", longitude + "");

            broadcastContext.stopService(locationIntent);
            broadcastContext.unregisterReceiver(broadcastReceiver);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void nameCheck(){

        //Create a new Geocoder for getting Location metadata
        geocoder = new Geocoder(broadcastContext);

        //Getting District Name from String Array Resources for Translating Locale from Bangla to English
        String [] districts_bn = broadcastContext.getResources().getStringArray(R.array.division_bn);
        String [] districts_en = broadcastContext.getResources().getStringArray(R.array.division_en);

        //Hashmap for Locale translation
        HashMap<String, String> hashMap = new HashMap<String, String>();

        for(int i=0;i<districts_bn.length;i++) {
            hashMap.put(districts_bn[i], districts_en[i]);
        }


        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); //Using Lat-Lng to set Geocoder sets error sometimes.
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getSubAdminArea();
            String countryName = addresses.get(0).getCountryName();
            city = cityName;

            //Finding the English Value using Bangla Keywords from each entrySet.
            for (Map.Entry<String, String> entry :
                    hashMap.entrySet()) {
                if (entry.getKey().equals(cityName)) {
                    city = entry.getValue();
                    break;
                }

            }

            //Log in console

            Log.d("Location","Admin Area: "+addresses.get(0).getAdminArea());
            Log.d("Location","City Name: "+cityName);
            Log.d("Location","City Name Translated: "+city);
            Log.d("Location","State Name: "+stateName);
            Log.d("Location","Country Name: "+countryName);
            Log.d("Location","Addresses: "+addresses);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareApiUrl(){

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);

            //Selecting city and date for API calls using Geocoder data

            prayerUrl = "https://api.pray.zone/v2/times/today.json?city=" + city + "&juristic=1&school=9";
            prayerUrlNext = "https://api.pray.zone/v2/times/day.json?city=" + city + "&juristic=1&school=9&date=" + simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class LocationTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {



            //API call for Dashboard Activity
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


                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(salahTimes != null) {
                //Update the UI on UI thread
                dailyWaqtTime();
                nextWaqtTime();
                nextSahriTime();
                nextIftarTIme();
            }
        }

    }

    private void initializeTimeData(){
        try {


            if (localePreferences.contains("Current_Language")) {
                String locale = localePreferences.getString("Current_Language", "");
                if (locale.equals("bn")) {

                    banglaTimeFormatter(StaticData.fajrTime, fajrTextView);
                    banglaTimeFormatter(StaticData.dhuhrNextTime, dhuhrTextView);
                    banglaTimeFormatter(StaticData.asrTime, asrTextView);
                    banglaTimeFormatter(StaticData.maghribTime, maghribTextView);
                    banglaTimeFormatter(StaticData.ishaTime, ishaTextView);
                    banglaTimeFormatter(StaticData.sahriNextTime, nextSahriTextView);
                    banglaTimeFormatter(StaticData.iftarNextTime, nextIftarTextView);
                    nextIftarTimeLeftTextView.setText(banglaStringConverter(StaticData.iftarNextTimeLeft) + " মি.");
                    banglaTimeFormatter(StaticData.waqtNextTime, currentWaqtTimeTextView);
                    waqtTimeLeftTextView.setText(banglaStringConverter(StaticData.waqtNextTimeLeft) + " মি.");


                } else if (locale.equals("en")) {

                    englishTimeFormatter(StaticData.fajrTime, fajrTextView);
                    englishTimeFormatter(StaticData.dhuhrTime, dhuhrTextView);
                    englishTimeFormatter(StaticData.asrTime, asrTextView);
                    englishTimeFormatter(StaticData.maghribTime, maghribTextView);
                    englishTimeFormatter(StaticData.ishaTime, ishaTextView);
                    englishTimeFormatter(StaticData.sahriNextTime, nextSahriTextView);
                    englishTimeFormatter(StaticData.iftarNextTime, nextIftarTextView);
                    nextIftarTimeLeftTextView.setText(StaticData.iftarNextTimeLeft);
                    englishTimeFormatter(StaticData.waqtNextTime, currentWaqtTextView);
                    englishTimeFormatter(StaticData.waqtNextTimeLeft, waqtTimeLeftTextView);
                }
            }

            currentWaqtTextView.setText(StaticData.waqtNext);

            //Setting default progress if no progressbar progress is found
            waqtTimeLeftProgressBar.setProgress(StaticData.waqtNextProgress);
            nextIftarTimeLeftProgressBar.setProgress(StaticData.iftarNextProgress);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dailyWaqtTime(){

        try {
            if (localePreferences.contains("Current_Language")) {
                String locale = localePreferences.getString("Current_Language", "");
                if (locale.equals("bn")) {

                    banglaTimeFormatter(fajr, fajrTextView);
                    banglaTimeFormatter(dhuhr, dhuhrTextView);
                    banglaTimeFormatter(asr, asrTextView);
                    banglaTimeFormatter(maghrib, maghribTextView);
                    banglaTimeFormatter(isha, ishaTextView);

                } else if (locale.equals("en")) {

                    englishTimeFormatter(fajr, fajrTextView);
                    englishTimeFormatter(dhuhr, dhuhrTextView);
                    englishTimeFormatter(asr, asrTextView);
                    englishTimeFormatter(maghrib, maghribTextView);
                    englishTimeFormatter(isha, ishaTextView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    if(getActivity() == null){
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


            currentWaqtTextView.setText(upcomingWaqt);
            progress = 100 - progress;
            waqtTimeLeftProgressBar.setProgress(progress);

            StaticData.waqtNext = upcomingWaqt;
            StaticData.waqtNextTime = upcomingWaqtTime;
            StaticData.waqtNextTimeLeft = timeLeft;
            StaticData.waqtNextProgress = progress;


            if (localePreferences.contains("Current_Language")) {
                String locale = localePreferences.getString("Current_Language", "");
                if (locale.equals("bn")) {

                    banglaTimeFormatter(upcomingWaqtTime, currentWaqtTimeTextView);
                    waqtTimeLeftTextView.setText(banglaStringConverter(timeLeft) + " মি.");


                } else if (locale.equals("en")) {
                    englishTimeFormatter(upcomingWaqtTime, currentWaqtTextView);
                    englishTimeFormatter(timeLeft, waqtTimeLeftTextView);


                }
            }
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

                if(localePreferences.contains("Current_Language")) {
                    String locale = localePreferences.getString("Current_Language", "");
                    if (locale.equals("bn")) {
                        banglaTimeFormatter(imsak, nextSahriTextView);
                        StaticData.sahriNextTime = imsak;


                    } else if (locale.equals("en")) {
                        englishTimeFormatter(imsak, nextSahriTextView);
                        StaticData.sahriNextTime = imsak;

                    }
                }
            }
            else if(mills2 < 0 && mills3 >= 0){
                if(localePreferences.contains("Current_Language")) {
                    String locale = localePreferences.getString("Current_Language", "");
                    if (locale.equals("bn")) {
                        banglaTimeFormatter(imsakNext, nextSahriTextView);
                        StaticData.sahriNextTime = imsakNext;


                    } else if (locale.equals("en")) {
                        englishTimeFormatter(imsakNext, nextSahriTextView);
                        StaticData.sahriNextTime = imsakNext;

                    }
                }
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

                if(localePreferences.contains("Current_Language")) {
                    String locale = localePreferences.getString("Current_Language", "");
                    if (locale.equals("bn")) {
                        banglaTimeFormatter(maghrib, nextIftarTextView);
                        StaticData.iftarNextTime = maghrib;


                    } else if (locale.equals("en")) {
                        englishTimeFormatter(maghrib, nextIftarTextView);
                        StaticData.iftarNextTime = maghrib;

                    }
                }


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


                    if(localePreferences.contains("Current_Language")) {
                        String locale = localePreferences.getString("Current_Language", "");
                        if (locale.equals("bn")) {
                            banglaTimeFormatter(maghrib, nextIftarTextView);
                            StaticData.iftarNextTime = maghrib;


                        } else if (locale.equals("en")) {
                            englishTimeFormatter(maghrib, nextIftarTextView);
                            StaticData.iftarNextTime = maghrib;

                        }
                    }

                }

                else if(mills2 < 0){

                    int hours = (int) (mills3 / (1000 * 60 * 60));
                    int mins = (int) (mills3 / (1000 * 60)) % 60;

                    timeLeft = hours + ":" + mins;
                    minutesLeft = hours * 60 + mins;

                    hours = (int) (millsTotal / (1000 * 60 * 60));
                    mins = (int) (millsTotal / (1000 * 60)) % 60;
                    totalMinutes = hours * 60 + mins;


                    if(localePreferences.contains("Current_Language")) {
                        String locale = localePreferences.getString("Current_Language", "");
                        if (locale.equals("bn")) {
                            banglaTimeFormatter(maghribNext, nextIftarTextView);
                            StaticData.iftarNextTime = maghribNext;


                        } else if (locale.equals("en")) {
                            englishTimeFormatter(maghribNext, nextIftarTextView);
                            StaticData.iftarNextTime = maghribNext;

                        }
                    }


                }

            }

            progress = (minutesLeft * 100) / totalMinutes;
            nextIftarTimeLeftProgressBar.setProgress(progress);

            StaticData.iftarNextProgress = progress;

            if(localePreferences.contains("Current_Language")) {
                String locale = localePreferences.getString("Current_Language", "");
                if (locale.equals("bn")) {

                    nextIftarTimeLeftTextView.setText(banglaStringConverter(timeLeft)+" মি.");
                    StaticData.iftarNextTimeLeft = timeLeft;


                }
                else if(locale.equals("en")){
                    englishTimeFormatter(timeLeft,nextIftarTimeLeftTextView);
                    StaticData.iftarNextTimeLeft = timeLeft;

                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private void setupCompass() {
        compass = new Compass(getActivity().getApplicationContext());
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
    }

    private void adjustArrow(float azimuth) {
//        Log.d("Dashboard", "will set rotation from " + currentAzimuth + " to "
//                + azimuth);

        Animation an = new RotateAnimation(-currentAzimuth-90, -azimuth-90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    private void adjustSotwLabel(float azimuth) {
        sotwLabel.setText(format(azimuth));
    }

    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {

                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustArrow(azimuth);
                        adjustSotwLabel(azimuth);
                    }
                });
            }
        };
    }


    public String format(float azimuth) {
        int iAzimuth = (int)azimuth;
        int index = findClosestIndex(iAzimuth);
        return iAzimuth + "° " + names[index];
    }

    private void initLocalizedNames(Context context) {


        if (names == null) {
            names = new String[]{
                    context.getString(R.string.sotw_north),
                    context.getString(R.string.sotw_northeast),
                    context.getString(R.string.sotw_east),
                    context.getString(R.string.sotw_southeast),
                    context.getString(R.string.sotw_south),
                    context.getString(R.string.sotw_southwest),
                    context.getString(R.string.sotw_west),
                    context.getString(R.string.sotw_northwest),
                    context.getString(R.string.sotw_north)
            };
        }
    }

    private static int findClosestIndex(int target) {


        int i = 0, j = sides.length, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (target < sides[mid]) {

                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target);
                }

                j = mid;
            } else {
                if (mid < sides.length-1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target);
                }
                i = mid + 1;
            }
        }

        return mid;
    }


    private static int getClosest(int index1, int index2, int target) {
        if (target - sides[index1] >= sides[index2] - target) {
            return index2;
        }
        return index1;
    }



    public String banglaStringConverter(String eng){

        char[] charArray = eng.toCharArray();
        StringBuilder stringBuilder = new StringBuilder(charArray.length);


        for (int i=0; i<charArray.length; i++ ){

            char character = charArray[i];

            switch (character){
                case ':':
                    stringBuilder.append(":");
                    break;
                case '-':
                    stringBuilder.append("-");
                    break;
                case '0':
                    stringBuilder.append("০");
                    break;
                case '1':
                    stringBuilder.append("১");
                    break;
                case '2':
                    stringBuilder.append("২");
                    break;
                case '3':
                    stringBuilder.append("৩");
                    break;
                case '4':
                    stringBuilder.append("৪");
                    break;
                case '5':
                    stringBuilder.append("৫");
                    break;
                case '6':
                    stringBuilder.append("৬");
                    break;
                case '7':
                    stringBuilder.append("৭");
                    break;
                case '8':
                    stringBuilder.append("৮");
                    break;
                case '9':
                    stringBuilder.append("৯");
                    break;
            }
        }

        return stringBuilder.toString();
    }

    public void banglaTimeFormatter(String eng, TextView view){


        String[] units = eng.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);

        String hoursBn = banglaStringConverter(units[0]);
        String minutesBn = banglaStringConverter(units[1]);

        if(hours >= 1 && hours <= 12){
            view.setText(hoursBn+":"+minutesBn+" মি.");
        }
        else if (hours == 0){
            view.setText(hoursBn+":"+minutesBn+" মি.");
        }
        else if(hours > 12){

            view.setText(banglaStringConverter(Integer.toString(hours-12))+":"+minutesBn+" মি.");
        }
    }


    public void englishTimeFormatter(String eng, TextView view){


        String[] units = eng.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);

        if(hours >= 1 && hours <= 12){
            view.setText(hours+":"+minutes+" am");
        }
        else if (hours == 0){
            view.setText(hours+":"+minutes+" am");
        }
        else if(hours > 12){
            view.setText(hours-12+":"+minutes+" pm");
        }

    }




}