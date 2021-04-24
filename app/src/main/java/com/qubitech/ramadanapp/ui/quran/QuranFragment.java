package com.qubitech.ramadanapp.ui.quran;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.mosques.MosquesFragment;
import com.qubitech.ramadanapp.ui.tasbih.TasbihFragment;

public class QuranFragment extends Fragment {


    private QuranViewModel mViewModel;

    public static QuranFragment newInstance() {
        return new QuranFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quran, container, false);
        ViewPager viewPager = view.findViewById(R.id.viewpager2);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.view_pager_tab);
        tabLayout.setupWithViewPager(viewPager);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuranViewModel.class);

    }

}