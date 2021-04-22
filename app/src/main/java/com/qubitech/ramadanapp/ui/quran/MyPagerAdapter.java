package com.qubitech.ramadanapp.ui.quran;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qubitech.ramadanapp.ui.calibrate.CalibrateFragment;
import com.qubitech.ramadanapp.ui.mosques.MosquesFragment;
import com.qubitech.ramadanapp.ui.tasbih.TasbihFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MosquesFragment.newInstance();
            case 1:
                return TasbihFragment.newInstance();
            case 2:
                return CalibrateFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
