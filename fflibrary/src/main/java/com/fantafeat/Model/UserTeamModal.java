package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTeamModal {
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("sport_id")
    @Expose
    private String sport_id;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("total_rank")
    @Expose
    private String totalRank;
    @SerializedName("lb_master_type_id")
    @Expose
    private String lbMasterTypeId;
    @SerializedName("temp_team_id")
    @Expose
    private String tempTeamId;
    @SerializedName("temp_team_name")
    @Expose
    private String tempTeamName;
    @SerializedName("league_id")
    @Expose
    private String leagueId;
    @SerializedName("team_1")
    @Expose
    private String team1;
    @SerializedName("team_2")
    @Expose
    private String team2;
    @SerializedName("team_1_sname")
    @Expose
    private String team1Sname;
    @SerializedName("team_2_sname")
    @Expose
    private String team2Sname;
    @SerializedName("team_1_name")
    @Expose
    private String team1Name;
    @SerializedName("team_2_name")
    @Expose
    private String team2Name;
    @SerializedName("match_title")
    @Expose
    private String matchTitle;
    @SerializedName("team_1_img")
    @Expose
    private String team1Img;
    @SerializedName("team_2_img")
    @Expose
    private String team2Img;
    @SerializedName("match_start_date")
    @Expose
    private String matchStartDate;
    @SerializedName("safe_match_start_time")
    @Expose
    private String safeMatchStartTime;
    @SerializedName("regular_match_start_time")
    @Expose
    private String regularMatchStartTime;
    @SerializedName("match_type")
    @Expose
    private String matchType;

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
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

    public String getLbMasterTypeId() {
        return lbMasterTypeId;
    }

    public void setLbMasterTypeId(String lbMasterTypeId) {
        this.lbMasterTypeId = lbMasterTypeId;
    }

    public String getTempTeamId() {
        return tempTeamId;
    }

    public void setTempTeamId(String tempTeamId) {
        this.tempTeamId = tempTeamId;
    }

    public String getTempTeamName() {
        return tempTeamName;
    }

    public void setTempTeamName(String tempTeamName) {
        this.tempTeamName = tempTeamName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
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

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public String getTeam1Img() {
        return team1Img;
    }

    public void setTeam1Img(String team1Img) {
        this.team1Img = team1Img;
    }

    public String getTeam2Img() {
        return team2Img;
    }

    public void setTeam2Img(String team2Img) {
        this.team2Img = team2Img;
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

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
}
