package com.qubitech.ramadanapp.ui.extras.recyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.extras.dataModel.AllahNamesDataModel;

import java.util.ArrayList;

class AllahNamesViewHolder extends RecyclerView.ViewHolder{

    TextView no, mainName, arabicName, translatedName;
    ConstraintLayout allahNamesConstraintLayout;
    ArrayList<AllahNamesDataModel> allahNamesDataModels;

    public AllahNamesViewHolder(@NonNull View itemView, ArrayList<AllahNamesDataModel> allahNamesDataModels) {
        super(itemView);
        this.allahNamesDataModels = allahNamesDataModels;

        no = itemView.findViewById(R.id.recyclerView_allah_names_no);
        mainName = itemView.findViewById(R.id.recyclerView_allah_names_main);
        arabicName = itemView.findViewById(R.id.recyclerView_allah_names_arabic);
        translatedName = itemView.findViewById(R.id.recyclerView_allah_names_translation);
        allahNamesConstraintLayout = itemView.findViewById(R.id.recyclerView_allah_names_constraintLayout);
    }

}

public class AllahNamesAdapter extends RecyclerView.Adapter<AllahNamesViewHolder>{
    
    ArrayList<AllahNamesDataModel> allahNamesDataModels;
    Activity activity;

    public AllahNamesAdapter(Activity activity, ArrayList<AllahNamesDataModel> allahNamesDataModels) {
        
        this.activity = activity;
        this.allahNamesDataModels = allahNamesDataModels;
        
    }

    @NonNull
    @Override
    public AllahNamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.recyclerview_allah_names, parent, false);
        AllahNamesViewHolder allahNamesViewHolder = new AllahNamesViewHolder(view,allahNamesDataModels);
        return allahNamesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllahNamesViewHolder holder, int position) {

        AllahNamesDataModel allahNamesDataModel = allahNamesDataModels.get(position);

        int number = position+1;
        holder.no.setText(number+"");
        holder.mainName.setText(allahNamesDataModel.getMainName());
        holder.arabicName.setText(allahNamesDataModel.getArabicName());
        holder.translatedName.setText(allahNamesDataModel.getTranslatedName());

    }

    @Override
    public int getItemCount() {
        return allahNamesDataModels.size();
    }
}