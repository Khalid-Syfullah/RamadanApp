package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum1;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum2;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum3;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSumMinimum;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalZakat;

public class ZakatSection4Fragment extends Fragment {

    private static TextView zakatEligibilityText,totalZakatText;
    private static CardView zakatNotRequried,zakatRequired;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_section_4, container, false);
        zakatEligibilityText = view.findViewById(R.id.zakat_eligibility_total_eligibility);
        totalZakatText = view.findViewById(R.id.zakat_eligibility_total_zakat);
        zakatNotRequried = view.findViewById(R.id.zakat_eligibility_2_cardView);
        zakatRequired = view.findViewById(R.id.zakat_eligibility_3_cardView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateZakatViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateZakatViews();

    }

    public static ZakatSection4Fragment newInstance(){

        ZakatSection4Fragment zakatSection4Fragment = new ZakatSection4Fragment();
        return zakatSection4Fragment;

    }

    private static void getTotalSum(){
        totalSum = totalSum1 + totalSum2 + totalSum3;
        totalZakat = totalSum * 2.5 / 100;

        Log.d("Zakat","Sum : "+totalSum+" Zakat : "+totalZakat);
    }

    public static void updateZakatViews(){

        getTotalSum();

        zakatEligibilityText.setText(totalSum+ " Taka");
        totalZakatText.setText(totalZakat+ " Taka");


        if(totalSum < totalSumMinimum){
            zakatNotRequried.setVisibility(View.VISIBLE);
            zakatRequired.setVisibility(View.GONE);
        }
        else{
            zakatNotRequried.setVisibility(View.GONE);
            zakatRequired.setVisibility(View.VISIBLE);
        }
    }
}