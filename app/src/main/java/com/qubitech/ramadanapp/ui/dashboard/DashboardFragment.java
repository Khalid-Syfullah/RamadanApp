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
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.qubitech.ramadanapp.ComingSoonActivity;
import com.qubitech.ramadanapp.R;

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
    String sunrise="",sunset="",midnight="",fajr="",dhuhr="",asr="",maghrib="",isha="",imsak="";
    String sunriseNext="",sunsetNext="",midnightNext="",fajrNext="",dhuhrNext="",asrNext="",maghribNext="",ishaNext="",imsakNext="";

    String[] salahTimes;
    String[] salahWaqts;

    int progress=0;
    SharedPreferences localePreferences;

    Compass compass;
    ImageView arrowView;
    TextView sotwLabel;
    CardView cardView,cardView2,cardView3,cardView4,cardView5,cardView6,cardView7,cardView8,cardView9;
    Button waqtBtn,waqtBtn2, calibrateBtn, alarmBtn;
    ProgressBar waqtTimeLeftProgressBar, nextIftarTimeLeftProgressBar;
    apiData apiData;
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
                getResources().getString(R.string.asr), getResources().getString(R.string.maghrib), getResources().getString(R.string.isha)};

        simpleDateFormat = new SimpleDateFormat("HH:mm");

        cardView.setBackgroundResource(R.drawable.cardview_topleftcorner);
        cardView2.setBackgroundResource(R.drawable.cardview_topleftcorner);
        cardView3.setBackgroundResource(R.drawable.cardview_toprightcorner);
        cardView6.setBackgroundResource(R.drawable.cardview_bottomleftcorner);
        cardView7.setBackgroundResource(R.drawable.cardview_bottomrightcorner);
        cardView8.setBackgroundResource(R.drawable.cardview_leftcorner);
        cardView9.setBackgroundResource(R.drawable.cardview_rightcorner);


        fajrTextView.setText("");
        dhuhrTextView.setText("");
        asrTextView.setText("");
        maghribTextView.setText("");
        ishaTextView.setText("");

        waqtTimeLeftProgressBar.setProgress(progress);
        nextIftarTimeLeftProgressBar.setProgress(progress);

        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION },
                            100);
        }

        initLocalizedNames(getActivity().getApplicationContext());
        setupCompass();

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

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Dashboard", "start compass");
        compass.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        compass.stop();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(locationIntent);

    }

    @Override
    public void onResume() {
        super.onResume();
        compass.start();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
        getActivity().startService(locationIntent);


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Dashboard", "stop compass");
        compass.stop();
        getActivity().stopService(locationIntent);


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Locale aLocale = new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("US").build();
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), aLocale);

            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            String [] districts_bn = getResources().getStringArray(R.array.division_bn);
            String [] districts_en = getResources().getStringArray(R.array.division_en);

            HashMap<String, String> hashMap = new HashMap<String, String>();

            for(int i=0;i<districts_bn.length;i++) {
                hashMap.put(districts_bn[i], districts_en[i]);
            }


            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getSubAdminArea();
                String countryName = addresses.get(0).getCountryName();
                String city="";


                for (Map.Entry<String, String> entry :
                        hashMap.entrySet()) {
                    if (entry.getKey().equals(cityName)) {
                        city = entry.getValue();
                    }
                }

                Log.d("Location","Admin Area: "+addresses.get(0).getAdminArea());
                Log.d("Location","City Name: "+cityName);
                Log.d("Location","City Name Translated: "+city);
                Log.d("Location","State Name: "+stateName);
                Log.d("Location","Country Name: "+countryName);
                Log.d("Location","Addresses: "+addresses);


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);

                prayerUrl = "https://api.pray.zone/v2/times/today.json?city="+city+"&juristic=1&school=9";
                prayerUrlNext = "https://api.pray.zone/v2/times/day.json?city="+city+"&juristic=1&school=9&date="+simpleDateFormat.format(calendar.getTime());



            } catch (IOException e1) {
                e1.printStackTrace();
            }



            apiData = new apiData();
            apiData.execute();

            Log.d("Location",latitude+"");
            Log.d("Location",longitude+"");


        }
    };


    private class apiData extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(prayerUrl)
                    .build();

            try {
                Response response = client.newCall(request).execute();

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

                    salahTimes = new String[]{fajr, dhuhr, asr, maghrib, isha};


                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            request = new Request.Builder()
                    .url(prayerUrlNext)
                    .build();

            try {
                Response response = client.newCall(request).execute();

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

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            dailyWaqtTime();
            nextWaqtTime();
            nextSahriTime();
            nextIftarTIme();
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


    public void nextSahriTime(){

        try {
            Date date0 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            Date date1 = simpleDateFormat.parse("00:00");
            Date date2 = simpleDateFormat.parse(imsak);
            Date date3 = simpleDateFormat.parse("23:59");


            long mills1 = date1.getTime() - date0.getTime();
            long mills2 = date2.getTime() - date0.getTime();
            long mills3 = date3.getTime() - date0.getTime();


            if(mills1 <= 0 && mills2 >= 0){

                if(localePreferences.contains("Current_Language")) {
                    String locale = localePreferences.getString("Current_Language", "");
                    if (locale.equals("bn")) {
                        banglaTimeFormatter(imsak, nextSahriTextView);


                    } else if (locale.equals("en")) {
                        englishTimeFormatter(imsak, nextSahriTextView);

                    }
                }
            }
            else if(mills2 < 0 && mills3 >= 0){
                if(localePreferences.contains("Current_Language")) {
                    String locale = localePreferences.getString("Current_Language", "");
                    if (locale.equals("bn")) {
                        banglaTimeFormatter(imsakNext, nextSahriTextView);


                    } else if (locale.equals("en")) {
                        englishTimeFormatter(imsakNext, nextSahriTextView);

                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    public void nextIftarTIme() {


        try {
            Date date0 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            Date date1 = simpleDateFormat.parse(maghrib);
            Date date2 = simpleDateFormat.parse(maghribNext);

            String timeLeft="";
            int minutesLeft=0, totalMinutes=0;

            long mills = date1.getTime() - date0.getTime();
            long mills2 = date2.getTime() - date0.getTime();
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


                    } else if (locale.equals("en")) {
                        englishTimeFormatter(maghrib, nextIftarTextView);

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


                        } else if (locale.equals("en")) {
                            englishTimeFormatter(maghrib, nextIftarTextView);

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


                        } else if (locale.equals("en")) {
                            englishTimeFormatter(maghribNext, nextIftarTextView);

                        }
                    }


                }

            }

            progress = (minutesLeft * 100) / totalMinutes;
            nextIftarTimeLeftProgressBar.setProgress(progress);

            if(localePreferences.contains("Current_Language")) {
                String locale = localePreferences.getString("Current_Language", "");
                if (locale.equals("bn")) {

                    nextIftarTimeLeftTextView.setText(banglaStringConverter(timeLeft)+" মি.");


                }
                else if(locale.equals("en")){
                    englishTimeFormatter(timeLeft,nextIftarTimeLeftTextView);
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    private void nextWaqtTime() {

        try {

            String currentTime = simpleDateFormat.format(Calendar.getInstance().getTime());
            String upcomingWaqt = "", upcomingWaqtTime = "", timeLeft = "";
            int minutesLeft = 0, totalMinutes = 0;

            for (int i = 0; i < 4; i++) {

                String prevWaqtTime = salahTimes[i];
                String nextWaqtTime = salahTimes[i + 1];

                Date date0 = simpleDateFormat.parse(prevWaqtTime);
                Date date1 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
                Date date2 = simpleDateFormat.parse(nextWaqtTime);

                long millsPrev = date0.getTime() - date1.getTime();

                if (millsPrev < 0) {
                    upcomingWaqt = salahWaqts[i + 1];
                    upcomingWaqtTime = nextWaqtTime;

                    long millsNext = date2.getTime() - date1.getTime();

                    if (millsNext > 0) {
                        int hoursNext = (int) (millsNext / (1000 * 60 * 60));
                        int minsNext = (int) (millsNext / (1000 * 60)) % 60;
                        timeLeft = hoursNext + ":" + minsNext;
                        minutesLeft = hoursNext * 60 + minsNext;
                        totalMinutes = hoursNext * 60 + minsNext;
                        progress = (minutesLeft * 100) / totalMinutes;

                        break;
                    }

                } else if (i == 3) {

                    upcomingWaqt = salahWaqts[i + 1];
                    upcomingWaqtTime = nextWaqtTime;

                    millsPrev = date0.getTime() - date1.getTime();

                    if (millsPrev < 0) {

                        long millsNext = date2.getTime() - date1.getTime();

                        if (millsNext > 0) {
                            int hoursNext = (int) (millsNext / (1000 * 60 * 60));
                            int minsNext = (int) (millsNext / (1000 * 60)) % 60;
                            timeLeft = hoursNext + ":" + minsNext;
                            minutesLeft = hoursNext * 60 + minsNext;
                            totalMinutes = hoursNext * 60 + minsNext;
                            progress = (minutesLeft * 100) / totalMinutes;

                            break;
                        } else if (millsNext < 0) {
                            upcomingWaqt = getResources().getString(R.string.fajr);
                            upcomingWaqtTime = fajrNext;

                            prevWaqtTime = isha;
                            nextWaqtTime = fajrNext;

                            date0 = simpleDateFormat.parse(prevWaqtTime);
                            date1 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
                            date2 = simpleDateFormat.parse(nextWaqtTime);

                            millsPrev = date0.getTime() - date1.getTime();

                            if (millsPrev < 0) {

                                millsNext = date2.getTime() - date1.getTime();

                                if (millsNext > 0) {
                                    int hoursNext = (int) (millsNext / (1000 * 60 * 60));
                                    int minsNext = (int) (millsNext / (1000 * 60)) % 60;
                                    timeLeft = hoursNext + ":" + minsNext;
                                    minutesLeft = hoursNext * 60 + minsNext;
                                    totalMinutes = hoursNext * 60 + minsNext;
                                    progress = (minutesLeft * 100) / totalMinutes;

                                }
                            }

                        }
                    }
                }

                Log.d("DateTime", "Waqt " + i + " Time: " + prevWaqtTime);

            }


            Log.d("DateTime", "Current Time: " + currentTime);
            Log.d("DateTime", "Upcoming Waqt: " + upcomingWaqt);
            Log.d("DateTime", "Upcoming Waqt Time: " + upcomingWaqtTime);
            Log.d("DateTime", "Upcoming Waqt Time Left: " + timeLeft);
            Log.d("DateTime", "Upcoming Waqt Minutes Left: " + minutesLeft);
            Log.d("DateTime", "Upcoming Waqt Total Minutes: " + totalMinutes);
            Log.d("DateTime", "Upcoming Waqt Progress: " + progress);


            currentWaqtTextView.setText(upcomingWaqt);
            progress = 100 - progress;
            waqtTimeLeftProgressBar.setProgress(progress);


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