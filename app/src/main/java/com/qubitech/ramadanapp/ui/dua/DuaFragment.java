package com.qubitech.ramadanapp.ui.dua;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.staticdata.StaticData;

public class DuaFragment extends Fragment implements View.OnClickListener{

    private DuaViewModel mViewModel;

    CardView duaCardView;
    ImageView closeBtn, salahImage, siyamImage, miscImage, zikirImage;
    TextView salahText, siyamText, miscText, zikirText;
    Bundle bundle;

    public static DuaFragment newInstance() {
        return new DuaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dua, container, false);

        duaCardView = view.findViewById(R.id.dua_cardView);
        closeBtn = view.findViewById(R.id.dua_closeBtn);

        salahImage = view.findViewById(R.id.dua_salah_image);
        siyamImage = view.findViewById(R.id.dua_fasting_image);
        miscImage = view.findViewById(R.id.dua_misc_image);
        zikirImage = view.findViewById(R.id.dua_zikir_image);

        salahText = view.findViewById(R.id.dua_salah_title);
        siyamText = view.findViewById(R.id.dua_fasting_title);
        miscText = view.findViewById(R.id.dua_misc_title);
        zikirText = view.findViewById(R.id.dua_zikir_title);

        bundle = new Bundle();

        salahImage.setOnClickListener(this);
        siyamImage.setOnClickListener(this);
        miscImage.setOnClickListener(this);
        zikirImage.setOnClickListener(this);

        salahText.setOnClickListener(this);
        siyamText.setOnClickListener(this);
        miscText.setOnClickListener(this);
        zikirText.setOnClickListener(this);


        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!StaticData.duaRevealFlag) {
                    revealFAB(duaCardView);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
                    animation.setDuration(500);
                    closeBtn.setAnimation(animation);
                    StaticData.duaRevealFlag = true;
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB(duaCardView);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DuaViewModel.class);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.dua_salah_title || view.getId() == R.id.dua_salah_image){
            bundle.putString("fragment","salah");
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dua_to_navigation_dualist,bundle);
        }
        else if(view.getId() == R.id.dua_fasting_title || view.getId() == R.id.dua_fasting_image){
            bundle.putString("fragment","siyam");
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dua_to_navigation_dualist,bundle);

        }
        else if(view.getId() == R.id.dua_misc_title || view.getId() == R.id.dua_misc_image){
            bundle.putString("fragment","misc");
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dua_to_navigation_dualist,bundle);

        }
        else if(view.getId() == R.id.dua_zikir_title || view.getId() == R.id.dua_zikir_image){
            bundle.putString("fragment","zikir");
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dua_to_navigation_dualist,bundle);

        }
    }

    private void revealFAB(View view) {

        int cx = view.getWidth() ;
        int cy = view.getHeight() ;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, 0, 0, finalRadius);
        anim.setDuration(500);

        anim.start();
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
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dua_to_navigation_dashboard);
            }
        });
        anim.start();

    }

}