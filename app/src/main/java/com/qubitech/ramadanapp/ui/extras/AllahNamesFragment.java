package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.extras.dataModel.AllahNamesDataModel;
import com.qubitech.ramadanapp.ui.extras.recyclerView.AllahNamesAdapter;
import com.qubitech.ramadanapp.ui.quran.dataModel.SurahDataModel;
import com.qubitech.ramadanapp.ui.quran.recyclerView.SurahAyahAdapter;
import com.qubitech.ramadanapp.ui.quran.recyclerView.SurahListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllahNamesFragment extends Fragment {


    RecyclerView allahNamesRecyclerView;
    String [] names,names_arabic,names_translation;

    ArrayList<AllahNamesDataModel> allahNamesDataModels;
    AllahNamesDataModel allahNamesDataModel;
    AllahNamesAdapter allahNamesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allah_names, container, false);

        allahNamesRecyclerView = view.findViewById(R.id.allah_names_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        allahNamesRecyclerView.setLayoutManager(linearLayoutManager);

        names = getResources().getStringArray(R.array.names);
        names_arabic = getResources().getStringArray(R.array.names_arabic);
        names_translation = getResources().getStringArray(R.array.names_translation);

        allahNamesDataModels = new ArrayList<>();

        for(int i=0;i<names.length;i++){
            allahNamesDataModel = new AllahNamesDataModel(names[i],names_arabic[i],names_translation[i]);
            allahNamesDataModels.add(allahNamesDataModel);
        }
        allahNamesAdapter = new AllahNamesAdapter(getActivity(), allahNamesDataModels);
        allahNamesRecyclerView.setAdapter(allahNamesAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}