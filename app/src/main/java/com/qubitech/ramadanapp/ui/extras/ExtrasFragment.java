package com.qubitech.ramadanapp.ui.extras;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qubitech.ramadanapp.R;

import org.jetbrains.annotations.NotNull;

public class ExtrasFragment extends Fragment {

    ConstraintLayout zakatLayout, namesLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extras, container, false);

        zakatLayout = view.findViewById(R.id.zakat_calculator_layout);
        namesLayout = view.findViewById(R.id.allah_names_layout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        zakatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_extras_to_navigation_zakat_calculator);
            }
        });

        namesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_extras_to_navigation_allah_names);

            }
        });
    }
}