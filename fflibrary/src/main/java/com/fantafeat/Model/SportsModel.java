package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SportsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sport_name")
    @Expose
    private String sportName;
    @SerializedName("sport_sname")
    @Expose
    private String sportSname;
    @SerializedName("sport_img")
    @Expose
    private String sportImg;
    @SerializedName("sport_order_no")
    @Expose
    private String sportOrderNo;
    @SerializedName("sport_default_display")
    @Expose
    private String sportDefaultDisplay;
    @SerializedName("sport_status")
    @Expose
    private String sportStatus;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("tab_1_max")
    @Expose
    private String tab_1_max;
    @SerializedName("tab_1_min")
    @Expose
    private String tab_1_min;
    @SerializedName("tab_2_max")
    @Expose
    private String tab_2_max;
    @SerializedName("tab_2_min")
    @Expose
    private String tab_2_min;
    @SerializedName("tab_3_max")
    @Expose
    private String tab_3_max;
    @SerializedName("tab_3_min")
    @Expose
    private String tab_3_min;
    @SerializedName("tab_4_max")
    @Expose
    private String tab_4_max;
    @SerializedName("tab_4_min")
    @Expose
    private String tab_4_min;
    @SerializedName("tab_5_min")
    @Expose
    private String tab_5_min;
    @SerializedName("tab_5_max")
    @Expose
    private String tab_5_mix;
    @SerializedName("tab_6_max")
    @Expose
    private String tab_6_max;
    @SerializedName("tab_6_min")
    @Expose
    private String tab_6_min;
    @SerializedName("team_1_max_player")
    @Expose
    private String team_1_max_player;
    @SerializedName("team_max_player")
    @Expose
    private String team_max_player;

    private boolean isPopupShow=false;

    public String getTab_1_max() {
        return tab_1_max;
    }

    public void setTab_1_max(String tab_1_max) {
        this.tab_1_max = tab_1_max;
    }

    public String getTab_1_min() {
        return tab_1_min;
    }

    public void setTab_1_min(String tab_1_min) {
        this.tab_1_min = tab_1_min;
    }

    public String getTab_2_max() {
        return tab_2_max;
    }

    public void setTab_2_max(String tab_2_max) {
        this.tab_2_max = tab_2_max;
    }

    public String getTab_2_min() {
        return tab_2_min;
    }

    public void setTab_2_min(String tab_2_min) {
        this.tab_2_min = tab_2_min;
    }

    public String getTab_3_max() {
        return tab_3_max;
    }

    public void setTab_3_max(String tab_3_max) {
        this.tab_3_max = tab_3_max;
    }

    public String getTab_3_min() {
        return tab_3_min;
    }

    public void setTab_3_min(String tab_3_min) {
        this.tab_3_min = tab_3_min;
    }

    public String getTab_4_max() {
        return tab_4_max;
    }

    public void setTab_4_max(String tab_4_max) {
        this.tab_4_max = tab_4_max;
    }

    public String getTab_4_min() {
        return tab_4_min;
    }

    public void setTab_4_min(String tab_4_min) {
        this.tab_4_min = tab_4_min;
    }

    public String getTab_5_min() {
        return tab_5_min;
    }

    public void setTab_5_min(String tab_5_min) {
        this.tab_5_min = tab_5_min;
    }

    public String getTab_5_mix() {
        return tab_5_mix;
    }

    public void setTab_5_mix(String tab_5_mix) {
        this.tab_5_mix = tab_5_mix;
    }

    public String getTab_6_max() {
        return tab_6_max;
    }

    public void setTab_6_max(String tab_6_max) {
        this.tab_6_max = tab_6_max;
    }

    public String getTab_6_min() {
        return tab_6_min;
    }

    public void setTab_6_min(String tab_6_min) {
        this.tab_6_min = tab_6_min;
    }

    public String getTeam_1_max_player() {
        return team_1_max_player;
    }

    public void setTeam_1_max_player(String team_1_max_player) {
        this.team_1_max_player = team_1_max_player;
    }

    public String getTeam_max_player() {
        return team_max_player;
    }

    public void setTeam_max_player(String team_max_player) {
        this.team_max_player = team_max_player;
    }

    public boolean isPopupShow() {
        return isPopupShow;
    }

    public void setPopupShow(boolean popupShow) {
        isPopupShow = popupShow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportSname() {
        return sportSname;
    }

    public void setSportSname(String sportSname) {
        this.sportSname = sportSname;
    }

    public String getSportImg() {
        return sportImg;
    }

    public void setSportImg(String sportImg) {
        this.sportImg = sportImg;
    }

    public String getSportOrderNo() {
        return sportOrderNo;
    }

    public void setSportOrderNo(String sportOrderNo) {
        this.sportOrderNo = sportOrderNo;
    }

    public String getSportDefaultDisplay() {
        return sportDefaultDisplay;
    }

    public void setSportDefaultDisplay(String sportDefaultDisplay) {
        this.sportDefaultDisplay = sportDefaultDisplay;
    }

    public String getSportStatus() {
        return sportStatus;
    }

    public void setSportStatus(String sportStatus) {
        this.sportStatus = sportStatus;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}