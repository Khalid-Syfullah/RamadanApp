package com.qubitech.ramadanapp.ui.mosques;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.location.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MosquesFragment extends Fragment {

    Double latitude,longitude;
    SharedPreferences localePreferences;
    Intent locationIntent;
    GoogleMap mosqueMap;
    Marker myLocationMarker, mosqueMarker;
    LatLng userLocation;

    String searchUrl = "";
    ArrayList<String> place_id, name, vicinity;
    ArrayList<Double> lat, lng;
    float distanceInMeters;
    HandlerThread backgroundThread;

    TextView nearbyMosqueTextView, mosqueNameTextView, mosqueDistanceTextView, mosqueLocationTextView;
    CardView cardView, cardViewMosque;
    ImageView closeBtn;

    boolean revealFlag = false;

    public static MosquesFragment newInstance(){

        MosquesFragment mosquesFragment = new MosquesFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 1);
        args.putString("someTitle", "Mosque");
        mosquesFragment.setArguments(args);
        return mosquesFragment;

    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
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

        cardView = view.findViewById(R.id.cardView14);
        cardViewMosque = view.findViewById(R.id.cardView15);
        closeBtn = view.findViewById(R.id.imageView24);
        nearbyMosqueTextView = view.findViewById(R.id.textView40);
        mosqueNameTextView = view.findViewById(R.id.textView41);
        mosqueDistanceTextView = view.findViewById(R.id.textView42);
        mosqueLocationTextView = view.findViewById(R.id.textView46);


        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        locationIntent = new Intent(getActivity().getApplicationContext(), LocationService.class);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    100);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeBtn.setEnabled(false);
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


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
            getActivity().startService(locationIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private class mosqueTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                findNearbyMosques();
                findClosestMosqueDistance();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                updateMosqueOnViewInitial();
                addMarkersInitial();
                getDirectionsToMosqueInitial();

            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {

                latitude = Double.valueOf(intent.getStringExtra("latitude"));
                longitude = Double.valueOf(intent.getStringExtra("longitude"));

                try {
                    getActivity().unregisterReceiver(broadcastReceiver);
                    getActivity().stopService(locationIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                searchUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=1000&types=mosque&sensor=false&key=" + getResources().getString(R.string.google_maps_key);
                userLocation = new LatLng(latitude, longitude);

                mosqueMap.clear();

                myLocationMarker = mosqueMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .snippet(String.valueOf(R.string.nearby_mosque))
                        .title(getResources().getString(R.string.current_location)));
                myLocationMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.marker));

                mosqueMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));

                mosqueMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if (!marker.equals(myLocationMarker)) {

                            try {

                                findSelectedMosqueDistance(marker);
                                updateMosqueOnViewOnSelected(marker);
                                addMarkersOnTap();
                                getDirectionsToMosqueOnTap(marker);

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        return true;
                    }
                });

                new mosqueTask().execute();

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    private void findNearbyMosques(){
        //Search for Nearby Mosques and save results to multiple Array Lists using Google Places API

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(searchUrl)
                    .build();


            Response response = client.newCall(request).execute();

            if(response.body() != null){

                place_id = new ArrayList<>();
                lat = new ArrayList<>();
                lng = new ArrayList<>();
                name = new ArrayList<>();
                vicinity = new ArrayList<>();


                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for(int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("location");

                    place_id.add(jsonObject1.getString("place_id"));
                    name.add(jsonObject1.getString("name"));
                    vicinity.add(jsonObject1.getString("vicinity"));
                    lat.add(jsonObject3.getDouble("lat"));
                    lng.add(jsonObject3.getDouble("lng"));

                    Log.d("Response", "Results: [" + i + "]: " + jsonObject1.getString("name"));

                }



            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }

    private void findClosestMosqueDistance(){

        if(lat == null){
            return;
        }
        //Find the distance between my location and the nearest mosque from Location API
        Location loc1 = new Location("");
        loc1.setLatitude(latitude);
        loc1.setLongitude(longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(lat.get(0));
        loc2.setLongitude(lng.get(0));

        distanceInMeters = loc1.distanceTo(loc2);
    }

    private void findSelectedMosqueDistance(Marker marker){

        if(lat == null){
            return;
        }
        Location loc1 = new Location("");
        loc1.setLatitude(latitude);
        loc1.setLongitude(longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(marker.getPosition().latitude);
        loc2.setLongitude(marker.getPosition().longitude);

        distanceInMeters = loc1.distanceTo(loc2);

    }

    private void updateMosqueOnViewInitial(){

        if(lat == null){
            return;
        }
        for (int i = 0; i < lat.size(); i++) {

            if(getActivity() == null){
                break;
            }

            //Add marker for the nearest mosque from LAT-LNG ArrayList
            mosqueMarker = mosqueMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat.get(i), lng.get(i)))
                    .snippet(vicinity.get(i))
                    .title(name.get(i)));

            mosqueMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.mosque_marker));
        }

        try {
            nearbyMosqueTextView.setText(name.get(0));
            mosqueNameTextView.setText(name.get(0));
            mosqueLocationTextView.setText(vicinity.get(0));
            mosqueDistanceTextView.setText("Distance: " + distanceInMeters + " m");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(getActivity() == null)
            return;

        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
        animation.setDuration(500);
        cardViewMosque.setAnimation(animation);
        cardViewMosque.setVisibility(View.VISIBLE);

        Log.d("Maps", "Mosque Clicked! Distance : " + distanceInMeters + " m");

    }

    private void updateMosqueOnViewOnSelected(Marker marker){

        if(lat == null){
            return;
        }
        for (int i = 0; i < lat.size(); i++) {

            if(getActivity() == null){
                break;
            }

            //Add marker for the nearest mosque from LAT-LNG ArrayList
            mosqueMarker = mosqueMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat.get(i), lng.get(i)))
                    .snippet(vicinity.get(i))
                    .title(name.get(i)));

            mosqueMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.mosque_marker));
        }

        nearbyMosqueTextView.setText(marker.getTitle());
        mosqueNameTextView.setText(marker.getTitle());
        mosqueLocationTextView.setText(marker.getSnippet());
        mosqueDistanceTextView.setText("Distance: " + distanceInMeters + " m");

        if(getActivity() == null)
            return;

        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
        animation.setDuration(500);
        cardViewMosque.setAnimation(animation);
        cardViewMosque.setVisibility(View.VISIBLE);

        Log.d("Maps", "Mosque Clicked! Distance : " + distanceInMeters + " m");

    }


    private void addMarkersInitial(){
        //Add a Marker to my current position

        if(getActivity() == null){
            return;
        }
        mosqueMap.clear();


        myLocationMarker = mosqueMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .snippet(String.valueOf(R.string.nearby_mosque))
                .title(getResources().getString(R.string.current_location)));
        myLocationMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.marker));

        if(lat == null){
            return;
        }
        //Add Markers to all of the nearby mosque locations
        for (int i = 0; i < lat.size(); i++) {

            if(getActivity() == null){
                return;
            }
            mosqueMarker = mosqueMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat.get(i), lng.get(i)))
                    .snippet(vicinity.get(i))
                    .title(name.get(i)));

            mosqueMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.mosque_marker));

        }
    }

    private void addMarkersOnTap(){
        //Add a Marker to my current position

        if(getActivity() == null){
            return;
        }
        mosqueMap.clear();

        myLocationMarker = mosqueMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .snippet(String.valueOf(R.string.nearby_mosque))
                        .title(getResources().getString(R.string.current_location)));
        myLocationMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.marker));

        if(lat == null){
            return;
        }
        //Add Markers to all of the nearby mosque locations
        for (int i = 0; i < lat.size(); i++) {

            if(getActivity() == null){
                return;
            }
            mosqueMarker = mosqueMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat.get(i), lng.get(i)))
                    .snippet(vicinity.get(i))
                    .title(name.get(i)));

            mosqueMarker.setIcon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.mosque_marker));

        }
    }

    //Get Directions to the closest Mosque on the Map using Directions API
    private void getDirectionsToMosqueInitial(){

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        if(getActivity() == null || lat == null){
            return;
        }
        try {

            //Execute Directions API request
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(getResources().getString(R.string.google_maps_key))
                    .build();
            DirectionsApiRequest req = DirectionsApi.getDirections(context, latitude + "," + longitude, lat.get(0) + "," + lng.get(0));
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        //Draw the Polyline in the map if Direction is available
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.parseColor("#67CE22")).width(15);
            mosqueMap.addPolyline(opts);
        }
    }

    //Get Directions to the selected Mosque on the Map using Directions API
    private void getDirectionsToMosqueOnTap(Marker marker){

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        if(getActivity() == null || marker == null){
            return;
        }
        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_maps_key))
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, latitude+","+longitude, marker.getPosition().latitude+","+marker.getPosition().longitude);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        //Draw the Polyline in the map if Direction is available
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.parseColor("#67CE22")).width(15);
            mosqueMap.addPolyline(opts);
        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
                if(getActivity() == null)
                    return;
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_mosques_to_navigation_dashboard);
            }
        });
        anim.start();

    }

}