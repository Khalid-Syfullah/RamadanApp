package com.qubitech.ramadanapp.ui.extras;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

import static com.qubitech.ramadanapp.ui.extras.ZakatCalculatorPagerAdapter.totalSum1;

public class ZakatSection1Fragment extends Fragment {


    private EditText zakatPersonalText1,zakatPersonalText2,zakatPersonalText3,zakatPersonalText4,zakatPersonalText5,zakatPersonalText6,zakatPersonalText7,zakatPersonalText8,zakatPersonalText9;
    private int temp=0,value1=0,value2=0,value3=0,value4=0,value5=0,value6=0,value7=0,value8=0,value9=0;
    private String text="";
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakat_section_1, container, false);

        zakatPersonalText1 = view.findViewById(R.id.zakat_personal_1_editText);
        zakatPersonalText2 = view.findViewById(R.id.zakat_personal_2_editText);
        zakatPersonalText3 = view.findViewById(R.id.zakat_personal_3_editText);
        zakatPersonalText4 = view.findViewById(R.id.zakat_personal_4_editText);
        zakatPersonalText5 = view.findViewById(R.id.zakat_personal_5_editText);
        zakatPersonalText6 = view.findViewById(R.id.zakat_personal_6_editText);
        zakatPersonalText7 = view.findViewById(R.id.zakat_personal_7_editText);
        zakatPersonalText8 = view.findViewById(R.id.zakat_personal_8_editText);
        zakatPersonalText9 = view.findViewById(R.id.zakat_personal_9_editText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        zakatPersonalText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
               getTemp(zakatPersonalText1);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                value1 = temp;
                getSum();

            }
        });
        zakatPersonalText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
                getTemp(zakatPersonalText2);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value2 = temp;
                getSum();

            }
        });
        zakatPersonalText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText3);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value3 = temp;
                getSum();

            }
        });
        zakatPersonalText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText4);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value4 = temp;
                getSum();

            }
        });
        zakatPersonalText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText5);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value5 = temp;
                getSum();

            }
        });
        zakatPersonalText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText6);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value6 = temp;
                getSum();

            }
        });
        zakatPersonalText7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText7);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value7 = temp;
                getSum();

            }
        });
        zakatPersonalText8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText8);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value8 = temp;
                getSum();

            }
        });
        zakatPersonalText9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTemp(zakatPersonalText9);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value9 = temp;
                getSum();
            }
        });

    }

    public static ZakatSection1Fragment newInstance(){

        ZakatSection1Fragment zakatSection1Fragment = new ZakatSection1Fragment();
        return zakatSection1Fragment;

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
        totalSum1 = value1 + value2 + value3 + value4 + value5 + value6 + value7 + value8 - value9;

    }

   
}