package com.qubitech.ramadanapp.ui.dua.recyclerView;

import android.app.Activity;
import android.content.Context;
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
import com.qubitech.ramadanapp.ui.dua.dataModel.DuaDataModel;

import java.util.ArrayList;

class DuaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView duaTitle;
    ConstraintLayout duaConstraintLayout;
    Activity activity;
    ArrayList<DuaDataModel> duaDataModels;

    public DuaViewHolder(@NonNull View itemView, Activity activity, ArrayList<DuaDataModel> duaDataModels) {
        super(itemView);
        this.activity = activity;
        this.duaDataModels = duaDataModels;
        duaTitle = itemView.findViewById(R.id.recyclerView_dua_list_title);
        duaConstraintLayout = itemView.findViewById(R.id.recyclerView_dua_list_constraintLayout);
        duaConstraintLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        DuaDataModel duaDataModel = duaDataModels.get(getAdapterPosition());
        Bundle bundle = new Bundle();
        bundle.putString("duaType",duaDataModel.getDuaType());
        bundle.putString("duaTitle",duaDataModel.getDuaTitle());
        bundle.putString("duaBody",duaDataModel.getDuaBody());

        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.action_navigation_dualist_to_navigation_duadetails,bundle);

    }
}

public class DuaAdapter extends RecyclerView.Adapter<DuaViewHolder>{
    
    Activity activity;
    ArrayList<DuaDataModel> duaDataModels;
    
    public DuaAdapter(Activity activity, ArrayList<DuaDataModel> duaDataModels) {
        
        this.activity = activity;
        this.duaDataModels = duaDataModels;
        
    }

    @NonNull
    @Override
    public DuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.recyclerview_dua_list, parent, false);
        DuaViewHolder duaViewHolder = new DuaViewHolder(view,activity,duaDataModels);
        return duaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DuaViewHolder holder, int position) {

        DuaDataModel duaDataModel = duaDataModels.get(position);
        holder.duaTitle.setText(duaDataModel.getDuaTitle());

    }

    @Override
    public int getItemCount() {
        return duaDataModels.size();
    }
}