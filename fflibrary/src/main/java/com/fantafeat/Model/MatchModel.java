package com.fantafeat.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchModel {

    private int matchDisplayType;
    private ArrayList<BannerModel> bannerModels;
    private ArrayList<EventModel> tradingList;
    private ArrayList<StatusUserListModel> statusUserListModels;
    private List<MatchModel> otherMatchList;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("team_1_api_id")
    @Expose
    private String team_1_api_id;
    @SerializedName("team_2_api_id")
    @Expose
    private String team_2_api_id;
    @SerializedName("sport_id")
    @Expose
    private String sportId;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("used_api")
    @Expose
    private String usedApi;
    @SerializedName("team_1")
    @Expose
    private String team1;
    @SerializedName("mega_text")
    @Expose
    private String mega_text;
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
    @SerializedName("team_1_color")
    @Expose
    private String team1Color;
    @SerializedName("team_2_color")
    @Expose
    private String team2Color;
    @SerializedName("team_1_img")
    @Expose
    private String team1Img;
    @SerializedName("team_2_img")
    @Expose
    private String team2Img;
    @SerializedName("league_id")
    @Expose
    private String leagueId;
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
    @SerializedName("match_squad")
    @Expose
    private String matchSquad;
    @SerializedName("match_title")
    @Expose
    private String matchTitle;
    @SerializedName("match_venue")
    @Expose
    private String matchVenue;
    @SerializedName("match_ground")
    @Expose
    private String matchGround;
    @SerializedName("sport_details_id")
    @Expose
    private String sportDetailsId;
    @SerializedName("display_is_safe")
    @Expose
    private String displayIsSafe;
    @SerializedName("display_is_regular")
    @Expose
    private String displayIsRegular;
    @SerializedName("match_status")
    @Expose
    private String matchStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("show_scorecard")
    @Expose
    private String showScorecard;
    @SerializedName("match_desc")
    @Expose
    private String matchDesc;
    @SerializedName("match_dtls_update_id")
    @Expose
    private String matchDtlsUpdateId;
    @SerializedName("team_a_xi")
    @Expose
    private String teamAXi;
    @SerializedName("team_b_xi")
    @Expose
    private String teamBXi;
    @SerializedName("team_1_inn_1_score")
    @Expose
    private String team1Inn1Score;
    @SerializedName("team_2_inn_1_score")
    @Expose
    private String team2Inn1Score;
    @SerializedName("team_1_inn_2_score")
    @Expose
    private String team1Inn2Score;
    @SerializedName("team_2_inn_2_score")
    @Expose
    private String team2Inn2Score;
    @SerializedName("total_entry_fees")
    @Expose
    private String totalEntryFees;
    @SerializedName("joined_teamp_team")
    @Expose
    private String joinedTeampTeam;
    @SerializedName("joined_contest")
    @Expose
    private String joinedContest;
    @SerializedName("total_win_amount")
    @Expose
    private String totalWinAmount;
    @SerializedName("total_win_bonus_amount")
    @Expose
    private String totalWinBonusAmount;
    @SerializedName("level_1_point")
    @Expose
    private String level1Point;
    @SerializedName("level_2_point")
    @Expose
    private String level2Point;
    @SerializedName("level_3_point")
    @Expose
    private String level3Point;
    private String ConDisplayType = "1";

    @SerializedName("lineup_change_msg")
    @Expose
    private String lineup_change_msg;

    @SerializedName("time_change_msg")
    @Expose
    private String time_change_msg;
    @SerializedName("is_single")
    @Expose
    private String is_single;
    @SerializedName("is_fifer")
    @Expose
    private String is_fifer;
    @SerializedName("is_male")
    @Expose
    private String is_male;

    public List<MatchModel> getOtherMatchList() {
        return otherMatchList;
    }

    public void setOtherMatchList(List<MatchModel> otherMatchList) {
        this.otherMatchList = otherMatchList;
    }

    public ArrayList<StatusUserListModel> getStatusUserListModels() {
        return statusUserListModels;
    }

    public void setStatusUserListModels(ArrayList<StatusUserListModel> statusUserListModels) {
        this.statusUserListModels = statusUserListModels;
    }

    public int getMatchDisplayType() {
        return matchDisplayType;
    }

    public void setMatchDisplayType(int matchDisplayType) {
        this.matchDisplayType = matchDisplayType;
    }

    public ArrayList<BannerModel> getBannerModels() {
        return bannerModels;
    }

    public void setBannerModels(ArrayList<BannerModel> bannerModels) {
        this.bannerModels = bannerModels;
    }

    public ArrayList<EventModel> getTradingList() {
        return tradingList;
    }

    public void setTradingList(ArrayList<EventModel> tradingList) {
        this.tradingList = tradingList;
    }

    public String getIs_male() {
        return is_male;
    }

    public void setIs_male(String is_male) {
        this.is_male = is_male;
    }

    public String getIs_single() {
        return is_single;
    }

    public void setIs_single(String is_single) {
        this.is_single = is_single;
    }

    public String getIs_fifer() {
        return is_fifer;
    }

    public void setIs_fifer(String is_fifer) {
        this.is_fifer = is_fifer;
    }

    public String getTime_change_msg() {
        return time_change_msg;
    }

    public void setTime_change_msg(String time_change_msg) {
        this.time_change_msg = time_change_msg;
    }

    public String getLineup_change_msg() {
        return lineup_change_msg;
    }

    public void setLineup_change_msg(String lineup_change_msg) {
        this.lineup_change_msg = lineup_change_msg;
    }

    public String getTeam_1_api_id() {
        return team_1_api_id;
    }

    public void setTeam_1_api_id(String team_1_api_id) {
        this.team_1_api_id = team_1_api_id;
    }

    public String getTeam_2_api_id() {
        return team_2_api_id;
    }

    public void setTeam_2_api_id(String team_2_api_id) {
        this.team_2_api_id = team_2_api_id;
    }

    private boolean isOpen = false;

    public String getMega_text() {
        return mega_text;
    }

    public void setMega_text(String mega_text) {
        this.mega_text = mega_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUsedApi() {
        return usedApi;
    }

    public void setUsedApi(String usedApi) {
        this.usedApi = usedApi;
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

    public String getTeam1Color() {
        return team1Color;
    }

    public void setTeam1Color(String team1Color) {
        this.team1Color = team1Color;
    }

    public String getTeam2Color() {
        return team2Color;
    }

    public void setTeam2Color(String team2Color) {
        this.team2Color = team2Color;
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

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
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

    public String getMatchSquad() {
        return matchSquad;
    }

    public void setMatchSquad(String matchSquad) {
        this.matchSquad = matchSquad;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public String getMatchVenue() {
        return matchVenue;
    }

    public void setMatchVenue(String matchVenue) {
        this.matchVenue = matchVenue;
    }

    public String getMatchGround() {
        return matchGround;
    }

    public void setMatchGround(String matchGround) {
        this.matchGround = matchGround;
    }

    public String getSportDetailsId() {
        return sportDetailsId;
    }

    public void setSportDetailsId(String sportDetailsId) {
        this.sportDetailsId = sportDetailsId;
    }

    public String getDisplayIsSafe() {
        return displayIsSafe;
    }

    public void setDisplayIsSafe(String displayIsSafe) {
        this.displayIsSafe = displayIsSafe;
    }

    public String getDisplayIsRegular() {
        return displayIsRegular;
    }

    public void setDisplayIsRegular(String displayIsRegular) {
        this.displayIsRegular = displayIsRegular;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShowScorecard() {
        return showScorecard;
    }

    public void setShowScorecard(String showScorecard) {
        this.showScorecard = showScorecard;
    }

    public String getMatchDesc() {
        return matchDesc;
    }

    public void setMatchDesc(String matchDesc) {
        this.matchDesc = matchDesc;
    }

    public String getMatchDtlsUpdateId() {
        return matchDtlsUpdateId;
    }

    public void setMatchDtlsUpdateId(String matchDtlsUpdateId) {
        this.matchDtlsUpdateId = matchDtlsUpdateId;
    }

    public String getTeamAXi() {
        return teamAXi;
    }

    public void setTeamAXi(String teamAXi) {
        this.teamAXi = teamAXi;
    }

    public String getTeamBXi() {
        return teamBXi;
    }

    public void setTeamBXi(String teamBXi) {
        this.teamBXi = teamBXi;
    }

    public String getTeam1Inn1Score() {
        return team1Inn1Score;
    }

    public void setTeam1Inn1Score(String team1Inn1Score) {
        this.team1Inn1Score = team1Inn1Score;
    }

    public String getTeam2Inn1Score() {
        return team2Inn1Score;
    }

    public void setTeam2Inn1Score(String team2Inn1Score) {
        this.team2Inn1Score = team2Inn1Score;
    }

    public String getTeam1Inn2Score() {
        return team1Inn2Score;
    }

    public void setTeam1Inn2Score(String team1Inn2Score) {
        this.team1Inn2Score = team1Inn2Score;
    }

    public String getTeam2Inn2Score() {
        return team2Inn2Score;
    }

    public void setTeam2Inn2Score(String team2Inn2Score) {
        this.team2Inn2Score = team2Inn2Score;
    }

    public String getTotalEntryFees() {
        return totalEntryFees;
    }

    public void setTotalEntryFees(String totalEntryFees) {
        this.totalEntryFees = totalEntryFees;
    }

    public String getJoinedTeampTeam() {
        return joinedTeampTeam;
    }

    public void setJoinedTeampTeam(String joinedTeampTeam) {
        this.joinedTeampTeam = joinedTeampTeam;
    }

    public String getJoinedContest() {
        return joinedContest;
    }

    public void setJoinedContest(String joinedContest) {
        this.joinedContest = joinedContest;
    }

    public String getTotalWinAmount() {
        return totalWinAmount;
    }

    public void setTotalWinAmount(String totalWinAmount) {
        this.totalWinAmount = totalWinAmount;
    }

    public String getTotalWinBonusAmount() {
        return totalWinBonusAmount;
    }

    public void setTotalWinBonusAmount(String totalWinBonusAmount) {
        this.totalWinBonusAmount = totalWinBonusAmount;
    }

    public String getLevel1Point() {
        return level1Point;
    }

    public void setLevel1Point(String level1Point) {
        this.level1Point = level1Point;
    }

    public String getLevel2Point() {
        return level2Point;
    }

    public void setLevel2Point(String level2Point) {
        this.level2Point = level2Point;
    }

    public String getLevel3Point() {
        return level3Point;
    }

    public void setLevel3Point(String level3Point) {
        this.level3Point = level3Point;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getConDisplayType() {
        return ConDisplayType;
    }

    public void setConDisplayType(String conDisplayType) {
        ConDisplayType = conDisplayType;
    }
}