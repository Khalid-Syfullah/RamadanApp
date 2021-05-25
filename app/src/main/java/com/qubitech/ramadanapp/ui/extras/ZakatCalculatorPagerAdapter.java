package com.qubitech.ramadanapp.ui.extras;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ZakatCalculatorPagerAdapter extends FragmentPagerAdapter {

    private String [] page;
    public static int totalSum = 0, totalSum1 = 0, totalSum2 = 0, totalSum3 = 0;
    public static int totalSumMinimum = 32500;
    public static double totalZakat = 0.0;

    public ZakatCalculatorPagerAdapter(Context c,FragmentManager fragmentManager) {
        super(fragmentManager);
        page = new String[]{c.getResources().getString(R.string.personal_wealth), c.getResources().getString(R.string.business_1), c.getResources().getString(R.string.business_2), c.getResources().getString(R.string.zakat_eligibility)};
    }

    @Override
    public int getCount() {
        return 4;
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

        return page[position];

    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }


}
