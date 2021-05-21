package com.qubitech.ramadanapp.staticdata;

import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

public class StaticData {

    public static Double latitude=null;
    public static Double longitude=null;

    public static String fajrTime="";
    public static String dhuhrTime="";
    public static String asrTime="";
    public static String maghribTime="";
    public static String ishaTime="";
    public static String imsakTime="";

    public static String fajrNextTime="";
    public static String dhuhrNextTime="";
    public static String asrNextTime="";
    public static String maghribNextTime="";
    public static String ishaNextTime="";
    public static String imsakNextTime="";

    public static String waqtNext="";
    public static String waqtNextTime="";
    public static String waqtNextTimeLeft="";
    public static String sahriNextTime="";
    public static String iftarNextTime="";
    public static String iftarNextTimeLeft="";

    public static int waqtNextProgress=0;
    public static int iftarNextProgress=0;
    public static boolean permissionGranted = false;
    public static boolean duaRevealFlag = false;

    public static MediaPlayer mediaPlayer;
    public static String mediaTitle="";
    public static String mediaStatus="";
    public static String mediaUrl="";
    public static boolean isMediaActive=false;
    public static boolean isMediaReset=true;
    public static Fragment currentFragment;






}
