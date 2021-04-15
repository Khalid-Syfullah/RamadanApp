package com.qubitech.ramadanapp.ui.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.qubitech.ramadanapp.ComingSoonActivity;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.tasbih.TasbihFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    Double latitude,longitude;
    Intent locationIntent;

    private DashboardViewModel dashboardViewModel;
    public String prayerUrl = "";
    public TextView sunriseTextView, sunsetTextView, midnightTextView, fajrTextView, dhuhrTextView, asrTextView, maghribTextView, ishaTextView;
    public String sunrise="",sunset="",midnight="",fajr="",dhuhr="",asr="",maghrib="",isha="";
    SharedPreferences localePreferences;


    private Compass compass;
    private ImageView arrowView;
    private TextView sotwLabel;
    private CardView cardView4,cardView5,cardView6,cardView7;
    private float currentAzimuth;

    private static final int[] sides = {0, 45, 90, 135, 180, 225, 270, 315, 360};
    private static String[] names = null;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sunriseTextView = root.findViewById(R.id.textView16);
        sunsetTextView = root.findViewById(R.id.textView17);
        midnightTextView = root.findViewById(R.id.textView18);

        fajrTextView = root.findViewById(R.id.textView6);
        dhuhrTextView = root.findViewById(R.id.textView8);
        asrTextView = root.findViewById(R.id.textView10);
        maghribTextView = root.findViewById(R.id.textView12);
        ishaTextView = root.findViewById(R.id.textView14);

        cardView4 = root.findViewById(R.id.cardView4);
        cardView5 = root.findViewById(R.id.cardView5);
        cardView6 = root.findViewById(R.id.cardView6);
        cardView7 = root.findViewById(R.id.cardView7);


        sunriseTextView.setText("");
        sunsetTextView.setText("");
        midnightTextView.setText("");
        fajrTextView.setText("");
        dhuhrTextView.setText("");
        asrTextView.setText("");
        maghribTextView.setText("");
        ishaTextView.setText("");

        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);

        arrowView = root.findViewById(R.id.imageView25);
        sotwLabel = root.findViewById(R.id.dashboard_quibla_sotw);

        initLocalizedNames(getActivity().getApplicationContext());
        setupCompass();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        try {
            Date waqtDate = simpleDateFormat.parse("18:45:00");
            Date currentDate = Calendar.getInstance().getTime();

            Long time = currentDate.getTime()-waqtDate.getTime();

            int timeInSeconds = (int) (time / 1000);
            int hours, minutes, seconds;
            hours = timeInSeconds / 3600;
            timeInSeconds = timeInSeconds - (hours * 3600);
            minutes = timeInSeconds / 60;
            timeInSeconds = timeInSeconds - (minutes * 60);
            seconds = timeInSeconds;


            //String diffTime = (hours<10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds) + " h";


            Log.d("DateTime",""+hours+":"+minutes+":"+seconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
        cardView7.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.cardView4:
            case R.id.cardView5:
            case R.id.textView4:
            case R.id.imageView4:
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_tasbih);
                break;
            case R.id.cardView6:
            case R.id.cardView7:
                Intent intent = new Intent(getActivity().getApplicationContext(), ComingSoonActivity.class);
                startActivity(intent);
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
                    Log.d("Response", "Results: " + jsonObject3.getString("Sunrise"));

                    sunrise = jsonObject3.getString("Sunrise");
                    sunset = jsonObject3.getString("Sunset");
                    midnight = jsonObject3.getString("Midnight");
                    fajr = jsonObject3.getString("Fajr");
                    dhuhr = jsonObject3.getString("Dhuhr");
                    asr = jsonObject3.getString("Asr");
                    maghrib = jsonObject3.getString("Maghrib");
                    isha = jsonObject3.getString("Isha");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(localePreferences.contains("Current_Language")){
                String locale = localePreferences.getString("Current_Language","");
                if(locale.equals("bn")){

//                    banglaTimeFormatter(sunrise, sunriseTextView);
//                    banglaTimeFormatter(sunset, sunsetTextView);
//                    banglaTimeFormatter(midnight, midnightTextView);
                    banglaTimeFormatter(fajr, fajrTextView);
                    banglaTimeFormatter(dhuhr, dhuhrTextView);
                    banglaTimeFormatter(asr, asrTextView);
                    banglaTimeFormatter(maghrib, maghribTextView);
                    banglaTimeFormatter(isha, ishaTextView);

                }
                else if(locale.equals("en")){

//                    englishTimeFormatter(sunrise, sunriseTextView);
//                    englishTimeFormatter(sunset, sunsetTextView);
//                    englishTimeFormatter(midnight, midnightTextView);
                    englishTimeFormatter(fajr, fajrTextView);
                    englishTimeFormatter(dhuhr, dhuhrTextView);
                    englishTimeFormatter(asr, asrTextView);
                    englishTimeFormatter(maghrib, maghribTextView);
                    englishTimeFormatter(isha, ishaTextView);
                }
            }




        }

    }

    public String banglaTimeConverter(String eng){

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

        String hoursBn = banglaTimeConverter(units[0]);
        String minutesBn = banglaTimeConverter(units[1]);

        if(hours >= 1 && hours <= 12){
            view.setText(hoursBn+":"+minutesBn+" মিনিট");
        }
        else if (hours == 0){
            view.setText(hoursBn+":"+minutesBn+" মিনিট");
        }
        else if(hours > 12){

            view.setText(banglaTimeConverter(Integer.toString(hours-12))+":"+minutesBn+" মিনিট");
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


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Locale aLocale = new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("US").build();
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), aLocale);

            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            prayerUrl = "https://api.pray.zone/v2/times/today.json?latitude="+latitude+"&longitude="+longitude;
            new apiData().execute();

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                Log.d("Location",addresses.get(0).getAdminArea()+"");
                Log.d("Location",stateName+"");
                Log.d("Location",cityName+"");
                Log.d("Location",addresses+"");




            } catch (IOException e1) {
                e1.printStackTrace();
            }


            Log.d("Location",latitude+"");
            Log.d("Location",longitude+"");


        }
    };


}