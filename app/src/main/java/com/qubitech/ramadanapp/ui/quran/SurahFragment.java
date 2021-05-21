package com.qubitech.ramadanapp.ui.quran;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.quran.dataModel.SurahDataModel;
import com.qubitech.ramadanapp.ui.quran.listener.OnSwipeTouchListener;
import com.qubitech.ramadanapp.ui.quran.recyclerView.SurahAyahAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.qubitech.ramadanapp.staticdata.StaticData.currentFragment;
import static com.qubitech.ramadanapp.staticdata.StaticData.isMediaActive;
import static com.qubitech.ramadanapp.staticdata.StaticData.isMediaReset;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaPlayer;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaStatus;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaTitle;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaUrl;

public class SurahFragment extends Fragment {


    private QuranViewModel mViewModel;
    ArrayList<SurahDataModel> surahDataModels;
    SurahDataModel surahDataModel;
    SurahAyahAdapter surahAyahAdapter;
    String surahUrl = "http://api.alquran.cloud/v1/surah/1/editions/quran-simple,bn.bengali";
    String surahTitle="",surahArabicTitle="", surahNo="", surahRevelationType="",surahAyahs="",ayahNo="",ayahTextArabic="",ayahTextBangla="",ayahSajda="", audioUrl="";
    String [] surahs_bn,surahs_en;
    SharedPreferences localePreferences, surahPreferences;
    SharedPreferences.Editor surahPreferencesEditor;
    String surahPref = "Surah";

    RecyclerView recyclerView;
    TextView surahTitleView, surahNoView, surahAyahView, surahRevelationTypeView,mediaPlayerTitleView, mediaPlayerStatusView;
    ImageView mediaPlayerStartView, mainMediaPlayerStartView, mainMediaPlayerStopView;
    ProgressBar progressBar;
    CardView mediaCardView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surah, container, false);
        recyclerView = view.findViewById(R.id.surah_recyclerView);
        surahTitleView = view.findViewById(R.id.surah_title);
        surahNoView = view.findViewById(R.id.surah_no);
        surahAyahView = view.findViewById(R.id.surah_ayahs);
        surahRevelationTypeView = view.findViewById(R.id.surah_type);

        mediaPlayerStartView = view.findViewById(R.id.surah_media_start_imageView);
        progressBar = view.findViewById(R.id.surah_progressBar);
        mediaCardView = getActivity().findViewById(R.id.main_media_cardView);
        mediaPlayerTitleView = getActivity().findViewById(R.id.main_media_title);
        mediaPlayerStatusView = getActivity().findViewById(R.id.main_media_status);
        mainMediaPlayerStartView = getActivity().findViewById(R.id.main_media_start_imageView);
        mainMediaPlayerStopView = getActivity().findViewById(R.id.main_media_stop_imageView);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        surahs_bn = getResources().getStringArray(R.array.surahs_bn);
        surahs_en = getResources().getStringArray(R.array.surahs_en);

        surahTitleView.setText("");
        surahNoView.setText("");
        surahRevelationTypeView.setText("");
        surahAyahView.setText("");

        currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        updateMediaUi();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuranViewModel.class);

        if (getArguments() != null) {

            if(localePreferences.contains("Current_Language")) {
                String locale = localePreferences.getString("Current_Language", "");
                if (locale.equals("bn")) {
                    surahUrl ="http://api.alquran.cloud/v1/surah/"+getArguments().getInt("surah")+"/editions/quran-simple,bn.bengali";

                }
                else{
                    surahUrl ="http://api.alquran.cloud/v1/surah/"+getArguments().getInt("surah")+"/editions/quran-simple,en.sahih";

                }
            }

            Log.d("Response", "Surah URL: "+surahUrl);

            progressBar.setVisibility(View.VISIBLE);

            mediaPlayerStartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isMediaActive){
                        stopAudio();
                    }
                    else{
                        playAudio();

                    }
                }

            });

            new surahTask().execute();

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    @Override
    public void onStop() {
        super.onStop();
        currentFragment = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        currentFragment = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    private class surahTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {



            //API call for Quran Fragment
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(surahUrl)
                    .build();

            try {
                Response response = client.newCall(request).execute();


                //Getting Surah Metadata from API
                if(response.body() != null){

                    surahDataModels = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                    JSONObject jsonObject2 = jsonObject.getJSONArray("data").getJSONObject(1);

                    surahNo = jsonObject1.getString("number");
                    surahTitle = jsonObject1.getString("englishName");
                    surahArabicTitle = jsonObject1.getString("name");
                    surahRevelationType = jsonObject1.getString("revelationType");
                    surahAyahs = jsonObject1.getString("numberOfAyahs");

                    Log.d("Surah","SurahNo: "+surahNo+" SurahLength: "+surahNo.length());

                    if(surahNo.length() == 1) {
                        audioUrl = "https://server7.mp3quran.net/s_gmd/00"+surahNo+".mp3";
                    }
                    else if(surahNo.length() == 2){
                        audioUrl = "https://server7.mp3quran.net/s_gmd/0"+surahNo+".mp3";
                    }
                    else{
                        audioUrl = "https://server7.mp3quran.net/s_gmd/"+surahNo+".mp3";

                    }

                    if(localePreferences.contains("Current_Language")) {
                        String locale = localePreferences.getString("Current_Language", "");
                        if (locale.equals("bn")) {
                            surahTitle = translateDataToBn(surahTitle,surahs_bn,surahs_en);
                            surahNo = getResources().getString(R.string.surahNumber) + " "+banglaStringConverter(surahNo);
                            surahAyahs = getResources().getString(R.string.ayahNumber) + " "+ banglaStringConverter(surahAyahs);
                            surahRevelationType = getResources().getString(R.string.revelation) + " " +translateDataToBn(surahRevelationType,surahs_bn,surahs_en);
                        }
                    }


                    JSONArray jsonArray1 = jsonObject1.getJSONArray("ayahs");
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ayahs");

                    for(int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject3 = jsonArray1.getJSONObject(i);
                        JSONObject jsonObject4 = jsonArray2.getJSONObject(i);

                        ayahNo = jsonObject3.getString("numberInSurah");
                        ayahTextArabic = jsonObject3.getString("text");
                        ayahTextBangla = jsonObject4.getString("text");
                        boolean sajda = jsonObject3.getBoolean("sajda");
                        if(sajda == true){
                            ayahSajda = getResources().getString(R.string.sajda);
                        }
                        else{
                            ayahSajda = "";
                        }

                        if(localePreferences.contains("Current_Language")) {
                            String locale = localePreferences.getString("Current_Language", "");
                            if (locale.equals("bn")) {
                                ayahNo = banglaStringConverter(String.valueOf(ayahNo));
                            }
                        }
                        surahDataModel = new SurahDataModel(surahNo, ayahNo, ayahTextArabic, ayahTextBangla, ayahSajda);
                        surahDataModels.add(surahDataModel);


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
            progressBar.setVisibility(View.GONE);

            surahTitleView.setText(surahTitle +"    |    "+ surahArabicTitle);
            surahNoView.setText(surahNo);
            surahRevelationTypeView.setText(surahRevelationType);
            surahAyahView.setText(surahAyahs);

            if(surahDataModels != null) {
                //Update the UI on UI thread
                surahAyahAdapter = new SurahAyahAdapter(getActivity(), surahDataModels);
                recyclerView.setAdapter(surahAyahAdapter);
            }
        }

    }

    private void prepareMedia(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(mediaUrl);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMediaControls(int drawable){
        mediaPlayerStartView.setImageResource(drawable);
    }

    private void updateSurahPreferences(){
        surahPreferences = getActivity().getSharedPreferences(surahPref, Context.MODE_PRIVATE);
        surahPreferencesEditor = surahPreferences.edit();

        surahPreferencesEditor.putString("mediaTitle", mediaTitle);
        surahPreferencesEditor.putString("mediaStatus", mediaStatus);
        surahPreferencesEditor.putString("mediaUrl", mediaUrl);
        surahPreferencesEditor.putBoolean("isMediaActive", isMediaActive);
        surahPreferencesEditor.putBoolean("isMediaReset", isMediaReset);

        surahPreferencesEditor.apply();
    }

    private void updateMediaUi(){
        if(isMediaActive) {
            mediaTitle = surahTitle;
            mediaStatus = getResources().getString(R.string.paused);
            mediaCardView.setVisibility(View.VISIBLE);
            mediaPlayerStartView.setImageResource(R.drawable.stop);
            mainMediaPlayerStartView.setImageResource(R.drawable.pause);

            if(mediaPlayer != null){
                if(!mediaPlayer.isPlaying()){
                    prepareMedia();
                }
            }

        }
        else{
            mediaPlayerStartView.setImageResource(R.drawable.play);
            mainMediaPlayerStartView.setImageResource(R.drawable.play);
            prepareMedia();

        }
    }

    private void playAudio(){
        mediaPlayerStartView.setImageResource(R.drawable.stop);
        mainMediaPlayerStartView.setImageResource(R.drawable.pause);
        mediaStatus = getResources().getString(R.string.now_playing);
        isMediaActive=true;

        mediaTitle = surahTitle;
        mediaStatus = getResources().getString(R.string.now_playing);
        mediaUrl = audioUrl;

        updateSurahPreferences();

        if(isMediaReset) {
            prepareMedia();
            mediaPlayer.start();
            isMediaReset=false;
        }
        else{
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }

        }

        mediaCardView.setVisibility(View.VISIBLE);
        mediaPlayerTitleView.setText(mediaTitle);
        mediaPlayerStatusView.setText(mediaStatus);
    }

    private void stopAudio(){
        mediaPlayerStartView.setImageResource(R.drawable.play);
        mainMediaPlayerStartView.setImageResource(R.drawable.play);
        mediaStatus = getResources().getString(R.string.ready_to_play);
        mediaPlayerStatusView.setText(mediaStatus);

        isMediaActive=false;
        isMediaReset=true;

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        else if(mediaPlayer != null){
            mediaPlayer.reset();
        }
    }


    private String translateDataToBn(String keyword, String[] array_bn, String[] array_en){

        //Hashmap for Locale translation
        HashMap<String, String> hashMap = new HashMap<String, String>();

        for(int i=0;i<array_en.length;i++) {
            hashMap.put(array_en[i], array_bn[i]);
        }

        //Finding the English Value using Bangla Keywords from each entrySet.
        for (Map.Entry<String, String> entry :
                hashMap.entrySet()) {
            if (entry.getKey().equals(keyword)) {
                keyword = entry.getValue();
                break;
            }

        }

        Log.d("Surah","Keyword Translated: "+keyword);

        return keyword;
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

}