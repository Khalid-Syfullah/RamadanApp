package com.qubitech.ramadanapp.ui.calendar;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.github.eltohamy.materialhijricalendarview.OnDateSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.qubitech.ramadanapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarFragment extends Fragment {


    MaterialHijriCalendarView hijriCalendarView;
    CalendarView gregorianCalendarView;
    TabLayout tabLayout;
    String calendarUrl = "",hijriDate = "",selectedDate="";
    SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        hijriCalendarView = view.findViewById(R.id.hijri_calendarView);
        gregorianCalendarView = view.findViewById(R.id.gregorian_calendarView);
        tabLayout = view.findViewById(R.id.calendar_tabLayout);

        gregorianCalendarView.setVisibility(View.GONE);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        hijriCalendarView.setDateSelected(Calendar.getInstance().getTime(),true);
        selectedDate = hijriCalendarView.getSelectedDate().toString();
        selectedDate = selectedDate.replace("CalendarDay{","");
        selectedDate = selectedDate.replace("}","");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new CalendarTask().execute();

        hijriCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialHijriCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date.toString();
                selectedDate = selectedDate.replace("CalendarDay{","");
                selectedDate = selectedDate.replace("}","");
                //currentDateView.setText(selectedDate);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    hijriCalendarView.setVisibility(View.VISIBLE);
                    gregorianCalendarView.setVisibility(View.GONE);
                }
                else if(tabLayout.getSelectedTabPosition() == 1){
                    hijriCalendarView.setVisibility(View.GONE);
                    gregorianCalendarView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class CalendarTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            calendarUrl = "http://api.aladhan.com/v1/gToH?date="+simpleDateFormat.format(Calendar.getInstance().getTime());

            //API call for Calendar Fragment
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(calendarUrl)
                    .build();

            try {
                Response response = client.newCall(request).execute();


                //Getting Hijri date from API
                if(response.body() != null){

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("hijri");
                    hijriDate = jsonObject2.getString("date");

                    Log.d("Calendar","Hijri Date: "+hijriDate);

                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



        }

    }

}