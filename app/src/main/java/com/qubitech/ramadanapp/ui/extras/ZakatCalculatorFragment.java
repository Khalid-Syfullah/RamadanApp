package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.mosques.MosquesFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ZakatCalculatorFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static ZakatCalculatorPagerAdapter zakatCalculatorPagerAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_calculator, container, false);
        viewPager = view.findViewById(R.id.zakat_calculator_viewpager);
        tabLayout = view.findViewById(R.id.zakat_calculator_tablayout);


        zakatCalculatorPagerAdapter = new ZakatCalculatorPagerAdapter(getActivity(),getActivity().getSupportFragmentManager());

        viewPager.setAdapter(zakatCalculatorPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public static ZakatCalculatorFragment newInstance(){

        ZakatCalculatorFragment zakatCalculatorFragment = new ZakatCalculatorFragment();
        return zakatCalculatorFragment;

    }

}