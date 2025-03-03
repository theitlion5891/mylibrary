package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LudoTournamentUserModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("tournament_id")
    @Expose
    private String tournamentId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("con_code")
    @Expose
    private String conCode;
    @SerializedName("use_deposit_amt")
    @Expose
    private String useDepositAmt;
    @SerializedName("use_bonus_amt")
    @Expose
    private String useBonusAmt;
    @SerializedName("use_trans_amt")
    @Expose
    private String useTransAmt;
    @SerializedName("use_win_amt")
    @Expose
    private String useWinAmt;
    @SerializedName("use_coin_amt")
    @Expose
    private String useCoinAmt;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("total_rank")
    @Expose
    private String totalRank;
    @SerializedName("total_win_amt")
    @Expose
    private String totalWinAmt;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("game_play_status")
    @Expose
    private String gamePlayStatus;

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

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getConCode() {
        return conCode;
    }

    public void setConCode(String conCode) {
        this.conCode = conCode;
    }

    public String getUseDepositAmt() {
        return useDepositAmt;
    }

    public void setUseDepositAmt(String useDepositAmt) {
        this.useDepositAmt = useDepositAmt;
    }

    public String getUseBonusAmt() {
        return useBonusAmt;
    }

    public void setUseBonusAmt(String useBonusAmt) {
        this.useBonusAmt = useBonusAmt;
    }

    public String getUseTransAmt() {
        return useTransAmt;
    }

    public void setUseTransAmt(String useTransAmt) {
        this.useTransAmt = useTransAmt;
    }

    public String getUseWinAmt() {
        return useWinAmt;
    }

    public void setUseWinAmt(String useWinAmt) {
        this.useWinAmt = useWinAmt;
    }

    public String getUseCoinAmt() {
        return useCoinAmt;
    }

    public void setUseCoinAmt(String useCoinAmt) {
        this.useCoinAmt = useCoinAmt;
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

    public String getTotalWinAmt() {
        return totalWinAmt;
    }

    public void setTotalWinAmt(String totalWinAmt) {
        this.totalWinAmt = totalWinAmt;
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

    public String getGamePlayStatus() {
        return gamePlayStatus;
    }

    public void setGamePlayStatus(String gamePlayStatus) {
        this.gamePlayStatus = gamePlayStatus;
    }

}
