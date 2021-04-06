package com.qubitech.ramadanapp.ui.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.qubitech.ramadanapp.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    public String prayerUrl = "https://api.pray.zone/v2/times/today.json?city=Rajshahi";
    public TextView sunriseTextView, sunsetTextView, midnightTextView, fajrTextView, dhuhrTextView, asrTextView, maghribTextView, ishaTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sunriseTextView = root.findViewById(R.id.textView2);
        sunsetTextView = root.findViewById(R.id.textView3);
        midnightTextView = root.findViewById(R.id.textView4);

        fajrTextView = root.findViewById(R.id.textView6);
        dhuhrTextView = root.findViewById(R.id.textView8);
        asrTextView = root.findViewById(R.id.textView10);
        maghribTextView = root.findViewById(R.id.textView12);
        ishaTextView = root.findViewById(R.id.textView14);


        new apiData().execute();

        return root;
    }

    private class apiData extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            URL url= null;
            String response = null;
            String line=null;

            try {
                url = new URL(prayerUrl);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();

                try{
                    while ((line=reader.readLine()) !=null){
                        stringBuilder.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try{
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                response = stringBuilder.toString();

                Log.d("Response","Response: "+response);


                try {
                    if(response != null) {

                        sunriseTextView.setText("0");
                        sunsetTextView.setText("0");
                        midnightTextView.setText("0");
                        fajrTextView.setText("0");
                        dhuhrTextView.setText("0");
                        asrTextView.setText("0");
                        maghribTextView.setText("0");
                        ishaTextView.setText("0");

                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("results");
                        JSONObject jsonObject2 = jsonObject1.getJSONArray("datetime").getJSONObject(0);

                        Log.d("Response", "Results: " + jsonObject2.getString("Sunrise"));


                    }

                }catch (Exception e) {

                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
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