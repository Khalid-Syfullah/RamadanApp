package com.qubitech.ramadanapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.qubitech.ramadanapp.ui.dashboard.DashboardFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView drawerNavigationView = findViewById(R.id.nav_drawer_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.mobile_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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
                    if(id == R.id.navigation_dua){
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_dua);
                    }

                }

                if(nv == R.id.navigation_calendar){
                    if(id == R.id.navigation_dashboard){
                        navController.navigate(R.id.action_navigation_calendar_to_navigation_dashboard);
                    }
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_calendar_to_navigation_quran);
                    }
                    if(id == R.id.navigation_dua){
                        navController.navigate(R.id.action_navigation_calendar_to_navigation_dua);
                    }

                }

                if(nv == R.id.navigation_quran){
                    if(id == R.id.navigation_dashboard){
                        navController.navigate(R.id.action_navigation_quran_to_navigation_dashboard);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_quran_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_dua){
                        navController.navigate(R.id.action_navigation_quran_to_navigation_dua);
                    }

                }

                if(nv == R.id.navigation_dua){
                    if(id == R.id.navigation_dashboard){
                        navController.navigate(R.id.action_navigation_dua_to_navigation_dashboard);
                    }
                    if(id == R.id.navigation_calendar){
                        navController.navigate(R.id.action_navigation_dua_to_navigation_calendar);
                    }
                    if(id == R.id.navigation_quran){
                        navController.navigate(R.id.action_navigation_dua_to_navigation_quran);
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
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() > 0) {

            if(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName().equals("dashboard")) {
                fragmentManager.popBackStack("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().remove(new DashboardFragment()).commit();
            }
        }
        else {
            super.onBackPressed();
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

}


