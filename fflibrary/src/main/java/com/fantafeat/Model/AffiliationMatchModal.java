package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffiliationMatchModal {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("total_win_amount")
    @Expose
    private String totalWinAmount;
    @SerializedName("trans_id")
    @Expose
    private String transId;
    @SerializedName("join_team")
    @Expose
    private String joinTeam;
    @SerializedName("use_deposit_amt")
    @Expose
    private String useDepositAmt;
    @SerializedName("use_win_amt")
    @Expose
    private String useWinAmt;
    @SerializedName("join_contest")
    @Expose
    private String joinContest;
    @SerializedName("total_entry_fee")
    @Expose
    private String totalEntryFee;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("match_title")
    @Expose
    private String matchTitle;
    @SerializedName("sport_id")
    @Expose
    private String sportId;
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
    @SerializedName("match_desc")
    @Expose
    private String matchDesc;
    @SerializedName("match_status")
    @Expose
    private String matchStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTotalWinAmount() {
        return totalWinAmount;
    }

    public void setTotalWinAmount(String totalWinAmount) {
        this.totalWinAmount = totalWinAmount;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getJoinTeam() {
        return joinTeam;
    }

    public void setJoinTeam(String joinTeam) {
        this.joinTeam = joinTeam;
    }

    public String getUseDepositAmt() {
        return useDepositAmt;
    }

    public void setUseDepositAmt(String useDepositAmt) {
        this.useDepositAmt = useDepositAmt;
    }

    public String getUseWinAmt() {
        return useWinAmt;
    }

    public void setUseWinAmt(String useWinAmt) {
        this.useWinAmt = useWinAmt;
    }

    public String getJoinContest() {
        return joinContest;
    }

    public void setJoinContest(String joinContest) {
        this.joinContest = joinContest;
    }

    public String getTotalEntryFee() {
        return totalEntryFee;
    }

    public void setTotalEntryFee(String totalEntryFee) {
        this.totalEntryFee = totalEntryFee;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
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

    public String getMatchDesc() {
        return matchDesc;
    }

    public void setMatchDesc(String matchDesc) {
        this.matchDesc = matchDesc;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }
}
