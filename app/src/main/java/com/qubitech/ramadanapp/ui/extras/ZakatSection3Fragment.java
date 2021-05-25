package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorFragment.zakatCalculatorPagerAdapter;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum1;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum2;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum3;

public class ZakatSection3Fragment extends Fragment {

    EditText zakatPartnershipText1,zakatPartnershipText2,zakatPartnershipText3,zakatPartnershipText4,zakatPartnershipText5,zakatPartnershipText6,zakatPartnershipText7,zakatPartnershipText8;
    private int temp=0,value1=0,value2=0,value3=0,value4=0,value5=0,value6=0,value7=0,value8=0;
    private String text="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_section_3, container, false);

        zakatPartnershipText1 = view.findViewById(R.id.zakat_partnership_1_editText);
        zakatPartnershipText2 = view.findViewById(R.id.zakat_partnership_2_editText);
        zakatPartnershipText3 = view.findViewById(R.id.zakat_partnership_3_editText);
        zakatPartnershipText4 = view.findViewById(R.id.zakat_partnership_4_editText);
        zakatPartnershipText5 = view.findViewById(R.id.zakat_partnership_5_editText);
        zakatPartnershipText6 = view.findViewById(R.id.zakat_partnership_6_editText);
        zakatPartnershipText7 = view.findViewById(R.id.zakat_partnership_7_editText);
        zakatPartnershipText8 = view.findViewById(R.id.zakat_partnership_8_editText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        zakatPartnershipText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                getTemp(zakatPartnershipText1);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                value1 = temp;
                getSum();

            }
        });
        zakatPartnershipText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                getTemp(zakatPartnershipText2);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value2 = temp;
                getSum();

            }
        });
        zakatPartnershipText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPartnershipText3);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value3 = temp;
                getSum();

            }
        });
        zakatPartnershipText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPartnershipText4);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value4 = temp;
                getSum();

            }
        });
        zakatPartnershipText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPartnershipText5);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value5 = temp;
                getSum();

            }
        });
        zakatPartnershipText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPartnershipText6);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value6 = temp;
                getSum();

            }
        });
        zakatPartnershipText7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPartnershipText7);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value7 = temp;
                getSum();

            }
        });
        zakatPartnershipText8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPartnershipText8);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value8 = temp;
                getSum();

            }
        });


    }

    public static ZakatSection3Fragment newInstance(){

        ZakatSection3Fragment zakatSection3Fragment = new ZakatSection3Fragment();
        return zakatSection3Fragment;

    }

    private void getTemp(EditText editText){
        text = editText.getText().toString();

        if(text.equals("")) {
            temp = 0;
        }
        else{
            temp = Integer.parseInt(text);
        }

    }

    private void getSum(){
        totalSum3 = value1 + value2 + value3 + value4 + value5 + value6 + value7 - value8;
        ZakatSection4Fragment.updateZakatViews();
    }
}