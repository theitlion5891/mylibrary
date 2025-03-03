package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GroupModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("display_type")
    @Expose
    private String displayType;
    @SerializedName("display_amt")
    @Expose
    private String display_amt;
    @SerializedName("grp_name")
    @Expose
    private String grpName;
    @SerializedName("grp_desc")
    @Expose
    private String grpDesc;
    @SerializedName("grp_status")
    @Expose
    private String grpStatus;
    @SerializedName("is_display")
    @Expose
    private String isDisplay;
    @SerializedName("is_add_contest")
    @Expose
    private String isAddContest;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;

    @SerializedName("filling_fast_text")
    @Expose
    private String filling_fast_text;
    @SerializedName("players_data")
    @Expose
    private List<PlayersDatum> playersData = null;

    private String type="";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplay_amt() {
        return display_amt;
    }

    public void setDisplay_amt(String display_amt) {
        this.display_amt = display_amt;
    }

    public String getFilling_fast_text() {
        return filling_fast_text;
    }

    public void setFilling_fast_text(String filling_fast_text) {
        this.filling_fast_text = filling_fast_text;
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

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpDesc() {
        return grpDesc;
    }

    public void setGrpDesc(String grpDesc) {
        this.grpDesc = grpDesc;
    }

    public String getGrpStatus() {
        return grpStatus;
    }

    public void setGrpStatus(String grpStatus) {
        this.grpStatus = grpStatus;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getIsAddContest() {
        return isAddContest;
    }

    public void setIsAddContest(String isAddContest) {
        this.isAddContest = isAddContest;
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

    public List<PlayersDatum> getPlayersData() {
        return playersData;
    }

    public void setPlayersData(List<PlayersDatum> playersData) {
        this.playersData = playersData;
    }

    public class PlayersDatum implements Serializable{

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
