package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchPlayerStateModel {

    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("player_id")
    @Expose
    private String playerId;
    @SerializedName("player_name")
    @Expose
    private String playerName;
    @SerializedName("player_sname")
    @Expose
    private String playerSname;
    @SerializedName("player_image")
    @Expose
    private String playerImage;
    @SerializedName("player_type")
    @Expose
    private String playerType;
    @SerializedName("player_rank")
    @Expose
    private String playerRank;
    @SerializedName("player_avg_point")
    @Expose
    private String playerAvgPoint;
    @SerializedName("playing_xi")
    @Expose
    private String playingXi;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("captain_points")
    @Expose
    private String captainPoints;
    @SerializedName("vice_captain_points")
    @Expose
    private String viceCaptainPoints;
    @SerializedName("player_percent")
    @Expose
    private String player_percent;
    @SerializedName("cap_percent")
    @Expose
    private String cap_percent;
    @SerializedName("wise_cap_percent")
    @Expose
    private String wise_cap_percent;

    private boolean isCheck=false;

    private String corvc;

    public String getCorvc() {
        return corvc;
    }

    public void setCorvc(String corvc) {
        this.corvc = corvc;
    }

    public String getPlayer_percent() {
        return player_percent;
    }

    public void setPlayer_percent(String player_percent) {
        this.player_percent = player_percent;
    }

    public String getCap_percent() {
        return cap_percent;
    }

    public void setCap_percent(String cap_percent) {
        this.cap_percent = cap_percent;
    }

    public String getWise_cap_percent() {
        return wise_cap_percent;
    }

    public void setWise_cap_percent(String wise_cap_percent) {
        this.wise_cap_percent = wise_cap_percent;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerSname() {
        return playerSname;
    }

    public void setPlayerSname(String playerSname) {
        this.playerSname = playerSname;
    }

    public String getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(String playerRank) {
        this.playerRank = playerRank;
    }

    public String getPlayerAvgPoint() {
        return playerAvgPoint;
    }

    public void setPlayerAvgPoint(String playerAvgPoint) {
        this.playerAvgPoint = playerAvgPoint;
    }

    public String getPlayingXi() {
        return playingXi;
    }

    public void setPlayingXi(String playingXi) {
        this.playingXi = playingXi;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getCaptainPoints() {
        return captainPoints;
    }

    public void setCaptainPoints(String captainPoints) {
        this.captainPoints = captainPoints;
    }

    public String getViceCaptainPoints() {
        return viceCaptainPoints;
    }

    public void setViceCaptainPoints(String viceCaptainPoints) {
        this.viceCaptainPoints = viceCaptainPoints;
    }

}