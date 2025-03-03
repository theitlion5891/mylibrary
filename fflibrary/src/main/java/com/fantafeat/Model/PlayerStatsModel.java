package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerStatsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("team_1_sname")
    @Expose
    private String team1Sname;
    @SerializedName("team_2_sname")
    @Expose
    private String team2Sname;
    @SerializedName("match_start_date")
    @Expose
    private String matchStartDate;
    @SerializedName("safe_match_start_time")
    @Expose
    private String safeMatchStartTime;
    @SerializedName("regular_match_start_time")
    @Expose
    private String regularMatchStartTime;
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
    @SerializedName("player_name")
    @Expose
    private String playerName;
    @SerializedName("player_sname")
    @Expose
    private String playerSname;
    @SerializedName("player_image")
    @Expose
    private String playerImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeam1Sname() {
        return team1Sname;
    }

    public void setTeam1Sname(String team1Sname) {
        this.team1Sname = team1Sname;
    }

    public String getTeam2Sname() {
        return team2Sname;
    }

    public void setTeam2Sname(String team2Sname) {
        this.team2Sname = team2Sname;
    }

    public String getMatchStartDate() {
        return matchStartDate;
    }

    public void setMatchStartDate(String matchStartDate) {
        this.matchStartDate = matchStartDate;
    }

    public String getSafeMatchStartTime() {
        return safeMatchStartTime;
    }

    public void setSafeMatchStartTime(String safeMatchStartTime) {
        this.safeMatchStartTime = safeMatchStartTime;
    }

    public String getRegularMatchStartTime() {
        return regularMatchStartTime;
    }

    public void setRegularMatchStartTime(String regularMatchStartTime) {
        this.regularMatchStartTime = regularMatchStartTime;
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

}
