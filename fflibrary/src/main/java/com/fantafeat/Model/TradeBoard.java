package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TradeBoard implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("con_desc")
    @Expose
    private String conDesc;
    @SerializedName("con_sub_desc")
    @Expose
    private String conSubDesc;
    @SerializedName("con_image")
    @Expose
    private Object conImage;
    @SerializedName("entry_fee")
    @Expose
    private String entryFee;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("distribute_amount")
    @Expose
    private String distributeAmount;
    @SerializedName("commission_amount")
    @Expose
    private String commissionAmount;
    @SerializedName("option_1")
    @Expose
    private String option1;
    @SerializedName("option_2")
    @Expose
    private String option2;
    @SerializedName("option_3")
    @Expose
    private String option3;
    @SerializedName("option_4")
    @Expose
    private String option4;
    @SerializedName("option_5")
    @Expose
    private String option5;
    @SerializedName("option_img_1")
    @Expose
    private String optionImg1;
    @SerializedName("option_img_2")
    @Expose
    private String optionImg2;
    @SerializedName("option_img_3")
    @Expose
    private String optionImg3;
    @SerializedName("option_img_4")
    @Expose
    private String optionImg4;
    @SerializedName("option_img_5")
    @Expose
    private String optionImg5;
    @SerializedName("con_start_time")
    @Expose
    private String conStartTime;
    @SerializedName("con_end_time")
    @Expose
    private String conEndTime;
    @SerializedName("con_winning")
    @Expose
    private String conWinning;
    @SerializedName("con_entry_status")
    @Expose
    private String conEntryStatus;
    @SerializedName("con_status")
    @Expose
    private String conStatus;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("total_trades")
    @Expose
    private String totalTrades;
    @SerializedName("total_confirmed_trades")
    @Expose
    private String totalConfirmedTrades;
    @SerializedName("option_1_price")
    @Expose
    private String option1Price;
    @SerializedName("option_2_price")
    @Expose
    private String option2Price;
    @SerializedName("option_3_price")
    @Expose
    private String option3Price;
    @SerializedName("option_4_price")
    @Expose
    private String option4Price;
    @SerializedName("option_5_price")
    @Expose
    private String option5Price;
    @SerializedName("is_winn_credit")
    @Expose
    private String isWinnCredit;
    @SerializedName("filter_tags_name")
    @Expose
    private String filterTagsName;
    @SerializedName("display_tags_name")
    @Expose
    private Object displayTagsName;
    @SerializedName("source_agency")
    @Expose
    private String sourceAgency;
    @SerializedName("con_overview")
    @Expose
    private Object conOverview;
    @SerializedName("option_1_text")
    @Expose
    private Object option1Text;
    @SerializedName("option_2_text")
    @Expose
    private Object option2Text;
    @SerializedName("option_3_text")
    @Expose
    private Object option3Text;
    @SerializedName("option_4_text")
    @Expose
    private Object option4Text;
    @SerializedName("option_5_text")
    @Expose
    private Object option5Text;
    @SerializedName("max_join_count_limit")
    @Expose
    private String maxJoinCountLimit;
    @SerializedName("winning_tree")
    @Expose
    private String winningTree;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getConDesc() {
        return conDesc;
    }

    public void setConDesc(String conDesc) {
        this.conDesc = conDesc;
    }

    public String getConSubDesc() {
        return conSubDesc;
    }

    public void setConSubDesc(String conSubDesc) {
        this.conSubDesc = conSubDesc;
    }

    public Object getConImage() {
        return conImage;
    }

    public void setConImage(Object conImage) {
        this.conImage = conImage;
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

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public String getOptionImg1() {
        return optionImg1;
    }

    public void setOptionImg1(String optionImg1) {
        this.optionImg1 = optionImg1;
    }

    public String getOptionImg2() {
        return optionImg2;
    }

    public void setOptionImg2(String optionImg2) {
        this.optionImg2 = optionImg2;
    }

    public String getOptionImg3() {
        return optionImg3;
    }

    public void setOptionImg3(String optionImg3) {
        this.optionImg3 = optionImg3;
    }

    public String getOptionImg4() {
        return optionImg4;
    }

    public void setOptionImg4(String optionImg4) {
        this.optionImg4 = optionImg4;
    }

    public String getOptionImg5() {
        return optionImg5;
    }

    public void setOptionImg5(String optionImg5) {
        this.optionImg5 = optionImg5;
    }

    public String getConStartTime() {
        return conStartTime;
    }

    public void setConStartTime(String conStartTime) {
        this.conStartTime = conStartTime;
    }

    public String getConEndTime() {
        return conEndTime;
    }

    public void setConEndTime(String conEndTime) {
        this.conEndTime = conEndTime;
    }

    public String getConWinning() {
        return conWinning;
    }

    public void setConWinning(String conWinning) {
        this.conWinning = conWinning;
    }

    public String getConEntryStatus() {
        return conEntryStatus;
    }

    public void setConEntryStatus(String conEntryStatus) {
        this.conEntryStatus = conEntryStatus;
    }

    public String getConStatus() {
        return conStatus;
    }

    public void setConStatus(String conStatus) {
        this.conStatus = conStatus;
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

    public String getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(String totalTrades) {
        this.totalTrades = totalTrades;
    }

    public String getTotalConfirmedTrades() {
        return totalConfirmedTrades;
    }

    public void setTotalConfirmedTrades(String totalConfirmedTrades) {
        this.totalConfirmedTrades = totalConfirmedTrades;
    }

    public String getOption1Price() {
        return option1Price;
    }

    public void setOption1Price(String option1Price) {
        this.option1Price = option1Price;
    }

    public String getOption2Price() {
        return option2Price;
    }

    public void setOption2Price(String option2Price) {
        this.option2Price = option2Price;
    }

    public String getOption3Price() {
        return option3Price;
    }

    public void setOption3Price(String option3Price) {
        this.option3Price = option3Price;
    }

    public String getOption4Price() {
        return option4Price;
    }

    public void setOption4Price(String option4Price) {
        this.option4Price = option4Price;
    }

    public String getOption5Price() {
        return option5Price;
    }

    public void setOption5Price(String option5Price) {
        this.option5Price = option5Price;
    }

    public String getIsWinnCredit() {
        return isWinnCredit;
    }

    public void setIsWinnCredit(String isWinnCredit) {
        this.isWinnCredit = isWinnCredit;
    }

    public String getFilterTagsName() {
        return filterTagsName;
    }

    public void setFilterTagsName(String filterTagsName) {
        this.filterTagsName = filterTagsName;
    }

    public Object getDisplayTagsName() {
        return displayTagsName;
    }

    public void setDisplayTagsName(Object displayTagsName) {
        this.displayTagsName = displayTagsName;
    }

    public String getSourceAgency() {
        return sourceAgency;
    }

    public void setSourceAgency(String sourceAgency) {
        this.sourceAgency = sourceAgency;
    }

    public Object getConOverview() {
        return conOverview;
    }

    public void setConOverview(Object conOverview) {
        this.conOverview = conOverview;
    }

    public Object getOption1Text() {
        return option1Text;
    }

    public void setOption1Text(Object option1Text) {
        this.option1Text = option1Text;
    }

    public Object getOption2Text() {
        return option2Text;
    }

    public void setOption2Text(Object option2Text) {
        this.option2Text = option2Text;
    }

    public Object getOption3Text() {
        return option3Text;
    }

    public void setOption3Text(Object option3Text) {
        this.option3Text = option3Text;
    }

    public Object getOption4Text() {
        return option4Text;
    }

    public void setOption4Text(Object option4Text) {
        this.option4Text = option4Text;
    }

    public Object getOption5Text() {
        return option5Text;
    }

    public void setOption5Text(Object option5Text) {
        this.option5Text = option5Text;
    }

    public String getMaxJoinCountLimit() {
        return maxJoinCountLimit;
    }

    public void setMaxJoinCountLimit(String maxJoinCountLimit) {
        this.maxJoinCountLimit = maxJoinCountLimit;
    }

    public String getWinningTree() {
        return winningTree;
    }

    public void setWinningTree(String winningTree) {
        this.winningTree = winningTree;
    }
}
