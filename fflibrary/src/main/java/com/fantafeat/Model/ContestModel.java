package com.fantafeat.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContestModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("is_view_all")
    @Expose
    private String is_view_all;
    @SerializedName("contest_data")
    @Expose
    private List<ContestDatum> contestData = null;
    private ArrayList<EventModel> listTop;
    private int type=0;

    public ContestModel() {
    }

    public ContestModel(int type) {
        this.type = type;
    }

    public ArrayList<EventModel> getListTop() {
        return listTop;
    }

    public void setListTop(ArrayList<EventModel> listTop) {
        this.listTop = listTop;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIs_view_all() {
        return is_view_all;
    }

    public void setIs_view_all(String is_view_all) {
        this.is_view_all = is_view_all;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ContestDatum> getContestData() {
        return contestData;
    }

    public void setContestData(List<ContestDatum> contestData) {
        this.contestData = contestData;
    }

    public static class ContestDatum implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("lb_id")
        @Expose
        private String lb_id;
        @SerializedName("match_id")
        @Expose
        private String matchId;
        @SerializedName("match_unique_id")
        @Expose
        private String matchUniqueId;
        @SerializedName("match_display_type")
        @Expose
        private String matchDisplayType;
        @SerializedName("con_display_type")
        @Expose
        private String conDisplayType;
        @SerializedName("sport_id")
        @Expose
        private String sportId;
        @SerializedName("con_tpl_id")
        @Expose
        private String conTplId;
        @SerializedName("con_tpl_name")
        @Expose
        private String conTplName;
        @SerializedName("con_type_id")
        @Expose
        private String conTypeId;
        @SerializedName("con_type_name")
        @Expose
        private String conTypeName;
        @SerializedName("con_type_desc")
        @Expose
        private String conTypeDesc;
        @SerializedName("con_type_image")
        @Expose
        private String conTypeImage;
        @SerializedName("no_team_join")
        @Expose
        private String noTeamJoin;
        @SerializedName("entry_fee")
        @Expose
        private String entryFee;
        private String o_entryFee;
        @SerializedName("commission")
        @Expose
        private String commission;
        @SerializedName("is_lb")
        @Expose
        private String is_lb;
        @SerializedName("distribute_amount")
        @Expose
        private String distributeAmount;
        @SerializedName("commission_amount")
        @Expose
        private String commissionAmount;
        @SerializedName("total_winner")
        @Expose
        private String totalWinner;
        @SerializedName("default_bonus")
        @Expose
        private String defaultBonus;
        @SerializedName("use_bonus")
        @Expose
        private String useBonus;
        private String o_useBonus;
        @SerializedName("max_team_bonus_use")
        @Expose
        private String maxTeamBonusUse;
        @SerializedName("is_unlimited")
        @Expose
        private String isUnlimited;
        @SerializedName("auto_create")
        @Expose
        private String autoCreate;
        @SerializedName("con_entry_status")
        @Expose
        private String conEntryStatus;
        @SerializedName("con_player_entry")
        @Expose
        private String conPlayerEntry;
        @SerializedName("winning_tree")
        @Expose
        private String winningTree;
        @SerializedName("winning_tree_test")
        @Expose
        private String winningTreeTest;
        @SerializedName("winning_tree_text")
        @Expose
        private String winningTreeText;
        @SerializedName("display_in_list")
        @Expose
        private String displayInList;
        @SerializedName("max_join_team")
        @Expose
        private String maxJoinTeam;
        @SerializedName("create_at")
        @Expose
        private String createAt;
        @SerializedName("update_at")
        @Expose
        private String updateAt;
        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("joined_teams")
        @Expose
        private String joinedTeams;
        @SerializedName("my_joined_team")
        @Expose
        private String myJoinedTeam;
        @SerializedName("joined_team_temp_team_id")
        @Expose
        private String joinedTeamTempTeamId;
        @SerializedName("offer_date_text")
        @Expose
        private String offer_date_text;
        @SerializedName("default_ff_coins")
        @Expose
        private String default_bb_coins;
        @SerializedName("is_flexi")
        @Expose
        private String is_flexi;
        private OfferModel offerModel;
        private Boolean o_isOffer;
        private Boolean isContestFavorite=false;
        private String join_con_qty;
        private ArrayList<PassModel> passModelArrayList = new ArrayList<>();
        private ArrayList<NewOfferModal> newOfferList;
        private ArrayList<NewOfferModal> newOfferTempList;
        private ArrayList<NewOfferModal> newOfferRemovedList;

        private String is_pass;

        public String getIs_flexi() {
            return is_flexi;
        }

        public void setIs_flexi(String is_flexi) {
            this.is_flexi = is_flexi;
        }

        public String getIs_pass() {
            return is_pass;
        }

        public void setIs_pass(String is_pass) {
            this.is_pass = is_pass;
        }

        public String getDefault_bb_coins() {
            return default_bb_coins;
        }

        public void setDefault_bb_coins(String default_bb_coins) {
            this.default_bb_coins = default_bb_coins;
        }

        public ArrayList<NewOfferModal> getNewOfferList() {
            return newOfferList;
        }

        public void setNewOfferList(ArrayList<NewOfferModal> newOfferList) {
            this.newOfferList = newOfferList;
        }

        public ArrayList<NewOfferModal> getNewOfferTempList() {
            return newOfferTempList;
        }

        public void setNewOfferTempList(ArrayList<NewOfferModal> newOfferTempList) {
            this.newOfferTempList = newOfferTempList;
        }

        public ArrayList<NewOfferModal> getNewOfferRemovedList() {
            return newOfferRemovedList;
        }

        public void setNewOfferRemovedList(ArrayList<NewOfferModal> newOfferRemovedList) {
            this.newOfferRemovedList = newOfferRemovedList;
        }

        public String getOffer_date_text() {
            return offer_date_text;
        }

        public void setOffer_date_text(String offer_date_text) {
            this.offer_date_text = offer_date_text;
        }
        public ArrayList<PassModel> getPassModelArrayList() {
            return passModelArrayList;
        }

        public void setPassModelArrayList(ArrayList<PassModel> passModelArrayList) {
            this.passModelArrayList = passModelArrayList;
        }
        public String getJoin_con_qty() {
            return join_con_qty;
        }

        public void setJoin_con_qty(String join_con_qty) {
            this.join_con_qty = join_con_qty;
        }

        public String getLb_id() {
            return lb_id;
        }

        public void setLb_id(String lb_id) {
            this.lb_id = lb_id;
        }

        public Boolean getContestFavorite() {
            return isContestFavorite;
        }

        public void setContestFavorite(Boolean contestFavorite) {
            isContestFavorite = contestFavorite;
        }

        public String getO_useBonus() {
            return o_useBonus;
        }

        public void setO_useBonus(String o_useBonus) {
            this.o_useBonus = o_useBonus;
        }

        public String getO_entryFee() {
            return o_entryFee;
        }

        public OfferModel getOfferModel() {
            return offerModel;
        }

        public void setOfferModel(OfferModel offerModel) {
            this.offerModel = offerModel;
        }

        public Boolean getO_isOffer() {
            return o_isOffer;
        }

        public void setO_isOffer(Boolean o_isOffer) {
            this.o_isOffer = o_isOffer;
        }

        public void setO_entryFee(String o_entryFee) {
            this.o_entryFee = o_entryFee;
        }

        public String getTotal_win_amount() {
            return total_win_amount;
        }

        public void setTotal_win_amount(String total_win_amount) {
            this.total_win_amount = total_win_amount;
        }

        @SerializedName("total_win_amount")
        @Expose
        private String total_win_amount;
        @SerializedName("total_rank")
        @Expose
        private String totalRank;
        @SerializedName("total_point")
        @Expose
        private String totalPoint;
        @SerializedName("temp_team_name")
        @Expose
        private String tempTeamName;

        private String isOffer = "No";
        private String offerPrice;
        private String offerText;

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

        public String getMatchUniqueId() {
            return matchUniqueId;
        }

        public void setMatchUniqueId(String matchUniqueId) {
            this.matchUniqueId = matchUniqueId;
        }

        public String getMatchDisplayType() {
            return matchDisplayType;
        }

        public void setMatchDisplayType(String matchDisplayType) {
            this.matchDisplayType = matchDisplayType;
        }

        public String getConDisplayType() {
            return conDisplayType;
        }

        public void setConDisplayType(String conDisplayType) {
            this.conDisplayType = conDisplayType;
        }

        public String getIs_lb() {
            return is_lb;
        }

        public void setIs_lb(String is_lb) {
            this.is_lb = is_lb;
        }

        public String getSportId() {
            return sportId;
        }

        public void setSportId(String sportId) {
            this.sportId = sportId;
        }

        public String getConTplId() {
            return conTplId;
        }

        public void setConTplId(String conTplId) {
            this.conTplId = conTplId;
        }

        public String getConTplName() {
            return conTplName;
        }

        public void setConTplName(String conTplName) {
            this.conTplName = conTplName;
        }

        public String getConTypeId() {
            return conTypeId;
        }

        public void setConTypeId(String conTypeId) {
            this.conTypeId = conTypeId;
        }

        public String getConTypeName() {
            return conTypeName;
        }

        public void setConTypeName(String conTypeName) {
            this.conTypeName = conTypeName;
        }

        public String getConTypeDesc() {
            return conTypeDesc;
        }

        public void setConTypeDesc(String conTypeDesc) {
            this.conTypeDesc = conTypeDesc;
        }

        public String getConTypeImage() {
            return conTypeImage;
        }

        public void setConTypeImage(String conTypeImage) {
            this.conTypeImage = conTypeImage;
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

        public String getCommissionAmount() {
            return commissionAmount;
        }

        public void setCommissionAmount(String commissionAmount) {
            this.commissionAmount = commissionAmount;
        }

        public String getTotalWinner() {
            return totalWinner;
        }

        public void setTotalWinner(String totalWinner) {
            this.totalWinner = totalWinner;
        }

        public String getDefaultBonus() {
            return defaultBonus;
        }

        public void setDefaultBonus(String defaultBonus) {
            this.defaultBonus = defaultBonus;
        }

        public String getUseBonus() {
            return useBonus;
        }

        public void setUseBonus(String useBonus) {
            this.useBonus = useBonus;
        }

        public String getMaxTeamBonusUse() {
            return maxTeamBonusUse;
        }

        public void setMaxTeamBonusUse(String maxTeamBonusUse) {
            this.maxTeamBonusUse = maxTeamBonusUse;
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

        public String getConEntryStatus() {
            return conEntryStatus;
        }

        public void setConEntryStatus(String conEntryStatus) {
            this.conEntryStatus = conEntryStatus;
        }

        public String getConPlayerEntry() {
            return conPlayerEntry;
        }

        public void setConPlayerEntry(String conPlayerEntry) {
            this.conPlayerEntry = conPlayerEntry;
        }

        public String getWinningTree() {
            return winningTree;
        }

        public void setWinningTree(String winningTree) {
            this.winningTree = winningTree;
        }

        public Object getWinningTreeTest() {
            return winningTreeTest;
        }

        public void setWinningTreeTest(String winningTreeTest) {
            this.winningTreeTest = winningTreeTest;
        }

        public String getWinningTreeText() {
            return winningTreeText;
        }

        public void setWinningTreeText(String winningTreeText) {
            this.winningTreeText = winningTreeText;
        }

        public String getDisplayInList() {
            return displayInList;
        }

        public void setDisplayInList(String displayInList) {
            this.displayInList = displayInList;
        }

        public String getMaxJoinTeam() {
            return maxJoinTeam;
        }

        public void setMaxJoinTeam(String maxJoinTeam) {
            this.maxJoinTeam = maxJoinTeam;
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

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getJoinedTeams() {
            return joinedTeams;
        }

        public void setJoinedTeams(String joinedTeams) {
            this.joinedTeams = joinedTeams;
        }

        public String getMyJoinedTeam() {
            return myJoinedTeam;
        }

        public void setMyJoinedTeam(String myJoinedTeam) {
            this.myJoinedTeam = myJoinedTeam;
        }

        public String getJoinedTeamTempTeamId() {
            return joinedTeamTempTeamId;
        }

        public void setJoinedTeamTempTeamId(String joinedTeamTempTeamId) {
            this.joinedTeamTempTeamId = joinedTeamTempTeamId;
        }

        public String getIsOffer() {
            return isOffer;
        }

        public void setIsOffer(String isOffer) {
            this.isOffer = isOffer;
        }

        public String getOfferPrice() {
            return offerPrice;
        }

        public void setOfferPrice(String offerPrice) {
            this.offerPrice = offerPrice;
        }

        public String getOfferText() {
            return offerText;
        }

        public void setOfferText(String offerText) {
            this.offerText = offerText;
        }

        public String getTotalRank() {
            return totalRank;
        }

        public void setTotalRank(String totalRank) {
            this.totalRank = totalRank;
        }

        public String getTotalPoint() {
            return totalPoint;
        }

        public void setTotalPoint(String totalPoint) {
            this.totalPoint = totalPoint;
        }

        public String getTempTeamName() {
            return tempTeamName;
        }

        public void setTempTeamName(String tempTeamName) {
            this.tempTeamName = tempTeamName;
        }

    }
   /* private class PassModel implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("pass_id")
        @Expose
        private String passId;
        @SerializedName("total_join_spot")
        @Expose
        private String totalJoinSpot;
        @SerializedName("create_at")
        @Expose
        private String createAt;
        @SerializedName("no_of_entry")
        @Expose
        private String noOfEntry;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassId() {
            return passId;
        }

        public void setPassId(String passId) {
            this.passId = passId;
        }

        public String getTotalJoinSpot() {
            return totalJoinSpot;
        }

        public void setTotalJoinSpot(String totalJoinSpot) {
            this.totalJoinSpot = totalJoinSpot;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getNoOfEntry() {
            return noOfEntry;
        }

        public void setNoOfEntry(String noOfEntry) {
            this.noOfEntry = noOfEntry;
        }
    }*/
}