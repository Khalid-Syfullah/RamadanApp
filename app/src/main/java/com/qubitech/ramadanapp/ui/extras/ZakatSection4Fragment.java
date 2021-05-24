package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

public class ZakatSection4Fragment extends Fragment {

    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_section_4, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public static ZakatSection4Fragment newInstance(){

        ZakatSection4Fragment zakatSection4Fragment = new ZakatSection4Fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 4);
        zakatSection4Fragment.setArguments(args);
        return zakatSection4Fragment;

    }
}