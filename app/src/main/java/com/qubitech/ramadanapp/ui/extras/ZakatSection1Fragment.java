package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

public class ZakatSection1Fragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_section_1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public static ZakatSection1Fragment newInstance(){

        ZakatSection1Fragment zakatSection1Fragment = new ZakatSection1Fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 1);
        zakatSection1Fragment.setArguments(args);
        return zakatSection1Fragment;

    }
}