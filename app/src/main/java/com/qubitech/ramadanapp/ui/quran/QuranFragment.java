package com.qubitech.ramadanapp.ui.quran;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.quran.dataModel.SurahDataModel;
import com.qubitech.ramadanapp.ui.quran.recyclerView.SurahListAdapter;

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

public class QuranFragment extends Fragment {


    private QuranViewModel mViewModel;
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<SurahDataModel> surahDataModels;
    SurahDataModel surahDataModel;
    SurahListAdapter surahListAdapter;
    String surahUrl = "http://api.alquran.cloud/v1/meta",surahTitle="",surahArabicTitle="", surahNo="", ayah="";
    SharedPreferences localePreferences;
    String [] surahs_bn,surahs_en;
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quran, container, false);
        searchView = view.findViewById(R.id.quran_searchView);
        recyclerView = view.findViewById(R.id.quran_recyclerView);
        progressBar = view.findViewById(R.id.quran_progressBar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        surahs_bn = getResources().getStringArray(R.array.surahs_bn);
        surahs_en = getResources().getStringArray(R.array.surahs_en);

        progressBar.setVisibility(View.VISIBLE);

        surahDataModels = new ArrayList<>();
        surahListAdapter = new SurahListAdapter(getActivity(), surahDataModels);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                surahListAdapter = new SurahListAdapter(getActivity(),surahDataModels);
                surahListAdapter.surahListFilter(query);
                recyclerView.setAdapter(surahListAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                surahListAdapter = new SurahListAdapter(getActivity(),surahDataModels);
                surahListAdapter.surahListFilter(newText);
                recyclerView.setAdapter(surahListAdapter);

                return false;
            }
        });

        new surahTask().execute();

//        ViewPager viewPager = view.findViewById(R.id.viewpager2);
//
//        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
//        viewPager.setAdapter(myPagerAdapter);
//
//        TabLayout tabLayout = view.findViewById(R.id.view_pager_tab);
//        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuranViewModel.class);

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

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("surahs");
                    JSONArray jsonArray = jsonObject2.getJSONArray("references");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                        surahNo = jsonObject3.getString("number");
                        surahTitle = jsonObject3.getString("englishName");
                        surahArabicTitle = jsonObject3.getString("name");
                        surahArabicTitle = surahArabicTitle.replace("سُورَةُ ","");
                        ayah = getResources().getString(R.string.ayahNumber) + " "+ jsonObject3.getString("numberOfAyahs");

                        if(localePreferences.contains("Current_Language")) {
                            String locale = localePreferences.getString("Current_Language", "");
                            if (locale.equals("bn")) {
                                surahTitle = translateDataToBn(surahTitle,surahs_bn,surahs_en);
                                surahNo = banglaStringConverter(surahNo);
                                ayah = getResources().getString(R.string.ayahNumber) + " "+ banglaStringConverter(ayah);
                            }
                        }
                        surahDataModel = new SurahDataModel(surahNo, surahTitle, surahArabicTitle, ayah);
                        surahDataModels.add(surahDataModel);

                        Log.d("Response", "Surah #"+i+ ": " + jsonObject3.getString("englishName"));

                    }




                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);

            if(surahDataModels != null) {
                //Update the UI on UI thread
                surahListAdapter = new SurahListAdapter(getActivity(), surahDataModels);
                recyclerView.setAdapter(surahListAdapter);
            }
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