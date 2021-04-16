package com.qubitech.ramadanapp.ui.mosques;

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

import com.qubitech.ramadanapp.R;

public class MosquesFragment extends Fragment {

    private MosquesViewModel mViewModel;

    public static MosquesFragment newInstance() {
        return new MosquesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mosques, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MosquesViewModel.class);
        // TODO: Use the ViewModel
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
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_mosques_to_navigation_dashboard);
            }
        });
        anim.start();

    }

}