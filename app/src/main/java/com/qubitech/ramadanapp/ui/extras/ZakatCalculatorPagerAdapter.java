package com.qubitech.ramadanapp.ui.extras;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qubitech.ramadanapp.ui.calibrate.CalibrateFragment;
import com.qubitech.ramadanapp.ui.mosques.MosquesFragment;
import com.qubitech.ramadanapp.ui.tasbih.TasbihFragment;

public class ZakatCalculatorPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;

    public ZakatCalculatorPagerAdapter(FragmentManager fragmentManager) {
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
                return ZakatSection1Fragment.newInstance();
            case 1:
                return ZakatSection2Fragment.newInstance();
            case 2:
                return ZakatSection3Fragment.newInstance();
            case 3:
                return ZakatSection4Fragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
