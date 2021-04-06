package com.qubitech.ramadanapp.ui.quibla;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.qubitech.ramadanapp.R;

public class CompassFragment extends Fragment {

    private static final String TAG = "CompassActivity";

    private Compass compass;
    private ImageView arrowView;
    private TextView sotwLabel;

    private float currentAzimuth;

    private static final int[] sides = {0, 45, 90, 135, 180, 225, 270, 315, 360};
    private static String[] names = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quibla, container, false);

        arrowView = root.findViewById(R.id.main_image_hands);
        sotwLabel = root.findViewById(R.id.sotw_label);

        initLocalizedNames(getActivity().getApplicationContext());
        setupCompass();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
    }

    private void setupCompass() {
        compass = new Compass(getActivity().getApplicationContext());
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
    }

    private void adjustArrow(float azimuth) {
        Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth);

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    private void adjustSotwLabel(float azimuth) {
        sotwLabel.setText(format(azimuth));
    }

    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustArrow(azimuth);
                        adjustSotwLabel(azimuth);
                    }
                });
            }
        };
    }


    public String format(float azimuth) {
        int iAzimuth = (int)azimuth;
        int index = findClosestIndex(iAzimuth);
        return iAzimuth + "° " + names[index];
    }

    private void initLocalizedNames(Context context) {
        // it will be localized version of
        // {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"}
        // yes, N is twice, for 0 and for 360

        if (names == null) {
            names = new String[]{
                    context.getString(R.string.sotw_north),
                    context.getString(R.string.sotw_northeast),
                    context.getString(R.string.sotw_east),
                    context.getString(R.string.sotw_southeast),
                    context.getString(R.string.sotw_south),
                    context.getString(R.string.sotw_southwest),
                    context.getString(R.string.sotw_west),
                    context.getString(R.string.sotw_northwest),
                    context.getString(R.string.sotw_north)
            };
        }
    }

    /**
     * Finds index of the closest element to identify Side Of The World label
     * @param target
     * @return index of the closest element
     */
    private static int findClosestIndex(int target) {
        // in the original binary search https://www.geeksforgeeks.org/find-closest-number-array/
        // you will see more steps to reduce the time
        // in in this particular case the corner conditions are never true
        // e.g. azimuth is never negative, so there is no point to check
        // these conditions. Also we don't check if target is equal to element of array,
        // because most of the time it's not.

        // and the main difference is it finds the index, not the value

        // Doing binary search
        int i = 0, j = sides.length, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            /* If target is less than array element,
               then search in left */
            if (target < sides[mid]) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target);
                }

                /* Repeat for left half */
                j = mid;
            } else {
                if (mid < sides.length-1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target);
                }
                i = mid + 1; // update i
            }
        }

        // Only single element left after search
        return mid;
    }

    // Method to compare which one is the more close
    // We find the closest by taking the difference
    // between the target and both values. It assumes
    // that val2 is greater than val1 and target lies
    // between these two.
    private static int getClosest(int index1, int index2, int target) {
        if (target - sides[index1] >= sides[index2] - target) {
            return index2;
        }
        return index1;
    }
}