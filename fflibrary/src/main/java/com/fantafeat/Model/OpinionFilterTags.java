package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OpinionFilterTags {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tag_title")
    @Expose
    private String tag_title;
    @SerializedName("tag_s_name")
    @Expose
    private String tag_s_name;
    @SerializedName("tag_img")
    @Expose
    private String tag_img;
    @SerializedName("tag_status")
    @Expose
    private String tag_status;
    @SerializedName("other_data")
    @Expose
    private OtherData other_data;
   // private int eventLength
    private ArrayList<EventModel> listOpinion;

    public OtherData getOther_data() {
        return other_data;
    }

    public void setOther_data(OtherData other_data) {
        this.other_data = other_data;
    }

    public ArrayList<EventModel> getListOpinion() {
        return listOpinion;
    }

    public void setListOpinion(ArrayList<EventModel> listOpinion) {
        this.listOpinion = listOpinion;
    }

    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag_title() {
        return tag_title;
    }

    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }

    public String getTag_s_name() {
        return tag_s_name;
    }

    public void setTag_s_name(String tag_s_name) {
        this.tag_s_name = tag_s_name;
    }

    public String getTag_img() {
        return tag_img;
    }

    public void setTag_img(String tag_img) {
        this.tag_img = tag_img;
    }

    public String getTag_status() {
        return tag_status;
    }

    public void setTag_status(String tag_status) {
        this.tag_status = tag_status;
    }

    public class OtherData implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sport_id")
        @Expose
        private String sportId;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
        @SerializedName("cid")
        @Expose
        private String cid;
        @SerializedName("used_api")
        @Expose
        private String usedApi;
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
        @SerializedName("winner_team")
        @Expose
        private String winnerTeam;
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
        @SerializedName("match_total_played")
        @Expose
        private String matchTotalPlayed;
        @SerializedName("match_1_inning_score")
        @Expose
        private String match1InningScore;
        @SerializedName("match_2_inning_score")
        @Expose
        private String match2InningScore;
        @SerializedName("match_win_1_bat")
        @Expose
        private String matchWin1Bat;
        @SerializedName("match_win_2_bat")
        @Expose
        private String matchWin2Bat;
        @SerializedName("match_wic_pacer")
        @Expose
        private String matchWicPacer;
        @SerializedName("match_wic_spinner")
        @Expose
        private String matchWicSpinner;
        @SerializedName("match_head_to_head")
        @Expose
        private String matchHeadToHead;
        @SerializedName("match_team1_win")
        @Expose
        private String matchTeam1Win;
        @SerializedName("match_team2_win")
        @Expose
        private String matchTeam2Win;
        @SerializedName("team1_this_ground")
        @Expose
        private String team1ThisGround;
        @SerializedName("team2_this_ground")
        @Expose
        private String team2ThisGround;
        @SerializedName("match_status")
        @Expose
        private String matchStatus;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("show_scorecard")
        @Expose
        private String showScorecard;
        @SerializedName("default_created")
        @Expose
        private String defaultCreated;
        @SerializedName("match_desc")
        @Expose
        private String matchDesc;
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
        @SerializedName("match_dtls_update_id")
        @Expose
        private String matchDtlsUpdateId;
        @SerializedName("prev_over")
        @Expose
        private String prevOver;
        @SerializedName("next_over")
        @Expose
        private String nextOver;
        @SerializedName("current_over")
        @Expose
        private String currentOver;
        @SerializedName("last_ball")
        @Expose
        private String lastBall;
        @SerializedName("cron_status")
        @Expose
        private String cronStatus;
        @SerializedName("cron_call_api")
        @Expose
        private String cronCallApi;
        @SerializedName("team_a_xi")
        @Expose
        private String teamAXi;
        @SerializedName("team_b_xi")
        @Expose
        private String teamBXi;
        @SerializedName("nf_title")
        @Expose
        private String nfTitle;
        @SerializedName("nf_msg")
        @Expose
        private String nfMsg;
        @SerializedName("contest_add")
        @Expose
        private String contestAdd;
        @SerializedName("create_at")
        @Expose
        private String createAt;
        @SerializedName("is_winning_declared")
        @Expose
        private String isWinningDeclared;
        @SerializedName("mega_text")
        @Expose
        private String megaText;
        @SerializedName("is_time_chage")
        @Expose
        private String isTimeChage;
        @SerializedName("time_change_msg")
        @Expose
        private String timeChangeMsg;
        @SerializedName("lineup_change_msg")
        @Expose
        private String lineupChangeMsg;
        @SerializedName("is_single")
        @Expose
        private String isSingle;
        @SerializedName("is_fifer")
        @Expose
        private String isFifer;
        @SerializedName("is_full_scorecard")
        @Expose
        private String is_full_scorecard;
        // private int eventLength


        public String getIs_full_scorecard() {
            return is_full_scorecard;
        }

        public void setIs_full_scorecard(String is_full_scorecard) {
            this.is_full_scorecard = is_full_scorecard;
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

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
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

        public String getWinnerTeam() {
            return winnerTeam;
        }

        public void setWinnerTeam(String winnerTeam) {
            this.winnerTeam = winnerTeam;
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

        public String getMatchTotalPlayed() {
            return matchTotalPlayed;
        }

        public void setMatchTotalPlayed(String matchTotalPlayed) {
            this.matchTotalPlayed = matchTotalPlayed;
        }

        public String getMatch1InningScore() {
            return match1InningScore;
        }

        public void setMatch1InningScore(String match1InningScore) {
            this.match1InningScore = match1InningScore;
        }

        public String getMatch2InningScore() {
            return match2InningScore;
        }

        public void setMatch2InningScore(String match2InningScore) {
            this.match2InningScore = match2InningScore;
        }

        public String getMatchWin1Bat() {
            return matchWin1Bat;
        }

        public void setMatchWin1Bat(String matchWin1Bat) {
            this.matchWin1Bat = matchWin1Bat;
        }

        public String getMatchWin2Bat() {
            return matchWin2Bat;
        }

        public void setMatchWin2Bat(String matchWin2Bat) {
            this.matchWin2Bat = matchWin2Bat;
        }

        public String getMatchWicPacer() {
            return matchWicPacer;
        }

        public void setMatchWicPacer(String matchWicPacer) {
            this.matchWicPacer = matchWicPacer;
        }

        public String getMatchWicSpinner() {
            return matchWicSpinner;
        }

        public void setMatchWicSpinner(String matchWicSpinner) {
            this.matchWicSpinner = matchWicSpinner;
        }

        public String getMatchHeadToHead() {
            return matchHeadToHead;
        }

        public void setMatchHeadToHead(String matchHeadToHead) {
            this.matchHeadToHead = matchHeadToHead;
        }

        public String getMatchTeam1Win() {
            return matchTeam1Win;
        }

        public void setMatchTeam1Win(String matchTeam1Win) {
            this.matchTeam1Win = matchTeam1Win;
        }

        public String getMatchTeam2Win() {
            return matchTeam2Win;
        }

        public void setMatchTeam2Win(String matchTeam2Win) {
            this.matchTeam2Win = matchTeam2Win;
        }

        public String getTeam1ThisGround() {
            return team1ThisGround;
        }

        public void setTeam1ThisGround(String team1ThisGround) {
            this.team1ThisGround = team1ThisGround;
        }

        public String getTeam2ThisGround() {
            return team2ThisGround;
        }

        public void setTeam2ThisGround(String team2ThisGround) {
            this.team2ThisGround = team2ThisGround;
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

        public String getDefaultCreated() {
            return defaultCreated;
        }

        public void setDefaultCreated(String defaultCreated) {
            this.defaultCreated = defaultCreated;
        }

        public String getMatchDesc() {
            return matchDesc;
        }

        public void setMatchDesc(String matchDesc) {
            this.matchDesc = matchDesc;
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

        public String getMatchDtlsUpdateId() {
            return matchDtlsUpdateId;
        }

        public void setMatchDtlsUpdateId(String matchDtlsUpdateId) {
            this.matchDtlsUpdateId = matchDtlsUpdateId;
        }

        public String getPrevOver() {
            return prevOver;
        }

        public void setPrevOver(String prevOver) {
            this.prevOver = prevOver;
        }

        public String getNextOver() {
            return nextOver;
        }

        public void setNextOver(String nextOver) {
            this.nextOver = nextOver;
        }

        public String getCurrentOver() {
            return currentOver;
        }

        public void setCurrentOver(String currentOver) {
            this.currentOver = currentOver;
        }

        public String getLastBall() {
            return lastBall;
        }

        public void setLastBall(String lastBall) {
            this.lastBall = lastBall;
        }

        public String getCronStatus() {
            return cronStatus;
        }

        public void setCronStatus(String cronStatus) {
            this.cronStatus = cronStatus;
        }

        public String getCronCallApi() {
            return cronCallApi;
        }

        public void setCronCallApi(String cronCallApi) {
            this.cronCallApi = cronCallApi;
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

        public String getNfTitle() {
            return nfTitle;
        }

        public void setNfTitle(String nfTitle) {
            this.nfTitle = nfTitle;
        }

        public String getNfMsg() {
            return nfMsg;
        }

        public void setNfMsg(String nfMsg) {
            this.nfMsg = nfMsg;
        }

        public String getContestAdd() {
            return contestAdd;
        }

        public void setContestAdd(String contestAdd) {
            this.contestAdd = contestAdd;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getIsWinningDeclared() {
            return isWinningDeclared;
        }

        public void setIsWinningDeclared(String isWinningDeclared) {
            this.isWinningDeclared = isWinningDeclared;
        }

        public String getMegaText() {
            return megaText;
        }

        public void setMegaText(String megaText) {
            this.megaText = megaText;
        }

        public String getIsTimeChage() {
            return isTimeChage;
        }

        public void setIsTimeChage(String isTimeChage) {
            this.isTimeChage = isTimeChage;
        }

        public String getTimeChangeMsg() {
            return timeChangeMsg;
        }

        public void setTimeChangeMsg(String timeChangeMsg) {
            this.timeChangeMsg = timeChangeMsg;
        }

        public String getLineupChangeMsg() {
            return lineupChangeMsg;
        }

        public void setLineupChangeMsg(String lineupChangeMsg) {
            this.lineupChangeMsg = lineupChangeMsg;
        }

        public String getIsSingle() {
            return isSingle;
        }

        public void setIsSingle(String isSingle) {
            this.isSingle = isSingle;
        }

        public String getIsFifer() {
            return isFifer;
        }

        public void setIsFifer(String isFifer) {
            this.isFifer = isFifer;
        }
    }
}
