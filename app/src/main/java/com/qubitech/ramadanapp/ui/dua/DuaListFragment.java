package com.qubitech.ramadanapp.ui.dua;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.dua.dataModel.DuaDataModel;
import com.qubitech.ramadanapp.ui.dua.recyclerView.DuaAdapter;
import com.qubitech.ramadanapp.ui.quran.dataModel.SurahDataModel;
import com.qubitech.ramadanapp.ui.quran.recyclerView.SurahAyahAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DuaListFragment extends Fragment {


    String arg;
    CardView duaListCardView;
    RecyclerView duaRecyclerView;
    TextView duaListTitle;
    ImageView closeBtn, backBtn;
    ProgressBar duaProgressBar;
    String duaUrl;
    String duaType="", duaTitle="", duaBody="";
    ArrayList<DuaDataModel> duaDataModels;
    DuaDataModel duaDataModel;
    DuaAdapter duaAdapter;

    public DuaListFragment() {
        // Required empty public constructor
    }

    public static DuaListFragment newInstance() {
        DuaListFragment fragment = new DuaListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dua_list, container, false);

        duaListCardView = view.findViewById(R.id.dualist_cardView);
        duaListTitle = view.findViewById(R.id.dualist_title);
        duaProgressBar = view.findViewById(R.id.dualist_progressBar);
        backBtn = view.findViewById(R.id.dualist_backBtn);
        closeBtn = view.findViewById(R.id.dualist_closeBtn);
        duaRecyclerView = view.findViewById(R.id.dualist_dua_recyclerView);
       

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        duaRecyclerView.setVisibility(View.VISIBLE);
        duaRecyclerView.setLayoutManager(linearLayoutManager);

        if (getArguments() != null) {
            arg = getArguments().getString("fragment");

            if(arg.equals("salah")){

                duaUrl = "https://api.sunnah.com/v1/collections/bukhari/books/8/hadiths";
                duaListTitle.setText(getResources().getString(R.string.dua_salah_title));
            }
            else if(arg.equals("siyam")){

                duaUrl = "https://api.sunnah.com/v1/collections/bukhari/books/30/hadiths";
                duaListTitle.setText(getResources().getString(R.string.dua_fasting_title));

            }
            else if(arg.equals("misc")){

                duaUrl = "https://api.sunnah.com/v1/collections/riyadussalihin/books/16/hadiths";
                duaListTitle.setText(getResources().getString(R.string.dua_misc_title));
            }
            else if(arg.equals("zikir")){

                duaUrl = "https://api.sunnah.com/v1/collections/riyadussalihin/books/15/hadiths";
                duaListTitle.setText(getResources().getString(R.string.dua_zikir_title));
            }

            new duaTask().execute();

        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dualist_to_navigation_dua);

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB(duaListCardView);
            }
        });
        return view;

    }

    private class duaTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            duaProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try{


            //API call for Quran Fragment
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(duaUrl)
                    .addHeader("X-API-Key","SqD712P3E82xnwOAEOkGd5JZH8s9wRR24TqNFzjk")
                    .build();

                Response response = client.newCall(request).execute();


                //Getting Surah Metadata from API
                if(response.body() != null){

                    duaDataModels = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject3 = jsonObject2.getJSONArray("hadith").getJSONObject(0);

                        duaType = arg;
                        duaTitle = jsonObject3.getString("chapterTitle");
                        duaBody = jsonObject3.getString("body");
                        duaBody = duaBody.replaceAll("<p>","");
                        duaBody = duaBody.replaceAll("</p>","");
                        duaBody = duaBody.replaceAll("<br/>","");
                        duaBody = duaBody.replaceAll("<b>","");
                        duaBody = duaBody.replaceAll("</b>","");
                        duaBody = duaBody.replaceAll("[\\t\\n\\r]+","");

                        duaDataModel = new DuaDataModel(duaType, duaTitle, duaBody);
                        duaDataModels.add(duaDataModel);

                        Log.d("Response","DuaType: "+duaType+" DuaTitle: "+duaTitle+" DuaBody"+duaBody);

                    }

                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
                duaProgressBar.setVisibility(View.GONE);

            }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            duaProgressBar.setVisibility(View.GONE);

            if(duaDataModels != null) {
                //Update the UI on UI thread

                duaAdapter = new DuaAdapter(getActivity(), duaDataModels);
                duaRecyclerView.setAdapter(duaAdapter);
            }
        }

    }


    private void hideFAB(View view) {

        int cx = view.getWidth();
        int cy = view.getHeight() ;
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, 0, initialRadius, 0);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dualist_to_navigation_dashboard);
            }
        });
        anim.start();

    }
}