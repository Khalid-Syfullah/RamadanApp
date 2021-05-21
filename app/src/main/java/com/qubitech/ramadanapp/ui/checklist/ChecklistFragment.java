package com.qubitech.ramadanapp.ui.checklist;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.qubitech.ramadanapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChecklistFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    CardView checkListCardView;
    CalendarView checkListCalendarView;
    ImageView closeBtn;
    boolean revealFlag = false, changeFlag= false;
    boolean dailyData[] = {false, false, false, false, false, false, false};
    SharedPreferences checklistPreferences;
    SharedPreferences.Editor checklistPreferencesEditor;
    String checklistPref = "Checklist", date=null;
    SimpleDateFormat simpleDateFormat;
    CheckBox fajrCheckbox, dhuhrCheckbox, asrCheckbox, maghribCheckbox, ishaCheckbox, fastingCheckbox, quranCheckbox;

    ChecklistViewModel mViewModel;

    public static ChecklistFragment newInstance() {
        return new ChecklistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_checklist, container, false);

        checkListCardView = view.findViewById(R.id.checklist_cardView);
        checkListCalendarView = view.findViewById(R.id.checklist_calendarView);
        closeBtn = view.findViewById(R.id.checklist_closeBtn);

        fajrCheckbox = view.findViewById(R.id.checklist_fajr_checkbox);
        dhuhrCheckbox = view.findViewById(R.id.checklist_dhuhr_checkbox);
        asrCheckbox = view.findViewById(R.id.checklist_asr_checkbox);
        maghribCheckbox = view.findViewById(R.id.checklist_maghrib_checkbox);
        ishaCheckbox = view.findViewById(R.id.checklist_isha_checkbox);
        fastingCheckbox = view.findViewById(R.id.checklist_fasting_checkbox);
        quranCheckbox = view.findViewById(R.id.checklist_quran_checkbox);

        checkListCalendarView.setDateTextAppearance(getResources().getIdentifier("CalendarDate","style",getActivity().getPackageName()));
        checkListCalendarView.setWeekDayTextAppearance(getResources().getIdentifier("CalendarWeek","style",getActivity().getPackageName()));
        checkListCalendarView.setFirstDayOfWeek(7);
        checkListCalendarView.setDate(Calendar.getInstance().getTimeInMillis());

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        date = simpleDateFormat.format(Calendar.getInstance().getTime());

        dailyData = getChecklist(date);

        fajrCheckbox.setChecked(dailyData[0]);
        dhuhrCheckbox.setChecked(dailyData[1]);
        asrCheckbox.setChecked(dailyData[2]);
        maghribCheckbox.setChecked(dailyData[3]);
        ishaCheckbox.setChecked(dailyData[4]);
        quranCheckbox.setChecked(dailyData[5]);
        fastingCheckbox.setChecked(dailyData[6]);

        changeFlag = true;

        checkListCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {


                Calendar calendar=new GregorianCalendar(i, i1, i2);
                date = simpleDateFormat.format(calendar.getTime());

                dailyData = getChecklist(date);

                changeFlag = false;

                fajrCheckbox.setChecked(dailyData[0]);
                dhuhrCheckbox.setChecked(dailyData[1]);
                asrCheckbox.setChecked(dailyData[2]);
                maghribCheckbox.setChecked(dailyData[3]);
                ishaCheckbox.setChecked(dailyData[4]);
                quranCheckbox.setChecked(dailyData[5]);
                fastingCheckbox.setChecked(dailyData[6]);

                changeFlag = true;


            }
        });

        fajrCheckbox.setOnCheckedChangeListener(this);
        dhuhrCheckbox.setOnCheckedChangeListener(this);
        asrCheckbox.setOnCheckedChangeListener(this);
        maghribCheckbox.setOnCheckedChangeListener(this);
        ishaCheckbox.setOnCheckedChangeListener(this);
        fastingCheckbox.setOnCheckedChangeListener(this);
        quranCheckbox.setOnCheckedChangeListener(this);


        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!revealFlag) {
                    revealFAB(checkListCardView);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
                    animation.setDuration(500);
                    closeBtn.setAnimation(animation);
                    revealFlag = true;
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB(checkListCardView);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChecklistViewModel.class);


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(changeFlag) {
            dailyData[0] = fajrCheckbox.isChecked();
            dailyData[1] = dhuhrCheckbox.isChecked();
            dailyData[2] = asrCheckbox.isChecked();
            dailyData[3] = maghribCheckbox.isChecked();
            dailyData[4] = ishaCheckbox.isChecked();
            dailyData[5] = fastingCheckbox.isChecked();
            dailyData[6] = quranCheckbox.isChecked();

            saveChecklist(date, dailyData);
        }
    }

    private boolean[] getChecklist(String currentDate){

        checklistPreferences = getActivity().getSharedPreferences(checklistPref, Context.MODE_PRIVATE);
        boolean fajr=false, dhuhr=false, asr=false, maghrib=false, isha=false, fasting=false, quran=false;

        if(checklistPreferences.contains("checklist_date: "+currentDate)){

            if(checklistPreferences.getString("checklist_date: "+currentDate,"").equals(currentDate)){
                fajr = checklistPreferences.getBoolean("fajr: "+currentDate,false);
                dhuhr = checklistPreferences.getBoolean("dhuhr: "+currentDate,false);
                asr = checklistPreferences.getBoolean("asr: "+currentDate,false);
                maghrib = checklistPreferences.getBoolean("maghrib: "+currentDate,false);
                isha = checklistPreferences.getBoolean("isha: "+currentDate,false);
                fasting = checklistPreferences.getBoolean("fasting: "+currentDate,false);
                quran = checklistPreferences.getBoolean("quran: "+currentDate,false);

            }
        }
        return new boolean [] {fajr, dhuhr, asr, maghrib, isha, fasting, quran};
    }

    private void saveChecklist(String currentDate, boolean [] data){
        checklistPreferences = getActivity().getSharedPreferences(checklistPref, Context.MODE_PRIVATE);
        checklistPreferencesEditor = checklistPreferences.edit();

        checklistPreferencesEditor.putString("checklist_date: "+currentDate, currentDate);
        checklistPreferencesEditor.putBoolean("fajr: "+currentDate, data[0]);
        checklistPreferencesEditor.putBoolean("dhuhr: "+currentDate, data[1]);
        checklistPreferencesEditor.putBoolean("asr: "+currentDate, data[2]);
        checklistPreferencesEditor.putBoolean("maghrib: "+currentDate, data[3]);
        checklistPreferencesEditor.putBoolean("isha: "+currentDate, data[4]);
        checklistPreferencesEditor.putBoolean("fasting: "+currentDate, data[5]);
        checklistPreferencesEditor.putBoolean("quran: "+currentDate, data[6]);

        checklistPreferencesEditor.apply();

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