package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LudoTournamentModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("game_type")
    @Expose
    private String gameType;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("game_time")
    @Expose
    private String game_time;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("no_of_spot")
    @Expose
    private String noOfSpot;
    @SerializedName("entry_fee")
    @Expose
    private String entryFee;
    @SerializedName("dist_amt")
    @Expose
    private String distAmt;
    @SerializedName("pf_amt")
    @Expose
    private String pfAmt;
    @SerializedName("max_join_spot")
    @Expose
    private String maxJoinSpot;
    @SerializedName("total_join_spot")
    @Expose
    private String totalJoinSpot;
    @SerializedName("total_winners")
    @Expose
    private String totalWinners;
    @SerializedName("is_win_credited")
    @Expose
    private String isWinCredited;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("win_credit_time")
    @Expose
    private Object winCreditTime;
    @SerializedName("win_credit_by")
    @Expose
    private Object winCreditBy;
    @SerializedName("my_join_cnt")
    @Expose
    private String myJoinCnt;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("winning_tree")
    @Expose
    private String winning_tree;
    @SerializedName("user_token_position")
    @Expose
    private String user_token_position;

    public String getUser_token_position() {
        return user_token_position;
    }

    public void setUser_token_position(String user_token_position) {
        this.user_token_position = user_token_position;
    }

    public String getWinning_tree() {
        return winning_tree;
    }

    public void setWinning_tree(String winning_tree) {
        this.winning_tree = winning_tree;
    }

    public String getGame_time() {
        return game_time;
    }

    public void setGame_time(String game_time) {
        this.game_time = game_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNoOfSpot() {
        return noOfSpot;
    }

    public void setNoOfSpot(String noOfSpot) {
        this.noOfSpot = noOfSpot;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getDistAmt() {
        return distAmt;
    }

    public void setDistAmt(String distAmt) {
        this.distAmt = distAmt;
    }

    public String getPfAmt() {
        return pfAmt;
    }

    public void setPfAmt(String pfAmt) {
        this.pfAmt = pfAmt;
    }

    public String getMaxJoinSpot() {
        return maxJoinSpot;
    }

    public void setMaxJoinSpot(String maxJoinSpot) {
        this.maxJoinSpot = maxJoinSpot;
    }

    public String getTotalJoinSpot() {
        return totalJoinSpot;
    }

    public void setTotalJoinSpot(String totalJoinSpot) {
        this.totalJoinSpot = totalJoinSpot;
    }

    public String getTotalWinners() {
        return totalWinners;
    }

    public void setTotalWinners(String totalWinners) {
        this.totalWinners = totalWinners;
    }

    public String getIsWinCredited() {
        return isWinCredited;
    }

    public void setIsWinCredited(String isWinCredited) {
        this.isWinCredited = isWinCredited;
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

    public Object getWinCreditTime() {
        return winCreditTime;
    }

    public void setWinCreditTime(Object winCreditTime) {
        this.winCreditTime = winCreditTime;
    }

    public Object getWinCreditBy() {
        return winCreditBy;
    }

    public void setWinCreditBy(Object winCreditBy) {
        this.winCreditBy = winCreditBy;
    }

    public String getMyJoinCnt() {
        return myJoinCnt;
    }

    public void setMyJoinCnt(String myJoinCnt) {
        this.myJoinCnt = myJoinCnt;
    }

}
