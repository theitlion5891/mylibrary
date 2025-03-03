package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AfterMatchPlayerStatesModel {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("point")
    @Expose
    private String point;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

}