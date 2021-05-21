package com.qubitech.ramadanapp.ui.quran.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurahDataModel {

    @SerializedName("no")
    @Expose
    private String surahNumber;

    @SerializedName("title")
    @Expose
    private String surahTitle;

    @SerializedName("arabicTitle")
    @Expose
    private String surahArabicTitle;

    @SerializedName("ayah")
    @Expose
    private String ayah;

    @SerializedName("revelationType")
    @Expose
    private String revelationType;

    @SerializedName("ayahTextArabic")
    @Expose
    private String ayahTextArabic;

    @SerializedName("ayahTextBangla")
    @Expose
    private String ayahTextBangla;

    @SerializedName("sajda")
    @Expose
    private String sajda;



    public SurahDataModel(String surahNumber, String surahTitle, String surahArabicTitle, String ayah) {
        this.surahNumber = surahNumber;
        this.surahTitle = surahTitle;
        this.surahArabicTitle = surahArabicTitle;
        this.ayah = ayah;
    }

    public SurahDataModel(String surahNumber, String ayah, String ayahTextArabic, String ayahTextBangla, String sajda) {
        this.surahNumber = surahNumber;
        this.ayah = ayah;
        this.ayahTextArabic = ayahTextArabic;
        this.ayahTextBangla = ayahTextBangla;
        this.sajda = sajda;
    }

    public String getSurahNumber() {
        return surahNumber;
    }

    public String getSurahTitle() {
        return surahTitle;
    }

    public String getSurahArabicTitle() {
        return surahArabicTitle;
    }

    public String getAyah() {
        return ayah;
    }

    public String getRevelationType() {
        return revelationType;
    }

    public String getAyahTextArabic() {
        return ayahTextArabic;
    }

    public String getAyahTextBangla() {
        return ayahTextBangla;
    }

    public String getSajda() {
        return sajda;
    }
}
