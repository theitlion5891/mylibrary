package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("sport_id")
    @Expose
    private String sportId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("offer_type")
    @Expose
    private String offerType;
    @SerializedName("is_bonus")
    @Expose
    private String isBonus;
    @SerializedName("min_team")
    @Expose
    private String minTeam;
    @SerializedName("max_team")
    @Expose
    private String maxTeam;
    @SerializedName("offer_text")
    @Expose
    private String offerText;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("offer_start_time")
    @Expose
    private String offerStartTime;
    @SerializedName("offer_end_time")
    @Expose
    private String offerEndTime;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getIsBonus() {
        return isBonus;
    }

    public void setIsBonus(String isBonus) {
        this.isBonus = isBonus;
    }

    public String getMinTeam() {
        return minTeam;
    }

    public void setMinTeam(String minTeam) {
        this.minTeam = minTeam;
    }

    public String getMaxTeam() {
        return maxTeam;
    }

    public void setMaxTeam(String maxTeam) {
        this.maxTeam = maxTeam;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferStartTime() {
        return offerStartTime;
    }

    public void setOfferStartTime(String offerStartTime) {
        this.offerStartTime = offerStartTime;
    }

    public String getOfferEndTime() {
        return offerEndTime;
    }

    public void setOfferEndTime(String offerEndTime) {
        this.offerEndTime = offerEndTime;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

}
