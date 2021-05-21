package com.qubitech.ramadanapp.ui.quran.recyclerView;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.quran.dataModel.SurahDataModel;

import java.util.ArrayList;


class SurahAyahViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView ayahNo, ayahTextArabic, ayahtextBangla, ayahSajda;
    ImageView ayahImage;
    ConstraintLayout ayahView;

    public SurahAyahViewHolder(@NonNull View itemView) {
        super(itemView);

        ayahNo = itemView.findViewById(R.id.recyclerView_quran_surah_ayah_no);
        ayahTextArabic = itemView.findViewById(R.id.recyclerView_quran_surah_ayah_arabic);
        ayahtextBangla = itemView.findViewById(R.id.recyclerView_quran_surah_ayah_bangla);
        ayahSajda = itemView.findViewById(R.id.recyclerView_quran_surah_ayah_sajda);
        ayahView = itemView.findViewById(R.id.recyclerView_quran_surah_constraintLayout);

        ayahView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.recyclerView_quran_surah_constraintLayout){

        }
    }
}


public class SurahAyahAdapter extends RecyclerView.Adapter<SurahAyahViewHolder>{

    ArrayList<SurahDataModel> surahDataModels;
    Activity activity;

    public SurahAyahAdapter(Activity activity, ArrayList<SurahDataModel> surahDataModels) {
        this.activity = activity;
        this.surahDataModels = surahDataModels;
    }

    @NonNull
    @Override
    public SurahAyahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.recyclerview_quran_surah,parent,false);
        SurahAyahViewHolder SurahAyahViewHolder = new SurahAyahViewHolder(view);
        return SurahAyahViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurahAyahViewHolder holder, int position) {

        SurahDataModel surahDataModel = surahDataModels.get(position);

        holder.ayahNo.setText(surahDataModel.getAyah());
        holder.ayahTextArabic.setText(surahDataModel.getAyahTextArabic());
        holder.ayahtextBangla.setText(surahDataModel.getAyahTextBangla());
        holder.ayahSajda.setText(surahDataModel.getSajda());

    }

    @Override
    public int getItemCount() {
        return surahDataModels.size();
    }
}

