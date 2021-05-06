package com.qubitech.ramadanapp.ui.dua;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubitech.ramadanapp.R;



public class DuaDetailsFragment extends Fragment {

    String duaType="", duaTitle="", duaBody="";
    TextView duaTitleText, duaHeadingText, duaBodyText;
    ImageView backBtn, closeBtn;
    CardView duaDetailsCardView;

    public DuaDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            duaType = getArguments().getString("duaType");
            duaTitle = getArguments().getString("duaTitle");
            duaBody = getArguments().getString("duaBody");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dua_details, container, false);

        duaTitleText = view.findViewById(R.id.duadetails_title);
        duaHeadingText = view.findViewById(R.id.duadetails_heading);
        duaBodyText = view.findViewById(R.id.duadetails_body);
        duaDetailsCardView = view.findViewById(R.id.duadetails_cardView);
        backBtn = view.findViewById(R.id.duadetails_backBtn);
        closeBtn = view.findViewById(R.id.duadetails_closeBtn);

        if(duaType == "salah") {
            duaTitleText.setText(getResources().getString(R.string.dua_salah_title));
        }
        else if(duaType == "siyam") {
            duaTitleText.setText(getResources().getString(R.string.dua_fasting_title));
        }
        else if(duaType == "misc") {
            duaTitleText.setText(getResources().getString(R.string.dua_misc_title));
        }
        else if(duaType == "zikir") {
            duaTitleText.setText(getResources().getString(R.string.dua_zikir_title));
        }

        duaHeadingText.setText(duaTitle);
        duaBodyText.setText(duaBody);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("fragment",duaType);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_duadetails_to_navigation_dualist,bundle);

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB(duaDetailsCardView);
            }
        });
        return view;
    }


    private void hideFAB(View view) {

        int cx = view.getWidth();
        int cy = view.getHeight() ;
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, 0, initialRadius, 0);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_duadetails_to_navigation_dashboard);
            }
        });
        anim.start();

    }
}