package com.qubitech.ramadanapp.ui.dua.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DuaDataModel {

    @SerializedName("type")
    @Expose
    private String duaType;

    @SerializedName("title")
    @Expose
    private String duaTitle;

    @SerializedName("body")
    @Expose
    private String duaBody;

    public DuaDataModel(String duaType, String duaTitle, String duaBody) {
        this.duaType = duaType;
        this.duaTitle = duaTitle;
        this.duaBody = duaBody;
    }

    public String getDuaType() {
        return duaType;
    }

    public void setDuaType(String duaType) {
        this.duaType = duaType;
    }

    public String getDuaTitle() {
        return duaTitle;
    }

    public void setDuaTitle(String duaTitle) {
        this.duaTitle = duaTitle;
    }

    public String getDuaBody() {
        return duaBody;
    }

    public void setDuaBody(String duaBody) {
        this.duaBody = duaBody;
    }
}


