package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LeaderBoardModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("temp_team_id")
    @Expose
    private String tempTeamId;
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("contest_display_type_id")
    @Expose
    private String contestDisplayTypeId;
    @SerializedName("temp_team_name")
    @Expose
    private String tempTeamName;
    @SerializedName("deposit_bal")
    @Expose
    private String depositBal;
    @SerializedName("bonus_bal")
    @Expose
    private String bonusBal;
    @SerializedName("win_bal")
    @Expose
    private String winBal;
    @SerializedName("transfer_bal")
    @Expose
    private String transferBal;
    @SerializedName("donation_bal")
    @Expose
    private String donationBal;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("total_rank")
    @Expose
    private String totalRank;
    @SerializedName("user_img")
    @Expose
    private String user_img;
    @SerializedName("total_win_amount")
    @Expose
    private String totalWinAmount;
    @SerializedName("total_bonus_amount")
    @Expose
    private String totalBonusAmount;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;
    @SerializedName("contest_status")
    @Expose
    private String contestStatus;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;

    @SerializedName("gender")
    @Expose
    private String gender="";

    @SerializedName("dob")
    @Expose
    private String dob="";
    private boolean selected = false;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempTeamId() {
        return tempTeamId;
    }

    public void setTempTeamId(String tempTeamId) {
        this.tempTeamId = tempTeamId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getContestDisplayTypeId() {
        return contestDisplayTypeId;
    }

    public void setContestDisplayTypeId(String contestDisplayTypeId) {
        this.contestDisplayTypeId = contestDisplayTypeId;
    }

    public String getTempTeamName() {
        return tempTeamName;
    }

    public void setTempTeamName(String tempTeamName) {
        this.tempTeamName = tempTeamName;
    }

    public String getDepositBal() {
        return depositBal;
    }

    public void setDepositBal(String depositBal) {
        this.depositBal = depositBal;
    }

    public String getBonusBal() {
        return bonusBal;
    }

    public void setBonusBal(String bonusBal) {
        this.bonusBal = bonusBal;
    }

    public String getWinBal() {
        return winBal;
    }

    public void setWinBal(String winBal) {
        this.winBal = winBal;
    }

    public String getTransferBal() {
        return transferBal;
    }

    public void setTransferBal(String transferBal) {
        this.transferBal = transferBal;
    }

    public String getDonationBal() {
        return donationBal;
    }

    public void setDonationBal(String donationBal) {
        this.donationBal = donationBal;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
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

    public String getTotalBonusAmount() {
        return totalBonusAmount;
    }

    public void setTotalBonusAmount(String totalBonusAmount) {
        this.totalBonusAmount = totalBonusAmount;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getContestStatus() {
        return contestStatus;
    }

    public void setContestStatus(String contestStatus) {
        this.contestStatus = contestStatus;
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
