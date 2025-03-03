package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameLeaderboardModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("con_game_type")
    @Expose
    private String conGameType;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("total_rank")
    @Expose
    private String totalRank;
    @SerializedName("total_win_amount")
    @Expose
    private String totalWinAmount;
    @SerializedName("lb_master_type_id")
    @Expose
    private String lbMasterTypeId;
    @SerializedName("total_played_game")
    @Expose
    private String totalPlayedGame;
    @SerializedName("is_win_credit")
    @Expose
    private String isWinCredit;
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

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getConGameType() {
        return conGameType;
    }

    public void setConGameType(String conGameType) {
        this.conGameType = conGameType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getLbMasterTypeId() {
        return lbMasterTypeId;
    }

    public void setLbMasterTypeId(String lbMasterTypeId) {
        this.lbMasterTypeId = lbMasterTypeId;
    }

    public String getTotalPlayedGame() {
        return totalPlayedGame;
    }

    public void setTotalPlayedGame(String totalPlayedGame) {
        this.totalPlayedGame = totalPlayedGame;
    }

    public String getIsWinCredit() {
        return isWinCredit;
    }

    public void setIsWinCredit(String isWinCredit) {
        this.isWinCredit = isWinCredit;
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
