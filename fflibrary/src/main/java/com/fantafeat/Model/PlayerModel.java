package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayerModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("temp_team_id")
    @Expose
    private String tempTeamId;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("contest_display_type_id")
    @Expose
    private String contestDisplayTypeId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("player_id")
    @Expose
    private String playerId;
    @SerializedName("player_name")
    @Expose
    private String playerName;
    @SerializedName("player_sname")
    @Expose
    private String playerSname;
    @SerializedName("other_text")
    @Expose
    private String other_text;
    @SerializedName("other_text2")
    @Expose
    private String other_text2;
    @SerializedName("player_image")
    @Expose
    private String playerImage;
    @SerializedName("player_type")
    @Expose
    private String playerType;
    @SerializedName("player_avg_point")
    @Expose
    private String playerAvgPoint;
    @SerializedName("player_rank")
    @Expose
    private String playerRank;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("is_captain")
    @Expose
    private String isCaptain = "No";
    @SerializedName("is_wise_captain")
    @Expose
    private String isWiseCaptain = "No";
    @SerializedName("is_man_of_match")
    @Expose
    private String isManOfMatch;
    @SerializedName("player_point")
    @Expose
    private String playerPoint;
    @SerializedName("total_points")
    @Expose
    private String totalPoints;
    @SerializedName("playing_xi")
    @Expose
    private String playingXi;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("player_percent")
    @Expose
    private String player_percent;
    @SerializedName("cap_percent")
    @Expose
    private String cap_percent;
    @SerializedName("wise_cap_percent")
    @Expose
    private String wise_cap_percent;
    @SerializedName("batting_order_no")
    @Expose
    private String batting_order_no;

    private boolean isSelected=false;
    private boolean isPlayerInTeam=false;

    public boolean isPlayerInTeam() {
        return isPlayerInTeam;
    }

    public void setPlayerInTeam(boolean playerInTeam) {
        isPlayerInTeam = playerInTeam;
    }

    public String getBatting_order_no() {
        return batting_order_no;
    }

    public void setBatting_order_no(String batting_order_no) {
        this.batting_order_no = batting_order_no;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getOther_text() {
        return other_text;
    }

    public void setOther_text(String other_text) {
        this.other_text = other_text;
    }

    public String getOther_text2() {
        return other_text2;
    }

    public void setOther_text2(String other_text2) {
        this.other_text2 = other_text2;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPlayerAvgPoint() {
        return playerAvgPoint;
    }

    public void setPlayerAvgPoint(String playerAvgPoint) {
        this.playerAvgPoint = playerAvgPoint;
    }

    public String getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(String playerRank) {
        this.playerRank = playerRank;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getIsCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(String isCaptain) {
        this.isCaptain = isCaptain;
    }

    public String getIsWiseCaptain() {
        return isWiseCaptain;
    }

    public void setIsWiseCaptain(String isWiseCaptain) {
        this.isWiseCaptain = isWiseCaptain;
    }

    public String getIsManOfMatch() {
        return isManOfMatch;
    }

    public void setIsManOfMatch(String isManOfMatch) {
        this.isManOfMatch = isManOfMatch;
    }

    public String getPlayerPoint() {
        return playerPoint;
    }

    public void setPlayerPoint(String playerPoint) {
        this.playerPoint = playerPoint;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getPlayingXi() {
        return playingXi;
    }

    public void setPlayingXi(String playingXi) {
        this.playingXi = playingXi;
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