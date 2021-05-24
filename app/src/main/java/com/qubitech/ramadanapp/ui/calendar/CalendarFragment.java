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
import android.widget.TextView;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.github.eltohamy.materialhijricalendarview.OnDateSelectedListener;
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


    MaterialHijriCalendarView materialhijricalendarview;
    TextView currentDateView;
    String calendarUrl = "",hijriDate = "",selectedDate="";
    SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        materialhijricalendarview = view.findViewById(R.id.hijri_calendarView);
        currentDateView = view.findViewById(R.id.calendar_currentDate);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        materialhijricalendarview.setDateSelected(Calendar.getInstance().getTime(),true);
        selectedDate = materialhijricalendarview.getSelectedDate().toString();
        selectedDate = selectedDate.replace("CalendarDay{","");
        selectedDate = selectedDate.replace("}","");
        currentDateView.setText(selectedDate);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new CalendarTask().execute();

        materialhijricalendarview.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialHijriCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date.toString();
                selectedDate = selectedDate.replace("CalendarDay{","");
                selectedDate = selectedDate.replace("}","");
                currentDateView.setText(selectedDate);
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