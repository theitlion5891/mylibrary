package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromotorsUserModel implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("ref_by")
    @Expose
    private String refBy;
    @SerializedName("user_img")
    @Expose
    private String user_img;
    @SerializedName("inf_lb_master_type_id")
    @Expose
    private String infLbMasterTypeId;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("total_rank")
    @Expose
    private String totalRank;
    @SerializedName("total_win_amount")
    @Expose
    private String totalWinAmount;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("asset_url")
    @Expose
    private String asset_url;

    private String lType;
    private String winning_dec;

    public String getAsset_url() {
        return asset_url;
    }

    public void setAsset_url(String asset_url) {
        this.asset_url = asset_url;
    }

    public String getWinning_dec() {
        return winning_dec;
    }

    public void setWinning_dec(String winning_dec) {
        this.winning_dec = winning_dec;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getlType() {
        return lType;
    }

    public void setlType(String lType) {
        this.lType = lType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefBy() {
        return refBy;
    }

    public void setRefBy(String refBy) {
        this.refBy = refBy;
    }

    public String getInfLbMasterTypeId() {
        return infLbMasterTypeId;
    }

    public void setInfLbMasterTypeId(String infLbMasterTypeId) {
        this.infLbMasterTypeId = infLbMasterTypeId;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(String totalRank) {
        this.totalRank = totalRank;
    }

    public String getTotalWinAmount() {
        return totalWinAmount;
    }

    public void setTotalWinAmount(String totalWinAmount) {
        this.totalWinAmount = totalWinAmount;
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
