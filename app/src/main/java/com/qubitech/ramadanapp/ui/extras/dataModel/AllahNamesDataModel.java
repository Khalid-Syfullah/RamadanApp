package com.qubitech.ramadanapp.ui.extras.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllahNamesDataModel {


    @SerializedName("mainName")
    @Expose
    private String mainName;

    @SerializedName("arabicName")
    @Expose
    private String arabicName;

    @SerializedName("translatedName")
    @Expose
    private String translatedName;

    public AllahNamesDataModel(String mainName, String arabicName, String translatedName) {
        this.mainName = mainName;
        this.arabicName = arabicName;
        this.translatedName = translatedName;
    }

    public String getMainName() {
        return mainName;
    }

    public String getArabicName() {
        return arabicName;
    }

    public String getTranslatedName() {
        return translatedName;
    }
}


