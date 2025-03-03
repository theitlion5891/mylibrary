package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaderboardListModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lb_list_title")
    @Expose
    private String lbListTitle;
    @SerializedName("lb_list_sub_title")
    @Expose
    private String lbListSubTitle;
    @SerializedName("lb_list_image")
    @Expose
    private String lbListImage;
    @SerializedName("lb_list_date_time_text")
    @Expose
    private String lbListDateTimeText;
    @SerializedName("lb_order_no")
    @Expose
    private String lbOrderNo;
    @SerializedName("lb_status")
    @Expose
    private String lbStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLbListTitle() {
        return lbListTitle;
    }

    public void setLbListTitle(String lbListTitle) {
        this.lbListTitle = lbListTitle;
    }

    public String getLbListSubTitle() {
        return lbListSubTitle;
    }

    public void setLbListSubTitle(String lbListSubTitle) {
        this.lbListSubTitle = lbListSubTitle;
    }

    public String getLbListImage() {
        return lbListImage;
    }

    public void setLbListImage(String lbListImage) {
        this.lbListImage = lbListImage;
    }

    public String getLbListDateTimeText() {
        return lbListDateTimeText;
    }

    public void setLbListDateTimeText(String lbListDateTimeText) {
        this.lbListDateTimeText = lbListDateTimeText;
    }

    public String getLbOrderNo() {
        return lbOrderNo;
    }

    public void setLbOrderNo(String lbOrderNo) {
        this.lbOrderNo = lbOrderNo;
    }

    public String getLbStatus() {
        return lbStatus;
    }

    public void setLbStatus(String lbStatus) {
        this.lbStatus = lbStatus;
    }
}
