package com.qubitech.ramadanapp.ui.mosques;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.dashboard.LocationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MosquesFragment extends Fragment {

    Double latitude,longitude;
    SharedPreferences localePreferences;
    Intent locationIntent;
    GoogleMap mosqueMap;

    boolean revealFlag = false;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mosqueMap = googleMap;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mosques, container, false);

        CardView cardView = view.findViewById(R.id.cardView14);
        ImageView closeBtn = view.findViewById(R.id.imageView24);

        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB(cardView);
            }
        });

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!revealFlag) {
                    revealFAB(cardView);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
                    animation.setDuration(500);
                    closeBtn.setAnimation(animation);
                    revealFlag = true;
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(locationIntent);


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
        getActivity().startService(locationIntent);


    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().stopService(locationIntent);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            LatLng userLocation = new LatLng(latitude,longitude);
            mosqueMap.clear();
            mosqueMap.addMarker(new MarkerOptions().position(userLocation).title(getResources().getString(R.string.current_location)));
            mosqueMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));

            Locale aLocale = new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("US").build();
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), aLocale);

            String [] districts_bn = getResources().getStringArray(R.array.division_bn);
            String [] districts_en = getResources().getStringArray(R.array.division_en);

            HashMap<String, String> hashMap = new HashMap<String, String>();

            for(int i=0;i<districts_bn.length;i++) {
                hashMap.put(districts_bn[i], districts_en[i]);
            }


            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String city_bn = addresses.get(0).getLocality();
                String countryName = addresses.get(0).getCountryName();
                String city_en="";


                for (Map.Entry<String, String> entry :
                        hashMap.entrySet()) {
                    if (entry.getKey().equals(city_bn)) {
                        city_en = entry.getValue();
                    }
                }

                if(localePreferences.contains("Current_Language")) {
                    String locale = localePreferences.getString("Current_Language", "");
                    if (locale.equals("bn")) {
                    } else if (locale.equals("en")) {
                    }
                }



            } catch (IOException e1) {
                e1.printStackTrace();
            }



        }
    };

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