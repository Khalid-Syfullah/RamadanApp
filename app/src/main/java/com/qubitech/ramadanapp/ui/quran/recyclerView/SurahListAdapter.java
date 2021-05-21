package com.qubitech.ramadanapp.ui.quran.recyclerView;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.quran.dataModel.SurahDataModel;

import java.util.ArrayList;


class SurahListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView surahNumber, surahTitle, surahArabicTitle, ayah;
    ImageView surahImage;
    ConstraintLayout surahView;
    Activity activity;

    public SurahListViewHolder(@NonNull View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;

        surahImage = itemView.findViewById(R.id.recyclerView_quran_list_imageView);
        surahNumber = itemView.findViewById(R.id.recyclerView_quran_list_no);
        surahTitle = itemView.findViewById(R.id.recyclerView_quran_list_title);
        surahArabicTitle = itemView.findViewById(R.id.recyclerView_quran_list_title_arabic);
        ayah = itemView.findViewById(R.id.recyclerView_quran_surah_sajda);
        surahView = itemView.findViewById(R.id.recyclerView_quran_list_constraintLayout);

        surahView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.recyclerView_quran_list_constraintLayout){
            Bundle bundle = new Bundle();
            bundle.putInt("surah",getAdapterPosition()+1);
            Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.action_navigation_quran_to_navigation_surah,bundle);

        }
    }
}


public class SurahListAdapter extends RecyclerView.Adapter<SurahListViewHolder>{

    ArrayList<SurahDataModel> surahDataModels, surahDataModelsFiltered;
    SurahDataModel surahDataModel;
    Activity activity;


    public SurahListAdapter(Activity activity, ArrayList<SurahDataModel> surahDataModels) {
        this.activity = activity;
        this.surahDataModelsFiltered = surahDataModels;
    }

    @NonNull
    @Override
    public SurahListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.recyclerview_quran_list,parent,false);
        SurahListViewHolder surahListViewHolder = new SurahListViewHolder(view,activity);
        return surahListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurahListViewHolder holder, int position) {

        surahDataModel = surahDataModelsFiltered.get(position);

        holder.surahNumber.setText(surahDataModel.getSurahNumber());
        holder.surahTitle.setText(surahDataModel.getSurahTitle());
        holder.surahArabicTitle.setText(surahDataModel.getSurahArabicTitle());
        holder.ayah.setText(surahDataModel.getAyah());

    }

    @Override
    public int getItemCount() {
        return surahDataModelsFiltered.size();
    }

    public void surahListFilter(String query) {

        surahDataModels = new ArrayList<>();
        surahDataModels.addAll(surahDataModelsFiltered);
        Log.d("Response", "SurahDataModels : "+surahDataModels.size());
        Log.d("Response", "SurahDataModelsFiltered : "+surahDataModelsFiltered.size());

        surahDataModelsFiltered = new ArrayList<>();
        if(query.isEmpty()){
            surahDataModelsFiltered.addAll(surahDataModels);
        }
        else{

            query = query.toLowerCase();
            for(SurahDataModel surahDataModel: surahDataModels){
                if(surahDataModel.getSurahTitle().toLowerCase().contains(query)){
                    surahDataModelsFiltered.add(surahDataModel);
                }
            }

            Log.d("Response", "SurahDataModels : "+surahDataModels.size());
            Log.d("Response", "SurahDataModelsFiltered : "+surahDataModelsFiltered.size());
        }
        notifyDataSetChanged();
    }
}

