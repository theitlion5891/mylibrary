package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GroupContestModel implements Serializable {

    @SerializedName("contest_data")
    @Expose
    private List<ContestDatum> contestData = null;
    @SerializedName("con_type")
    @Expose
    private String conType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("order_no")
    @Expose
    private String orderNo;

    public List<ContestDatum> getContestData() {
        return contestData;
    }

    public void setContestData(List<ContestDatum> contestData) {
        this.contestData = contestData;
    }

    public String getConType() {
        return conType;
    }

    public void setConType(String conType) {
        this.conType = conType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public static class ContestDatum implements Serializable{

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("match_id")
        @Expose
        private String matchId;
        @SerializedName("grp_id")
        @Expose
        private String grpId;
        @SerializedName("con_type")
        @Expose
        private String conType;
        @SerializedName("no_team_join")
        @Expose
        private String noTeamJoin;
        @SerializedName("entry_fee")
        @Expose
        private String entryFee;
        @SerializedName("commission")
        @Expose
        private String commission;
        @SerializedName("distribute_amount")
        @Expose
        private String distributeAmount;
        @SerializedName("joined_teams")
        @Expose
        private String  joinedTeams;
        @SerializedName("total_winner")
        @Expose
        private String totalWinner;
        @SerializedName("max_join_team")
        @Expose
        private String maxJoinTeam;
        @SerializedName("player_spot_wise_data")
        @Expose
        private String playerSpotWiseData;
        @SerializedName("is_unlimited")
        @Expose
        private String isUnlimited;
        @SerializedName("auto_create")
        @Expose
        private String autoCreate;
        @SerializedName("con_tpl_id")
        @Expose
        private String conTplId;
        @SerializedName("default_bonus")
        @Expose
        private String defaultBonus;
        @SerializedName("offer_data")
        @Expose
        private String offerData;
        @SerializedName("offer_title_text")
        @Expose
        private String offerTitleText;
        @SerializedName("con_entry_status")
        @Expose
        private String contestStatus;
        @SerializedName("create_at")
        @Expose
        private String createAt;
        @SerializedName("update_at")
        @Expose
        private String updateAt;
        @SerializedName("is_win_declare")
        @Expose
        private String isWinDeclare;
        @SerializedName("con_win_amount_per_spot")
        @Expose
        private String con_win_amount_per_spot;
        @SerializedName("con_win_player_list")
        @Expose
        private String con_win_player_list;

        @SerializedName("con_win_amount_per_spot_my")
        @Expose
        private String con_win_amount_per_spot_my;
        @SerializedName("default_ff_coins")
        @Expose
        private String default_ff_coins;
       // private String myJoinPlayer;

        public String getDefault_ff_coins() {
            return default_ff_coins;
        }

        public void setDefault_ff_coins(String default_ff_coins) {
            this.default_ff_coins = default_ff_coins;
        }

        public String getCon_win_amount_per_spot_my() {
            return con_win_amount_per_spot_my;
        }

        public void setCon_win_amount_per_spot_my(String con_win_amount_per_spot_my) {
            this.con_win_amount_per_spot_my = con_win_amount_per_spot_my;
        }

        public String getCon_win_player_list() {
            return con_win_player_list;
        }

        public void setCon_win_player_list(String con_win_player_list) {
            this.con_win_player_list = con_win_player_list;
        }

        public String getCon_win_amount_per_spot() {
            return con_win_amount_per_spot;
        }

        public void setCon_win_amount_per_spot(String con_win_amount_per_spot) {
            this.con_win_amount_per_spot = con_win_amount_per_spot;
        }

        private boolean isDisable=false;


        public boolean isDisable() {
            return isDisable;
        }

        public void setDisable(boolean disable) {
            isDisable = disable;
        }

        @SerializedName("group_player_data")
        @Expose
        private List<GroupModel.PlayersDatum> players;
        private List<GroupContestModel.ContestDatum.PlayersDatum> playersContest;

        @SerializedName("all_group_player_data")
        @Expose
        private List<PlayersDatum> all_players;

        public List<PlayersDatum> getPlayersContest() {
            return playersContest;
        }

        public void setPlayersContest(List<PlayersDatum> playersContest) {
            this.playersContest = playersContest;
        }

        public List<PlayersDatum> getAll_players() {
            return all_players;
        }

        public void setAll_players(List<PlayersDatum> all_players) {
            this.all_players = all_players;
        }

        public List<GroupModel.PlayersDatum> getPlayers() {
            return players;
        }

        public void setPlayers(List<GroupModel.PlayersDatum> players) {
            this.players = players;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getGrpId() {
            return grpId;
        }

        public void setGrpId(String grpId) {
            this.grpId = grpId;
        }

        public String getConType() {
            return conType;
        }

        public void setConType(String conType) {
            this.conType = conType;
        }

        public String getNoTeamJoin() {
            return noTeamJoin;
        }

        public void setNoTeamJoin(String noTeamJoin) {
            this.noTeamJoin = noTeamJoin;
        }

        public String getEntryFee() {
            return entryFee;
        }

        public void setEntryFee(String entryFee) {
            this.entryFee = entryFee;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getDistributeAmount() {
            return distributeAmount;
        }

        public void setDistributeAmount(String distributeAmount) {
            this.distributeAmount = distributeAmount;
        }

        public String getJoinedTeams() {
            return joinedTeams;
        }

        public void setJoinedTeams(String joinedTeams) {
            this.joinedTeams = joinedTeams;
        }

        public String getTotalWinner() {
            return totalWinner;
        }

        public void setTotalWinner(String totalWinner) {
            this.totalWinner = totalWinner;
        }

        public String getMaxJoinTeam() {
            return maxJoinTeam;
        }

        public void setMaxJoinTeam(String maxJoinTeam) {
            this.maxJoinTeam = maxJoinTeam;
        }

        public String getPlayerSpotWiseData() {
            return playerSpotWiseData;
        }

        public void setPlayerSpotWiseData(String playerSpotWiseData) {
            this.playerSpotWiseData = playerSpotWiseData;
        }

        public String getIsUnlimited() {
            return isUnlimited;
        }

        public void setIsUnlimited(String isUnlimited) {
            this.isUnlimited = isUnlimited;
        }

        public String getAutoCreate() {
            return autoCreate;
        }

        public void setAutoCreate(String autoCreate) {
            this.autoCreate = autoCreate;
        }

        public String getConTplId() {
            return conTplId;
        }

        public void setConTplId(String conTplId) {
            this.conTplId = conTplId;
        }

        public String getDefaultBonus() {
            return defaultBonus;
        }

        public void setDefaultBonus(String defaultBonus) {
            this.defaultBonus = defaultBonus;
        }

        public String getOfferData() {
            return offerData;
        }

        public void setOfferData(String offerData) {
            this.offerData = offerData;
        }

        public String getOfferTitleText() {
            return offerTitleText;
        }

        public void setOfferTitleText(String offerTitleText) {
            this.offerTitleText = offerTitleText;
        }

        public String getContestStatus() {
            return contestStatus;
        }

        public void setContestStatus(String contestStatus) {
            this.contestStatus = contestStatus;
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

        public String getIsWinDeclare() {
            return isWinDeclare;
        }

        public void setIsWinDeclare(String isWinDeclare) {
            this.isWinDeclare = isWinDeclare;
        }

        public static class PlayersDatum implements Serializable{

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("match_id")
            @Expose
            private String matchId;
            @SerializedName("grp_id")
            @Expose
            private String grpId;
            @SerializedName("player_id")
            @Expose
            private String playerId;
            @SerializedName("selection_cnt")
            @Expose
            private String selectionCnt;
            @SerializedName("total_rank")
            @Expose
            private String totalRank;
            @SerializedName("total_points")
            @Expose
            private String totalPoints;
            @SerializedName("is_win")
            @Expose
            private String isWin;
            @SerializedName("is_active")
            @Expose
            private String isActive;
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
            @SerializedName("playing_xi")
            @Expose
            private String playingXi;

            private boolean isChecked=false;
            private boolean isDisable=false;

            private boolean isJoinMe=false;

            private String joiningCnt="0";

            private String joined_spot="0";
            private String apx_win_amt="0";

            public String getJoined_spot() {
                return joined_spot;
            }

            public void setJoined_spot(String joined_spot) {
                this.joined_spot = joined_spot;
            }

            public String getJoiningCnt() {
                return joiningCnt;
            }

            public void setJoiningCnt(String joiningCnt) {
                this.joiningCnt = joiningCnt;
            }

            public String getApx_win_amt() {
                return apx_win_amt;
            }

            public void setApx_win_amt(String apx_win_amt) {
                this.apx_win_amt = apx_win_amt;
            }

            public boolean isJoinMe() {
                return isJoinMe;
            }

            public void setJoinMe(boolean joinMe) {
                isJoinMe = joinMe;
            }

            public boolean isDisable() {
                return isDisable;
            }

            public void setDisable(boolean disable) {
                isDisable = disable;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMatchId() {
                return matchId;
            }

            public void setMatchId(String matchId) {
                this.matchId = matchId;
            }

            public String getGrpId() {
                return grpId;
            }

            public void setGrpId(String grpId) {
                this.grpId = grpId;
            }

            public String getPlayerId() {
                return playerId;
            }

            public void setPlayerId(String playerId) {
                this.playerId = playerId;
            }

            public String getSelectionCnt() {
                return selectionCnt;
            }

            public void setSelectionCnt(String selectionCnt) {
                this.selectionCnt = selectionCnt;
            }

            public String getTotalRank() {
                return totalRank;
            }

            public void setTotalRank(String totalRank) {
                this.totalRank = totalRank;
            }

            public String getTotalPoints() {
                return totalPoints;
            }

            public void setTotalPoints(String totalPoints) {
                this.totalPoints = totalPoints;
            }

            public String getIsWin() {
                return isWin;
            }

            public void setIsWin(String isWin) {
                this.isWin = isWin;
            }

            public String getIsActive() {
                return isActive;
            }

            public void setIsActive(String isActive) {
                this.isActive = isActive;
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

            public String getPlayingXi() {
                return playingXi;
            }

            public void setPlayingXi(String playingXi) {
                this.playingXi = playingXi;
            }

        }

    }
}
