package com.qubitech.ramadanapp.ui.tasbih;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.mosques.MosquesFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.qubitech.ramadanapp.R.drawable.feedback;
import static com.qubitech.ramadanapp.R.drawable.feedback_disabled;
import static com.qubitech.ramadanapp.R.drawable.tasbih_button;
import static com.qubitech.ramadanapp.R.drawable.tasbih_count_button;
import static com.qubitech.ramadanapp.R.drawable.tasbih_reset_button;


public class TasbihFragment extends Fragment implements View.OnClickListener{


    TasbihViewModel tasbihViewModel;
    ImageView closeBtn, resetBtn, feedbackBtn;
    ImageButton tasbihBtn;
    TextView tasbihText, loopText, totalText, resetText, feedbackText;
    CardView cardView;
    int tasbihCounter=0;
    int loopCounter=0;
    int totalCounter=0;
    boolean revealFlag = false, vibeFlag = true;
    String currentDate;
    SharedPreferences localePreferences;
    SharedPreferences tasbihPreference;
    SharedPreferences.Editor tasbihPreferenceEditor;
    Vibrator vibe;

    public static TasbihFragment newInstance(){

        TasbihFragment tasbihFragment = new TasbihFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 2);
        args.putString("someTitle", "Tasbih");
        tasbihFragment.setArguments(args);
        return tasbihFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasbih, container, false);
        tasbihViewModel = new ViewModelProvider(this).get(TasbihViewModel.class);

        cardView = view.findViewById(R.id.cardView10);

        tasbihText = view.findViewById(R.id.textView26);
        loopText = view.findViewById(R.id.textView34);
        totalText = view.findViewById(R.id.textView36);
        feedbackText = view.findViewById(R.id.textView38);

        tasbihBtn = view.findViewById(R.id.imageButton);
        resetBtn = view.findViewById(R.id.imageView9);
        feedbackBtn = view.findViewById(R.id.imageView10);
        closeBtn = view.findViewById(R.id.imageView7);

        localePreferences = getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        tasbihPreference = getActivity().getSharedPreferences("tasbih_data",MODE_PRIVATE);
        tasbihPreferenceEditor = tasbihPreference.edit();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = dateFormat.format(cal.getTime());
        Log.d("Current Date","Today is "+currentDate);

        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);



        if(tasbihPreference.contains("tasbih_date")){
            if(tasbihPreference.getString("tasbih_date","").equals(currentDate)) {
                tasbihCounter = tasbihPreference.getInt("tasbih_counter: " + currentDate, 0);
                loopCounter = tasbihPreference.getInt("loop_counter: " + currentDate, 0);
                totalCounter = tasbihPreference.getInt("total_counter: " + currentDate, 0);
            }
        }

        if(tasbihPreference.contains("vibration")){
            vibeFlag = tasbihPreference.getBoolean("vibration",true);
        }


        if(localePreferences.contains("Current_Language")){
            String locale = localePreferences.getString("Current_Language","");
            if(locale.equals("bn")) {
                tasbihText.setText(banglaConverter(String.valueOf(tasbihCounter)));
                loopText.setText(banglaConverter(String.valueOf(loopCounter)));
                totalText.setText(banglaConverter(String.valueOf(totalCounter)));
            }

            else if(locale.equals("en")) {
                tasbihText.setText(String.valueOf(tasbihCounter));
                loopText.setText(String.valueOf(loopCounter));
                totalText.setText(String.valueOf(totalCounter));
            }
        }
        if(tasbihCounter == 33) {
            tasbihBtn.setImageResource(tasbih_reset_button);
        }

        tasbihBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        feedbackBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);


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
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imageView7:
                closeBtn.setEnabled(false);
                hideFAB(cardView);
                break;
            case R.id.imageButton:

                if(tasbihCounter==33){
                    tasbihCounter=0;
                    loopCounter++;

                    if(vibeFlag) {
                        vibe.vibrate(100);
                    }
                    tasbihBtn.setImageResource(tasbih_button);

                    if(localePreferences.contains("Current_Language")){
                        String locale = localePreferences.getString("Current_Language","");
                        if(locale.equals("bn")) {
                            tasbihText.setText(banglaConverter(String.valueOf(tasbihCounter)));
                            loopText.setText(banglaConverter(String.valueOf(loopCounter)));
                            totalText.setText(banglaConverter(String.valueOf(totalCounter)));
                        }

                        else if(locale.equals("en")) {
                            tasbihText.setText(String.valueOf(tasbihCounter));
                            loopText.setText(String.valueOf(loopCounter));
                            totalText.setText(String.valueOf(totalCounter));
                        }
                    }

                    tasbihPreferenceEditor.putString("tasbih_date", currentDate);
                    tasbihPreferenceEditor.putInt("tasbih_counter: "+currentDate, tasbihCounter);
                    tasbihPreferenceEditor.putInt("loop_counter: "+currentDate, loopCounter);
                    tasbihPreferenceEditor.putInt("total_counter: "+currentDate, totalCounter);
                    tasbihPreferenceEditor.apply();

                }
                else{
                    tasbihCounter++;
                    totalCounter++;

                    if(vibeFlag) {
                        vibe.vibrate(100);
                    }

                    if(tasbihCounter == 33) {
                        tasbihBtn.setImageResource(tasbih_reset_button);
                    }
                    else{
                        tasbihBtn.setImageResource(tasbih_count_button);

                    }



                    if(localePreferences.contains("Current_Language")){
                        String locale = localePreferences.getString("Current_Language","");
                        if(locale.equals("bn")) {
                            tasbihText.setText(banglaConverter(String.valueOf(tasbihCounter)));
                            totalText.setText(banglaConverter(String.valueOf(totalCounter)));
                        }

                        else if(locale.equals("en")) {
                            tasbihText.setText(String.valueOf(tasbihCounter));
                            totalText.setText(String.valueOf(totalCounter));
                        }
                    }


                    tasbihPreferenceEditor.putString("tasbih_date", currentDate);
                    tasbihPreferenceEditor.putInt("tasbih_counter: "+currentDate, tasbihCounter);
                    tasbihPreferenceEditor.putInt("loop_counter: "+currentDate, loopCounter);
                    tasbihPreferenceEditor.putInt("total_counter: "+currentDate, totalCounter);
                    tasbihPreferenceEditor.apply();

                }
                break;
            case R.id.imageView9:
                tasbihCounter = 0;
                loopCounter = 0;
                totalCounter = 0;
                if(localePreferences.contains("Current_Language")){
                    String locale = localePreferences.getString("Current_Language","");
                    if(locale.equals("bn")) {
                        tasbihText.setText(banglaConverter(String.valueOf(tasbihCounter)));
                        loopText.setText(banglaConverter(String.valueOf(loopCounter)));
                        totalText.setText(banglaConverter(String.valueOf(totalCounter)));
                    }

                    else if(locale.equals("en")) {
                        tasbihText.setText(String.valueOf(tasbihCounter));
                        loopText.setText(String.valueOf(loopCounter));
                        totalText.setText(String.valueOf(totalCounter));
                    }
                }


                tasbihPreferenceEditor.putString("tasbih_date", currentDate);
                tasbihPreferenceEditor.putInt("tasbih_counter: "+currentDate, tasbihCounter);
                tasbihPreferenceEditor.putInt("loop_counter: "+currentDate, loopCounter);
                tasbihPreferenceEditor.putInt("total_counter: "+currentDate, totalCounter);
                tasbihPreferenceEditor.apply();
                break;

            case R.id.imageView10:

                if(vibeFlag) {
                    vibeFlag = false;
                    feedbackBtn.setImageResource(feedback_disabled);
                    feedbackText.setTextColor(getResources().getColor(R.color.background));
                    tasbihPreferenceEditor.putBoolean("vibration", vibeFlag);
                    tasbihPreferenceEditor.apply();
                }
                else{
                    vibeFlag = true;
                    feedbackBtn.setImageResource(feedback);
                    feedbackText.setTextColor(getResources().getColor(R.color.lime));
                    tasbihPreferenceEditor.putBoolean("vibration", vibeFlag);
                    tasbihPreferenceEditor.apply();
                }

                break;
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

        if(getActivity() == null){
            return;
        }

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

                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_tasbih_to_navigation_dashboard);
            }
        });
        anim.start();

    }

    public String banglaConverter(String eng){

        char[] charArray = eng.toCharArray();
        StringBuilder stringBuilder = new StringBuilder(charArray.length);


        for (int i=0; i<charArray.length; i++ ){

            char character = charArray[i];

            switch (character){
                case '0':
                    stringBuilder.append("০");
                    break;
                case '1':
                    stringBuilder.append("১");
                    break;
                case '2':
                    stringBuilder.append("২");
                    break;
                case '3':
                    stringBuilder.append("৩");
                    break;
                case '4':
                    stringBuilder.append("৪");
                    break;
                case '5':
                    stringBuilder.append("৫");
                    break;
                case '6':
                    stringBuilder.append("৬");
                    break;
                case '7':
                    stringBuilder.append("৭");
                    break;
                case '8':
                    stringBuilder.append("৮");
                    break;
                case '9':
                    stringBuilder.append("৯");
                    break;
            }
        }

        return stringBuilder.toString();
    }


}