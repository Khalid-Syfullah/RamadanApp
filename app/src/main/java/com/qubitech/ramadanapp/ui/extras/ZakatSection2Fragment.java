package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorFragment.zakatCalculatorPagerAdapter;
import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum2;

public class ZakatSection2Fragment extends Fragment {


    EditText zakatBusinessText1,zakatBusinessText2,zakatBusinessText3,zakatBusinessText4,zakatBusinessText5,zakatBusinessText6,zakatBusinessText7,zakatBusinessText8;
    private int temp=0,value1=0,value2=0,value3=0,value4=0,value5=0,value6=0,value7=0,value8=0;
    private String text="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_section_2, container, false);
        
        zakatBusinessText1 = view.findViewById(R.id.zakat_business_1_editText);
        zakatBusinessText2 = view.findViewById(R.id.zakat_business_2_editText);
        zakatBusinessText3 = view.findViewById(R.id.zakat_business_3_editText);
        zakatBusinessText4 = view.findViewById(R.id.zakat_business_4_editText);
        zakatBusinessText5 = view.findViewById(R.id.zakat_business_5_editText);
        zakatBusinessText6 = view.findViewById(R.id.zakat_business_6_editText);
        zakatBusinessText7 = view.findViewById(R.id.zakat_business_7_editText);
        zakatBusinessText8 = view.findViewById(R.id.zakat_business_8_editText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        zakatBusinessText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                getTemp(zakatBusinessText1);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                value1 = temp;
                getSum();

            }
        });
        zakatBusinessText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                getTemp(zakatBusinessText2);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value2 = temp;
                getSum();

            }
        });
        zakatBusinessText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatBusinessText3);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value3 = temp;
                getSum();

            }
        });
        zakatBusinessText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatBusinessText4);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value4 = temp;
                getSum();

            }
        });
        zakatBusinessText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatBusinessText5);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value5 = temp;
                getSum();

            }
        });
        zakatBusinessText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatBusinessText6);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value6 = temp;
                getSum();

            }
        });
        zakatBusinessText7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatBusinessText7);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value7 = temp;
                getSum();

            }
        });
        zakatBusinessText8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatBusinessText8);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value8 = temp;
                getSum();

            }
        });
    }

    public static ZakatSection2Fragment newInstance(){

        ZakatSection2Fragment zakatSection2Fragment = new ZakatSection2Fragment();
        return zakatSection2Fragment;

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
        totalSum2 = value1 + value2 + value3 + value4 + value5 + value6 + value7 - value8;

    }
}