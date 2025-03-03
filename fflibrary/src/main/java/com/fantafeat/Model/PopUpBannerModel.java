package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopUpBannerModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("banner_img")
    @Expose
    private String bannerImg;
    @SerializedName("banner_web_url")
    @Expose
    private String bannerWebUrl;
    @SerializedName("banner_force_close")
    @Expose
    private String bannerForceClose;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("banner_action")
    @Expose
    private String bannerAction;
    @SerializedName("banner_start_time")
    @Expose
    private String bannerStartTime;
    @SerializedName("banner_end_time")
    @Expose
    private String bannerEndTime;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("display_banner_count")
    @Expose
    private String displayBannerCount;
    @SerializedName("sport_id")
    @Expose
    private String sport_id;

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerWebUrl() {
        return bannerWebUrl;
    }

    public void setBannerWebUrl(String bannerWebUrl) {
        this.bannerWebUrl = bannerWebUrl;
    }

    public String getBannerForceClose() {
        return bannerForceClose;
    }

    public void setBannerForceClose(String bannerForceClose) {
        this.bannerForceClose = bannerForceClose;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBannerAction() {
        return bannerAction;
    }

    public void setBannerAction(String bannerAction) {
        this.bannerAction = bannerAction;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDisplayBannerCount() {
        return displayBannerCount;
    }

    public void setDisplayBannerCount(String displayBannerCount) {
        this.displayBannerCount = displayBannerCount;
    }

}