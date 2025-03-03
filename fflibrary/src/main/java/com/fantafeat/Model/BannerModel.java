package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("banner_action")
    @Expose
    private String bannerAction;
    @SerializedName("banner_web_view_url")
    @Expose
    private String bannerWebViewUrl;
    @SerializedName("banner_popup_image")
    @Expose
    private String bannerPopupImage;
    @SerializedName("banner_match_id")
    @Expose
    private String bannerMatchId;
    @SerializedName("banner_status")
    @Expose
    private String bannerStatus;
    @SerializedName("banner_start_time")
    @Expose
    private String bannerStartTime;
    @SerializedName("banner_end_time")
    @Expose
    private String bannerEndTime;
    @SerializedName("create_at")
    @Expose
    private String createAt;

    @SerializedName("sport_id")
    @Expose
    private String sport_id;

    @SerializedName("banner_contest_id")
    @Expose
    private String banner_contest_id;

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }

    public String getBanner_contest_id() {
        return banner_contest_id;
    }

    public void setBanner_contest_id(String banner_contest_id) {
        this.banner_contest_id = banner_contest_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerAction() {
        return bannerAction;
    }

    public void setBannerAction(String bannerAction) {
        this.bannerAction = bannerAction;
    }

    public String getBannerWebViewUrl() {
        return bannerWebViewUrl;
    }

    public void setBannerWebViewUrl(String bannerWebViewUrl) {
        this.bannerWebViewUrl = bannerWebViewUrl;
    }

    public String getBannerPopupImage() {
        return bannerPopupImage;
    }

    public void setBannerPopupImage(String bannerPopupImage) {
        this.bannerPopupImage = bannerPopupImage;
    }

    public String getBannerMatchId() {
        return bannerMatchId;
    }

    public void setBannerMatchId(String bannerMatchId) {
        this.bannerMatchId = bannerMatchId;
    }

    public String getBannerStatus() {
        return bannerStatus;
    }

    public void setBannerStatus(String bannerStatus) {
        this.bannerStatus = bannerStatus;
    }

    public String getBannerStartTime() {
        return bannerStartTime;
    }

    public void setBannerStartTime(String bannerStartTime) {
        this.bannerStartTime = bannerStartTime;
    }

    public String getBannerEndTime() {
        return bannerEndTime;
    }

    public void setBannerEndTime(String bannerEndTime) {
        this.bannerEndTime = bannerEndTime;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
