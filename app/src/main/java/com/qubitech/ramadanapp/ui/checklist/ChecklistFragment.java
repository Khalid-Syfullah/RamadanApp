package com.qubitech.ramadanapp.ui.checklist;

import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.qubitech.ramadanapp.R;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ChecklistFragment extends Fragment {

    private ChecklistViewModel mViewModel;
    Handler handler, handler2;
    HandlerThread handlerThread, handlerThread2;
    Thread thread3, thread4;
    ThreadPoolExecutor executor;
    Runnable runnable;


    public static ChecklistFragment newInstance() {
        return new ChecklistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        runnable = new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<100;i++) {
                    Log.d("Thread", i+" Thread is :" + Thread.currentThread().getName());
                }
            }
        };

        handlerThread = new HandlerThread("handler");
        handlerThread2 = new HandlerThread("handler2");
        handlerThread.start();
        handlerThread2.start();

        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES*2,
                NUMBER_OF_CORES*2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
        thread3 = new Thread(){
            @Override
            public void run() {

                Looper.prepare();
                Handler handler = new Handler();
                handler.post(runnable);
                Looper.loop();
            }
        };
        thread4 = new Thread(runnable);
        handler = new Handler(handlerThread.getLooper());
        handler2 = new Handler(handlerThread2.getLooper());

        executor.execute(runnable);
        thread3.start();
        thread4.start();
        handler.post(runnable);
        handler2.post(runnable);



        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChecklistViewModel.class);


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handlerThread.quit();
        handlerThread2.quit();
        thread3.interrupt();
        thread4.interrupt();
        executor.shutdown();
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
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_checklist_to_navigation_dashboard);
            }
        });
        anim.start();

    }

}