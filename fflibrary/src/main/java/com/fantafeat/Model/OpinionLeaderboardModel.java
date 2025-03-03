package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpinionLeaderboardModel {

    @SerializedName("option_value")
    @Expose
    private String optionValue;
    @SerializedName("my_total_trades_count")
    @Expose
    private String myTotalTradesCount;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("total_rank")
    @Expose
    private String totalRank;
    @SerializedName("win_amount")
    @Expose
    private String winAmount;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("user_team_name")
    @Expose
    private String user_team_name;

    private boolean isMyOpinion;

    public String getUser_team_name() {
        return user_team_name;
    }

    public void setUser_team_name(String user_team_name) {
        this.user_team_name = user_team_name;
    }

    public boolean isMyOpinion() {
        return isMyOpinion;
    }

    public void setMyOpinion(boolean myOpinion) {
        isMyOpinion = myOpinion;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getMyTotalTradesCount() {
        return myTotalTradesCount;
    }

    public void setMyTotalTradesCount(String myTotalTradesCount) {
        this.myTotalTradesCount = myTotalTradesCount;
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

    public String getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(String winAmount) {
        this.winAmount = winAmount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
