package com.qubitech.ramadanapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.qubitech.ramadanapp.ui.dashboard.DashboardFragment;
import com.qubitech.ramadanapp.ui.quran.SurahFragment;
import com.qubitech.ramadanapp.ui.quran.listener.OnSwipeTouchListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.util.Locale;

import static com.qubitech.ramadanapp.staticdata.StaticData.currentFragment;
import static com.qubitech.ramadanapp.staticdata.StaticData.isMediaActive;
import static com.qubitech.ramadanapp.staticdata.StaticData.isMediaReset;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaPlayer;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaStatus;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaTitle;
import static com.qubitech.ramadanapp.staticdata.StaticData.mediaUrl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    DrawerLayout drawerLayout;
    NavigationView drawerNavigationView;
    BottomNavigationView bottomNavigationView;
    CardView mainMediaCardView;
    TextView mainMediaPlayerTitleView,mainMediaPlayerStatusView;
    ImageView menu, mainMediaPlayerStartView, mainMediaPlayerStopView;
    SharedPreferences surahPreferences;
    SharedPreferences.Editor surahPreferencesEditor;
    String surahPref = "Surah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerNavigationView = findViewById(R.id.nav_drawer_view);
        bottomNavigationView = findViewById(R.id.nav_view);
        menu = findViewById(R.id.imageView12);
        mainMediaCardView = findViewById(R.id.main_media_cardView);
        mainMediaPlayerTitleView = findViewById(R.id.main_media_title);
        mainMediaPlayerStatusView = findViewById(R.id.main_media_status);
        mainMediaPlayerStartView = findViewById(R.id.main_media_start_imageView);
        mainMediaPlayerStopView = findViewById(R.id.main_media_stop_imageView);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.mobile_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        menu.setOnClickListener(this);


        readSurahPreferences();
        updateMediaUi();

        mainMediaPlayerStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMediaActive){
                    pauseAudio();
                }
                else{
                    playAudio();
                }
            }
        });

        mainMediaPlayerStopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudio();
            }
        });

        mainMediaCardView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            public void onSwipeRight() {
                swipeToHide(mainMediaCardView);
            }
            public void onSwipeLeft() {
                swipeToHide(mainMediaCardView);
            }
        });





        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                int nv = navController.getCurrentDestination().getId();


                if(nv == R.id.navigation_dashboard){
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_quran);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_extras);
                    }

                }



                if(nv == R.id.navigation_calendar){
                    if(id == R.id.navigation_dashboard){
                        navController.navigate(R.id.action_navigation_calendar_to_navigation_dashboard);
                    }
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_calendar_to_navigation_quran);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_calendar_to_navigation_extras);
                    }

                }

                if(nv == R.id.navigation_quran){
                    if(id == R.id.navigation_dashboard){
                        navController.navigate(R.id.action_navigation_quran_to_navigation_dashboard);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_quran_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_quran_to_navigation_extras);
                    }

                }

                if(nv == R.id.navigation_extras){
                    if(id == R.id.navigation_dashboard){
                        navController.navigate(R.id.action_navigation_extras_to_navigation_dashboard);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_extras_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_extras_to_navigation_quran);
                    }

                }

                if(nv == R.id.navigation_mosques){
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_mosques_to_navigation_quran);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_mosques_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_mosques_to_navigation_extras);
                    }
                }

                if(nv == R.id.navigation_tasbih){
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_tasbih_to_navigation_quran);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_tasbih_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_tasbih_to_navigation_extras);
                    }
                }

                if(nv == R.id.navigation_checklist){
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_checklist_to_navigation_quran);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_checklist_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_checklist_to_navigation_extras);
                    }
                }

                if(nv == R.id.navigation_dua){
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_dua_to_navigation_quran);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_dua_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_extras){
                        navController.navigate(R.id.action_navigation_dua_to_navigation_extras);
                    }
                }



                return false;
            }
        });

        drawerNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.english){
                  languageAlertDialog("en");
                }
                else if(id == R.id.bangla){
                   languageAlertDialog("bn");
                }

                else if (id == R.id.shareApp){
                    Intent shareIntent=new Intent((Intent.ACTION_SEND));
                    shareIntent.setType("text/plain");
                    String shareBody="RamadanApp: +\n http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                    String shareSub="RamadanApp+";
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                    startActivity(Intent.createChooser(shareIntent,"Share Using"));

                }
                else if (id == R.id.emailUs){

                    String email = "qubitechsolutions@gmail.com";
                    String subject = "Contact Us";
                    String body = "Please share your valuable thoughts with us.";
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivityForResult(intent,200);
                }

                else if(id == R.id.sendReview){
                    Uri uri = Uri.parse("https://docs.google.com/forms/d/e/98sd9f8s9f8sd9f8v89s8df/viewform?usp=sf_link");
                    Intent review = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(review);
                }

                else if(id == R.id.rateApp) {

                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToMarket);


                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


        });

    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageView12:

                if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
        }
    }



    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() > 0) {

            if(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName().equals("dashboard")) {
                fragmentManager.popBackStack("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().remove(new DashboardFragment()).commit();
            }
            if(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName().equals("tasbih")) {
                fragmentManager.popBackStack("tasbih", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                hideFAB(findViewById(R.id.cardView10));
            }
        }
        else {
            super.onBackPressed();
        }
    }


    private void prepareMedia(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(mediaUrl);
            mediaPlayer.prepare();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayerComplete) {
                    stopAudio();

                    Log.d("MainActivity","MediaPlayer: Completed");
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readSurahPreferences(){
        surahPreferences = getSharedPreferences(surahPref, Context.MODE_PRIVATE);

        if(surahPreferences.contains("mediaTitle")){

            mediaTitle = surahPreferences.getString("mediaTitle","");
            mediaStatus = surahPreferences.getString("mediaStatus","");
            mediaUrl = surahPreferences.getString("mediaUrl","");
            isMediaActive = surahPreferences.getBoolean("isMediaActive",false);
            isMediaReset = surahPreferences.getBoolean("isMediaReset",true);
        }
    }

    private void updateSurahPreferences(){
        surahPreferences = getSharedPreferences(surahPref, Context.MODE_PRIVATE);
        surahPreferencesEditor = surahPreferences.edit();

        surahPreferencesEditor.putString("mediaTitle", mediaTitle);
        surahPreferencesEditor.putString("mediaStatus", mediaStatus);
        surahPreferencesEditor.putString("mediaUrl", mediaUrl);
        surahPreferencesEditor.putBoolean("isMediaActive", isMediaActive);
        surahPreferencesEditor.putBoolean("isMediaReset", isMediaReset);

        surahPreferencesEditor.apply();
    }


    private void updateMediaUi(){
        if(isMediaActive) {
            mainMediaCardView.setVisibility(View.VISIBLE);
            mainMediaPlayerStartView.setImageResource(R.drawable.pause);
            mediaStatus = getResources().getString(R.string.now_playing);
            mainMediaPlayerTitleView.setText(mediaTitle);
            mainMediaPlayerStatusView.setText(mediaStatus);


            if(mediaPlayer != null){
                if(!mediaPlayer.isPlaying()){
                    prepareMedia();
                }
            }

        }
        else{
            mainMediaPlayerStartView.setImageResource(R.drawable.play);
            mediaStatus = getResources().getString(R.string.ready_to_play);
            mainMediaPlayerStatusView.setText(mediaStatus);
            prepareMedia();

        }
    }

    private void playAudio(){
        try {
            if (currentFragment != null) {
                NavHostFragment navHostFragment = (NavHostFragment) currentFragment;
                SurahFragment surahFragment = (SurahFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                surahFragment.updateMediaControls(R.drawable.stop);
            }
            mainMediaPlayerStartView.setImageResource(R.drawable.pause);
            mediaStatus = getResources().getString(R.string.now_playing);
            mainMediaPlayerStatusView.setText(mediaStatus);
            isMediaActive = true;

            updateSurahPreferences();

            if (isMediaReset) {
                prepareMedia();
                mediaPlayer.start();
                isMediaReset = false;
            } else {
                if(mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void pauseAudio(){
        try {
            if (currentFragment != null) {
                NavHostFragment navHostFragment = (NavHostFragment) currentFragment;
                SurahFragment surahFragment = (SurahFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                surahFragment.updateMediaControls(R.drawable.play);
            }
            mainMediaPlayerStartView.setImageResource(R.drawable.play);
            mediaStatus = getResources().getString(R.string.paused);
            mainMediaPlayerStatusView.setText(mediaStatus);
            isMediaActive = false;

            updateSurahPreferences();

            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void stopAudio(){
        try {
            if (currentFragment != null) {
                NavHostFragment navHostFragment = (NavHostFragment) currentFragment;
                SurahFragment surahFragment = (SurahFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                surahFragment.updateMediaControls(R.drawable.play);
            }
            mainMediaPlayerStartView.setImageResource(R.drawable.play);
            mediaStatus = getResources().getString(R.string.ready_to_play);
            mainMediaPlayerStatusView.setText(mediaStatus);
            isMediaActive = false;
            isMediaReset = true;

            updateSurahPreferences();


            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }






    private void languageAlertDialog(String lang){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.change_language), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setLocale(lang);
            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    public void setLocale(String localeString) {

        Locale myLocale = new Locale(localeString);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = myLocale;
        resources.updateConfiguration(configuration, displayMetrics);

        SharedPreferences localePreferences = getSharedPreferences("Language",MODE_PRIVATE);
        SharedPreferences.Editor localePreferencesEditor = localePreferences.edit();
        localePreferencesEditor.putString("Current_Language", localeString);
        localePreferencesEditor.apply();

        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                navController.navigate(R.id.action_navigation_tasbih_to_navigation_dashboard);
            }
        });
        anim.start();
    }

    private void swipeToHide(View view) {

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

            }
        });
        anim.start();
    }




}


