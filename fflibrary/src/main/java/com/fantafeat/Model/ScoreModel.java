package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScoreModel {
    @SerializedName("iid")
    @Expose
    private String iid;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("batting_team_id")
    @Expose
    private String battingTeamId;
    @SerializedName("fielding_team_id")
    @Expose
    private String fieldingTeamId;
    @SerializedName("scores")
    @Expose
    private String scores;
    @SerializedName("scores_full")
    @Expose
    private String scoresFull;
    @SerializedName("batsmen")
    @Expose
    private List<Batsman> batsmen = null;
    @SerializedName("bowlers")
    @Expose
    private List<Bowler> bowlers = null;
    @SerializedName("fows")
    @Expose
    private List<Fow> fows = null;
    @SerializedName("extra_runs")
    @Expose
    private ExtraRuns extraRuns;
    @SerializedName("equations")
    @Expose
    private Equations equations;

    @SerializedName("did_not_bat")
    @Expose
    private List<DidNotBat> didNotBat = null;
    @SerializedName("commentaries")
    @Expose
    private List<Commentary> commentaries = null;

    private boolean isShow=false;

    public boolean getShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBattingTeamId() {
        return battingTeamId;
    }

    public void setBattingTeamId(String battingTeamId) {
        this.battingTeamId = battingTeamId;
    }

    public String getFieldingTeamId() {
        return fieldingTeamId;
    }

    public void setFieldingTeamId(String fieldingTeamId) {
        this.fieldingTeamId = fieldingTeamId;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getScoresFull() {
        return scoresFull;
    }

    public void setScoresFull(String scoresFull) {
        this.scoresFull = scoresFull;
    }

    public List<Batsman> getBatsmen() {
        return batsmen;
    }

    public void setBatsmen(List<Batsman> batsmen) {
        this.batsmen = batsmen;
    }

    public List<Bowler> getBowlers() {
        return bowlers;
    }

    public void setBowlers(List<Bowler> bowlers) {
        this.bowlers = bowlers;
    }

    public List<Fow> getFows() {
        return fows;
    }

    public void setFows(List<Fow> fows) {
        this.fows = fows;
    }

    public ExtraRuns getExtraRuns() {
        return extraRuns;
    }

    public void setExtraRuns(ExtraRuns extraRuns) {
        this.extraRuns = extraRuns;
    }

    public Equations getEquations() {
        return equations;
    }

    public void setEquations(Equations equations) {
        this.equations = equations;
    }

    public List<DidNotBat> getDidNotBat() {
        return didNotBat;
    }

    public void setDidNotBat(List<DidNotBat> didNotBat) {
        this.didNotBat = didNotBat;
    }

    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

    public class ExtraRuns {

        @SerializedName("byes")
        @Expose
        private Integer byes;
        @SerializedName("legbyes")
        @Expose
        private Integer legbyes;
        @SerializedName("wides")
        @Expose
        private Integer wides;
        @SerializedName("noballs")
        @Expose
        private Integer noballs;
        @SerializedName("penalty")
        @Expose
        private String penalty;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getByes() {
            return byes;
        }

        public void setByes(Integer byes) {
            this.byes = byes;
        }

        public Integer getLegbyes() {
            return legbyes;
        }

        public void setLegbyes(Integer legbyes) {
            this.legbyes = legbyes;
        }

        public Integer getWides() {
            return wides;
        }

        public void setWides(Integer wides) {
            this.wides = wides;
        }

        public Integer getNoballs() {
            return noballs;
        }

        public void setNoballs(Integer noballs) {
            this.noballs = noballs;
        }

        public String getPenalty() {
            return penalty;
        }

        public void setPenalty(String penalty) {
            this.penalty = penalty;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }

    public class Bat {

        @SerializedName("runs")
        @Expose
        private String runs;
        @SerializedName("balls_faced")
        @Expose
        private Integer ballsFaced;
        @SerializedName("fours")
        @Expose
        private Integer fours;
        @SerializedName("sixes")
        @Expose
        private Integer sixes;
        @SerializedName("batsman_id")
        @Expose
        private Integer batsmanId;

        public String getRuns() {
            return runs;
        }

        public void setRuns(String runs) {
            this.runs = runs;
        }

        public Integer getBallsFaced() {
            return ballsFaced;
        }

        public void setBallsFaced(Integer ballsFaced) {
            this.ballsFaced = ballsFaced;
        }

        public Integer getFours() {
            return fours;
        }

        public void setFours(Integer fours) {
            this.fours = fours;
        }

        public Integer getSixes() {
            return sixes;
        }

        public void setSixes(Integer sixes) {
            this.sixes = sixes;
        }

        public Integer getBatsmanId() {
            return batsmanId;
        }

        public void setBatsmanId(Integer batsmanId) {
            this.batsmanId = batsmanId;
        }

    }

    public class Batsman {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("batsman_id")
        @Expose
        private String batsmanId;
        @SerializedName("batting")
        @Expose
        private String batting;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("role_str")
        @Expose
        private String roleStr;
        @SerializedName("runs")
        @Expose
        private String runs;
        @SerializedName("balls_faced")
        @Expose
        private String ballsFaced;
        @SerializedName("fours")
        @Expose
        private String fours;
        @SerializedName("sixes")
        @Expose
        private String sixes;
        @SerializedName("run0")
        @Expose
        private String run0;
        @SerializedName("run1")
        @Expose
        private String run1;
        @SerializedName("run2")
        @Expose
        private String run2;
        @SerializedName("run3")
        @Expose
        private String run3;
        @SerializedName("run5")
        @Expose
        private String run5;
        @SerializedName("how_out")
        @Expose
        private String howOut;
        @SerializedName("dismissal")
        @Expose
        private String dismissal;
        @SerializedName("strike_rate")
        @Expose
        private String strikeRate;
        @SerializedName("bowler_id")
        @Expose
        private String bowlerId;
        @SerializedName("first_fielder_id")
        @Expose
        private String firstFielderId;
        @SerializedName("second_fielder_id")
        @Expose
        private String secondFielderId;
        @SerializedName("third_fielder_id")
        @Expose
        private String thirdFielderId;
        private boolean isPlayerInTeam=false;
        private String corvc="";

        public String getCorvc() {
            return corvc;
        }

        public void setCorvc(String corvc) {
            this.corvc = corvc;
        }

        public boolean isPlayerInTeam() {
            return isPlayerInTeam;
        }

        public void setPlayerInTeam(boolean playerInTeam) {
            isPlayerInTeam = playerInTeam;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBatsmanId() {
            return batsmanId;
        }

        public void setBatsmanId(String batsmanId) {
            this.batsmanId = batsmanId;
        }

        public String getBatting() {
            return batting;
        }

        public void setBatting(String batting) {
            this.batting = batting;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRoleStr() {
            return roleStr;
        }

        public void setRoleStr(String roleStr) {
            this.roleStr = roleStr;
        }

        public String getRuns() {
            return runs;
        }

        public void setRuns(String runs) {
            this.runs = runs;
        }

        public String getBallsFaced() {
            return ballsFaced;
        }

        public void setBallsFaced(String ballsFaced) {
            this.ballsFaced = ballsFaced;
        }

        public String getFours() {
            return fours;
        }

        public void setFours(String fours) {
            this.fours = fours;
        }

        public String getSixes() {
            return sixes;
        }

        public void setSixes(String sixes) {
            this.sixes = sixes;
        }

        public String getRun0() {
            return run0;
        }

        public void setRun0(String run0) {
            this.run0 = run0;
        }

        public String getRun1() {
            return run1;
        }

        public void setRun1(String run1) {
            this.run1 = run1;
        }

        public String getRun2() {
            return run2;
        }

        public void setRun2(String run2) {
            this.run2 = run2;
        }

        public String getRun3() {
            return run3;
        }

        public void setRun3(String run3) {
            this.run3 = run3;
        }

        public String getRun5() {
            return run5;
        }

        public void setRun5(String run5) {
            this.run5 = run5;
        }

        public String getHowOut() {
            return howOut;
        }

        public void setHowOut(String howOut) {
            this.howOut = howOut;
        }

        public String getDismissal() {
            return dismissal;
        }

        public void setDismissal(String dismissal) {
            this.dismissal = dismissal;
        }

        public String getStrikeRate() {
            return strikeRate;
        }

        public void setStrikeRate(String strikeRate) {
            this.strikeRate = strikeRate;
        }

        public String getBowlerId() {
            return bowlerId;
        }

        public void setBowlerId(String bowlerId) {
            this.bowlerId = bowlerId;
        }

        public String getFirstFielderId() {
            return firstFielderId;
        }

        public void setFirstFielderId(String firstFielderId) {
            this.firstFielderId = firstFielderId;
        }

        public String getSecondFielderId() {
            return secondFielderId;
        }

        public void setSecondFielderId(String secondFielderId) {
            this.secondFielderId = secondFielderId;
        }

        public String getThirdFielderId() {
            return thirdFielderId;
        }

        public void setThirdFielderId(String thirdFielderId) {
            this.thirdFielderId = thirdFielderId;
        }

    }

    public class Batsman__1 {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("batsman_id")
        @Expose
        private String batsmanId;
        @SerializedName("runs")
        @Expose
        private String runs;
        @SerializedName("balls")
        @Expose
        private String balls;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBatsmanId() {
            return batsmanId;
        }

        public void setBatsmanId(String batsmanId) {
            this.batsmanId = batsmanId;
        }

        public String getRuns() {
            return runs;
        }

        public void setRuns(String runs) {
            this.runs = runs;
        }

        public String getBalls() {
            return balls;
        }

        public void setBalls(String balls) {
            this.balls = balls;
        }

    }

    public class Bowl {

        @SerializedName("runs_conceded")
        @Expose
        private Integer runsConceded;
        @SerializedName("overs")
        @Expose
        private String overs;
        @SerializedName("wickets")
        @Expose
        private Integer wickets;
        @SerializedName("maidens")
        @Expose
        private Integer maidens;
        @SerializedName("bowler_id")
        @Expose
        private String bowlerId;

        public Integer getRunsConceded() {
            return runsConceded;
        }

        public void setRunsConceded(Integer runsConceded) {
            this.runsConceded = runsConceded;
        }

        public String getOvers() {
            return overs;
        }

        public void setOvers(String overs) {
            this.overs = overs;
        }

        public Integer getWickets() {
            return wickets;
        }

        public void setWickets(Integer wickets) {
            this.wickets = wickets;
        }

        public Integer getMaidens() {
            return maidens;
        }

        public void setMaidens(Integer maidens) {
            this.maidens = maidens;
        }

        public String getBowlerId() {
            return bowlerId;
        }

        public void setBowlerId(String bowlerId) {
            this.bowlerId = bowlerId;
        }

    }

    public class Bowler {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("bowler_id")
        @Expose
        private String bowlerId;
        @SerializedName("bowling")
        @Expose
        private String bowling;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("overs")
        @Expose
        private String overs;
        @SerializedName("maidens")
        @Expose
        private String maidens;
        @SerializedName("runs_conceded")
        @Expose
        private String runsConceded;
        @SerializedName("wickets")
        @Expose
        private String wickets;
        @SerializedName("noballs")
        @Expose
        private String noballs;
        @SerializedName("wides")
        @Expose
        private String wides;
        @SerializedName("econ")
        @Expose
        private String econ;
        @SerializedName("run0")
        @Expose
        private String run0;
        @SerializedName("bowledcount")
        @Expose
        private String bowledcount;
        @SerializedName("lbwcount")
        @Expose
        private String lbwcount;
        private boolean isPlayerInTeam=false;

        private String corvc="";

        public String getCorvc() {
            return corvc;
        }

        public void setCorvc(String corvc) {
            this.corvc = corvc;
        }

        public boolean isPlayerInTeam() {
            return isPlayerInTeam;
        }

        public void setPlayerInTeam(boolean playerInTeam) {
            isPlayerInTeam = playerInTeam;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBowlerId() {
            return bowlerId;
        }

        public void setBowlerId(String bowlerId) {
            this.bowlerId = bowlerId;
        }

        public String getBowling() {
            return bowling;
        }

        public void setBowling(String bowling) {
            this.bowling = bowling;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getOvers() {
            return overs;
        }

        public void setOvers(String overs) {
            this.overs = overs;
        }

        public String getMaidens() {
            return maidens;
        }

        public void setMaidens(String maidens) {
            this.maidens = maidens;
        }

        public String getRunsConceded() {
            return runsConceded;
        }

        public void setRunsConceded(String runsConceded) {
            this.runsConceded = runsConceded;
        }

        public String getWickets() {
            return wickets;
        }

        public void setWickets(String wickets) {
            this.wickets = wickets;
        }

        public String getNoballs() {
            return noballs;
        }

        public void setNoballs(String noballs) {
            this.noballs = noballs;
        }

        public String getWides() {
            return wides;
        }

        public void setWides(String wides) {
            this.wides = wides;
        }

        public String getEcon() {
            return econ;
        }

        public void setEcon(String econ) {
            this.econ = econ;
        }

        public String getRun0() {
            return run0;
        }

        public void setRun0(String run0) {
            this.run0 = run0;
        }

        public String getBowledcount() {
            return bowledcount;
        }

        public void setBowledcount(String bowledcount) {
            this.bowledcount = bowledcount;
        }

        public String getLbwcount() {
            return lbwcount;
        }

        public void setLbwcount(String lbwcount) {
            this.lbwcount = lbwcount;
        }

    }

    public class Commentary {

        @SerializedName("ball_score")
        @Expose
        public List<BallScore> ballScore = null;
        @SerializedName("ov_no")
        @Expose
        private Integer ovNo;
        @SerializedName("bat_names")
        @Expose
        private String batNames;
        @SerializedName("bow_names")
        @Expose
        private String bowNames;
        @SerializedName("runs")
        @Expose
        private String runs;
        @SerializedName("score")
        @Expose
        private String score;
        @SerializedName("bats")
        @Expose
        private List<Bat> bats = null;
        @SerializedName("bowls")
        @Expose
        private List<Bowl> bowls = null;
        @SerializedName("commentary")
        @Expose
        private String commentary;

        public List<BallScore> getBallScore() {
            return ballScore;
        }

        public void setBallScore(List<BallScore> ballScore) {
            this.ballScore = ballScore;
        }

        public Integer getOvNo() {
            return ovNo;
        }

        public void setOvNo(Integer ovNo) {
            this.ovNo = ovNo;
        }

        public String getBatNames() {
            return batNames;
        }

        public void setBatNames(String batNames) {
            this.batNames = batNames;
        }

        public String getBowNames() {
            return bowNames;
        }

        public void setBowNames(String bowNames) {
            this.bowNames = bowNames;
        }

        public String getRuns() {
            return runs;
        }

        public void setRuns(String runs) {
            this.runs = runs;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public List<Bat> getBats() {
            return bats;
        }

        public void setBats(List<Bat> bats) {
            this.bats = bats;
        }

        public List<Bowl> getBowls() {
            return bowls;
        }

        public void setBowls(List<Bowl> bowls) {
            this.bowls = bowls;
        }

        public String getCommentary() {
            return commentary;
        }

        public void setCommentary(String commentary) {
            this.commentary = commentary;
        }

        public class BallScore {

            @SerializedName("event_id")
            @Expose
            private String eventId;
            @SerializedName("event")
            @Expose
            private String event;
            @SerializedName("batsman_id")
            @Expose
            private String batsmanId;
            @SerializedName("bowler_id")
            @Expose
            private String bowlerId;
            @SerializedName("over")
            @Expose
            private String over;
            @SerializedName("ball")
            @Expose
            private String ball;
            @SerializedName("score")
            @Expose
            private String score;
            @SerializedName("commentary")
            @Expose
            private String commentary;
            @SerializedName("noball_dismissal")
            @Expose
            private Boolean noballDismissal;
            @SerializedName("text")
            @Expose
            private String text;
            @SerializedName("timestamp")
            @Expose
            private String timestamp;
            @SerializedName("run")
            @Expose
            private String run;
            @SerializedName("wicket_batsman_id")
            @Expose
            private String wicketBatsmanId;
            @SerializedName("how_out")
            @Expose
            private String howOut;
            @SerializedName("batsman_runs")
            @Expose
            private String batsmanRuns;
            @SerializedName("batsman_balls")
            @Expose
            private String batsmanBalls;

            public String getEventId() {
                return eventId;
            }

            public void setEventId(String eventId) {
                this.eventId = eventId;
            }

            public String getEvent() {
                return event;
            }

            public void setEvent(String event) {
                this.event = event;
            }

            public String getBatsmanId() {
                return batsmanId;
            }

            public void setBatsmanId(String batsmanId) {
                this.batsmanId = batsmanId;
            }

            public String getBowlerId() {
                return bowlerId;
            }

            public void setBowlerId(String bowlerId) {
                this.bowlerId = bowlerId;
            }

            public String getOver() {
                return over;
            }

            public void setOver(String over) {
                this.over = over;
            }

            public String getBall() {
                return ball;
            }

            public void setBall(String ball) {
                this.ball = ball;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getCommentary() {
                return commentary;
            }

            public void setCommentary(String commentary) {
                this.commentary = commentary;
            }

            public Boolean getNoballDismissal() {
                return noballDismissal;
            }

            public void setNoballDismissal(Boolean noballDismissal) {
                this.noballDismissal = noballDismissal;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getRun() {
                return run;
            }

            public void setRun(String run) {
                this.run = run;
            }

            public String getWicketBatsmanId() {
                return wicketBatsmanId;
            }

            public void setWicketBatsmanId(String wicketBatsmanId) {
                this.wicketBatsmanId = wicketBatsmanId;
            }

            public String getHowOut() {
                return howOut;
            }

            public void setHowOut(String howOut) {
                this.howOut = howOut;
            }

            public String getBatsmanRuns() {
                return batsmanRuns;
            }

            public void setBatsmanRuns(String batsmanRuns) {
                this.batsmanRuns = batsmanRuns;
            }

            public String getBatsmanBalls() {
                return batsmanBalls;
            }

            public void setBatsmanBalls(String batsmanBalls) {
                this.batsmanBalls = batsmanBalls;
            }

        }


    }

    public class CurrentPartnership {

        @SerializedName("runs")
        @Expose
        private Integer runs;
        @SerializedName("balls")
        @Expose
        private Integer balls;
        @SerializedName("overs")
        @Expose
        private String overs;
        @SerializedName("batsmen")
        @Expose
        private List<Batsman__1> batsmen = null;

        public Integer getRuns() {
            return runs;
        }

        public void setRuns(Integer runs) {
            this.runs = runs;
        }

        public Integer getBalls() {
            return balls;
        }

        public void setBalls(Integer balls) {
            this.balls = balls;
        }

        public String getOvers() {
            return overs;
        }

        public void setOvers(String overs) {
            this.overs = overs;
        }

        public List<Batsman__1> getBatsmen() {
            return batsmen;
        }

        public void setBatsmen(List<Batsman__1> batsmen) {
            this.batsmen = batsmen;
        }

    }

    public class DidNotBat {

        @SerializedName("player_id")
        @Expose
        private String playerId;
        @SerializedName("name")
        @Expose
        private String name;

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Equations {

        @SerializedName("runs")
        @Expose
        private Integer runs;
        @SerializedName("wickets")
        @Expose
        private Integer wickets;
        @SerializedName("overs")
        @Expose
        private String overs;
        @SerializedName("bowlers_used")
        @Expose
        private Integer bowlersUsed;
        @SerializedName("runrate")
        @Expose
        private String runrate;

        public Integer getRuns() {
            return runs;
        }

        public void setRuns(Integer runs) {
            this.runs = runs;
        }

        public Integer getWickets() {
            return wickets;
        }

        public void setWickets(Integer wickets) {
            this.wickets = wickets;
        }

        public String getOvers() {
            return overs;
        }

        public void setOvers(String overs) {
            this.overs = overs;
        }

        public Integer getBowlersUsed() {
            return bowlersUsed;
        }

        public void setBowlersUsed(Integer bowlersUsed) {
            this.bowlersUsed = bowlersUsed;
        }

        public String getRunrate() {
            return runrate;
        }

        public void setRunrate(String runrate) {
            this.runrate = runrate;
        }

    }

    public class Fow {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("batsman_id")
        @Expose
        private String batsmanId;
        @SerializedName("runs")
        @Expose
        private String runs;
        @SerializedName("balls")
        @Expose
        private String balls;
        @SerializedName("how_out")
        @Expose
        private String howOut;
        @SerializedName("score_at_dismissal")
        @Expose
        private Integer scoreAtDismissal;
        @SerializedName("overs_at_dismissal")
        @Expose
        private String oversAtDismissal;
        @SerializedName("bowler_id")
        @Expose
        private String bowlerId;
        @SerializedName("dismissal")
        @Expose
        private String dismissal;
        @SerializedName("number")
        @Expose
        private Integer number;
        private boolean isPlayerInTeam=false;

        public boolean isPlayerInTeam() {
            return isPlayerInTeam;
        }

        public void setPlayerInTeam(boolean playerInTeam) {
            isPlayerInTeam = playerInTeam;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBatsmanId() {
            return batsmanId;
        }

        public void setBatsmanId(String batsmanId) {
            this.batsmanId = batsmanId;
        }

        public String getRuns() {
            return runs;
        }

        public void setRuns(String runs) {
            this.runs = runs;
        }

        public String getBalls() {
            return balls;
        }

        public void setBalls(String balls) {
            this.balls = balls;
        }

        public String getHowOut() {
            return howOut;
        }

        public void setHowOut(String howOut) {
            this.howOut = howOut;
        }

        public Integer getScoreAtDismissal() {
            return scoreAtDismissal;
        }

        public void setScoreAtDismissal(Integer scoreAtDismissal) {
            this.scoreAtDismissal = scoreAtDismissal;
        }

        public String getOversAtDismissal() {
            return oversAtDismissal;
        }

        public void setOversAtDismissal(String oversAtDismissal) {
            this.oversAtDismissal = oversAtDismissal;
        }

        public String getBowlerId() {
            return bowlerId;
        }

        public void setBowlerId(String bowlerId) {
            this.bowlerId = bowlerId;
        }

        public String getDismissal() {
            return dismissal;
        }

        public void setDismissal(String dismissal) {
            this.dismissal = dismissal;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

    }
}




