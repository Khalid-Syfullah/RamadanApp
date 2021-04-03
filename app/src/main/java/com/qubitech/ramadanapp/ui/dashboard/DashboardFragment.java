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

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    public String twitterUrl = "https://api.twitter.com/1.1/users/show.json?screen_name=mkbhd";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        return root;
    }

    private class pageData extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Twitter Page
            HttpHandler handler=new HttpHandler();
            String jsonStr = handler.makeServiceCall(twitterUrl,"twitter");

            if(jsonStr != null){

                try {
                    JSONObject jsonObject=new JSONObject(jsonStr);
                    Log.d("Response","Page Name: "+jsonObject.getString("screen_name"));



                }catch (Exception e) {

                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}