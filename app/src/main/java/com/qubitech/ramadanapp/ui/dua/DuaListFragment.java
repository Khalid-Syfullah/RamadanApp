package com.qubitech.ramadanapp.ui.dua;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubitech.ramadanapp.R;
import com.qubitech.ramadanapp.ui.dua.dataModel.DuaDataModel;
import com.qubitech.ramadanapp.ui.dua.recyclerView.DuaAdapter;

import java.util.ArrayList;


public class DuaListFragment extends Fragment {


    String arg;
    CardView duaListCardView;
    RecyclerView duaRecyclerView;
    TextView duaListTitle;
    ImageView closeBtn, backBtn;


    public DuaListFragment() {
        // Required empty public constructor
    }

    public static DuaListFragment newInstance() {
        DuaListFragment fragment = new DuaListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dua_list, container, false);

        duaListCardView = view.findViewById(R.id.dualist_cardView);
        duaListTitle = view.findViewById(R.id.dualist_title);
        backBtn = view.findViewById(R.id.dualist_backBtn);
        closeBtn = view.findViewById(R.id.dualist_closeBtn);
        duaRecyclerView = view.findViewById(R.id.dualist_dua_recyclerView);
       

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        ArrayList<DuaDataModel> duaDataModels = new ArrayList<>();
        DuaDataModel duaDataModel;

        if (getArguments() != null) {
            arg = getArguments().getString("fragment");

            if(arg.equals("salah")){

                duaListTitle.setText(getResources().getString(R.string.dua_salah_title));

                duaDataModel = new DuaDataModel("salah","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।");
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
            }
            else if(arg.equals("siyam")){

                duaListTitle.setText(getResources().getString(R.string.dua_fasting_title));

                duaDataModel = new DuaDataModel("siyam","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।");
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
            }
            else if(arg.equals("misc")){

                duaListTitle.setText(getResources().getString(R.string.dua_misc_title));

                duaDataModel = new DuaDataModel("misc","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।");
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);


            }
            else if(arg.equals("zikir")){

                duaListTitle.setText(getResources().getString(R.string.dua_zikir_title));

                duaDataModel = new DuaDataModel("zikir","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু","অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।অর্থহীন লেখা যার মাঝে আছে অনেক কিছু। অর্থহীন লেখা যার মাঝে আছে অনেক কিছু।");
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);
                duaDataModels.add(duaDataModel);

            }



            DuaAdapter duaAdapter = new DuaAdapter(getActivity(), duaDataModels);
            duaRecyclerView.setVisibility(View.VISIBLE);
            duaRecyclerView.setLayoutManager(linearLayoutManager);
            duaRecyclerView.setAdapter(duaAdapter);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dualist_to_navigation_dua);

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB(duaListCardView);
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
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_navigation_dualist_to_navigation_dashboard);
            }
        });
        anim.start();

    }
}