package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    int type=0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("con_status")
    @Expose
    private String con_status;
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
    private String conImage;
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
    @SerializedName("investment_amount")
    @Expose
    private String investmentAmount;
    @SerializedName("win_amount")
    @Expose
    private String winAmount;
    @SerializedName("refund_amount")
    @Expose
    private String refundAmount;
    @SerializedName("exit_amount")
    @Expose
    private String exitAmount;
    @SerializedName("total_trades_count")
    @Expose
    private String totalTradesCount;
    @SerializedName("winning_tree")
    @Expose
    private String winningTree;
    @SerializedName("my_total_trades_count")
    @Expose
    private String my_total_trades_count;
    @SerializedName("my_total_confirm_trades")
    @Expose
    private String my_total_confirm_trades;

    @SerializedName("filter_tags_name")
    @Expose
    private String filter_tags_name;
    @SerializedName("display_tags_name")
    @Expose
    private String display_tags_name;

    @SerializedName("source_agency")
    @Expose
    private String source_agency;
    @SerializedName("con_type")
    @Expose
    private String con_type;
    @SerializedName("total_join_cnt")
    @Expose
    private String total_join_cnt;
    @SerializedName("join_spot_limit")
    @Expose
    private String join_spot_limit;
    @SerializedName("no_of_spot")
    @Expose
    private String no_of_spot;
    @SerializedName("no_of_winners")
    @Expose
    private String no_of_winners;
    @SerializedName("option_1_cnt")
    @Expose
    private String option_1_cnt;
    @SerializedName("option_2_cnt")
    @Expose
    private String option_2_cnt;
    @SerializedName("option_3_cnt")
    @Expose
    private String option_3_cnt;
    @SerializedName("option_4_cnt")
    @Expose
    private String option_4_cnt;
    @SerializedName("option_5_cnt")
    @Expose
    private String option_5_cnt;
    @SerializedName("con_overview")
    @Expose
    private String con_overview;
    @SerializedName("trade_board")
    @Expose
    private List<TradeBoard> tradeBoard;

    private String optionPrice;
    private String optionValue;
    private String optionEntryFee;
    private String con_join_qty;
    private String selectedPrice;

    private boolean isNewBadge = false;

    @SerializedName("opinion_value")
    @Expose
    private String opinion_value;
    @SerializedName("sell_bal")
    @Expose
    private String sell_bal;

    @SerializedName("my_join_id")
    @Expose
    private String my_join_id;
    @SerializedName("join_win_amt")
    @Expose
    private String join_win_amt;
    @SerializedName("join_ef")
    @Expose
    private String join_ef;

    private ArrayList<EventModel> confirmTradeList;

    public String getMy_join_id() {
        return my_join_id;
    }

    public void setMy_join_id(String my_join_id) {
        this.my_join_id = my_join_id;
    }

    public String getJoin_win_amt() {
        return join_win_amt;
    }

    public void setJoin_win_amt(String join_win_amt) {
        this.join_win_amt = join_win_amt;
    }

    public String getJoin_ef() {
        return join_ef;
    }

    public void setJoin_ef(String join_ef) {
        this.join_ef = join_ef;
    }

    public String getOpinion_value() {
        return opinion_value;
    }

    public void setOpinion_value(String opinion_value) {
        this.opinion_value = opinion_value;
    }

    public String getSell_bal() {
        return sell_bal;
    }

    public void setSell_bal(String sell_bal) {
        this.sell_bal = sell_bal;
    }

    public String getCon_status() {
        return con_status;
    }

    public void setCon_status(String con_status) {
        this.con_status = con_status;
    }

    public String getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(String optionPrice) {
        this.optionPrice = optionPrice;
    }

    public ArrayList<EventModel> getConfirmTradeList() {
        return confirmTradeList;
    }

    public void setConfirmTradeList(ArrayList<EventModel> confirmTradeList) {
        this.confirmTradeList = confirmTradeList;
    }

    public List<TradeBoard> getTradeBoard() {
        return tradeBoard;
    }

    public void setTradeBoard(List<TradeBoard> tradeBoard) {
        this.tradeBoard = tradeBoard;
    }
    public String getOptionEntryFee() {
        return optionEntryFee;
    }

    public void setOptionEntryFee(String optionEntryFee) {
        this.optionEntryFee = optionEntryFee;
    }

    public String getCon_overview() {
        return con_overview;
    }

    public void setCon_overview(String con_overview) {
        this.con_overview = con_overview;
    }

    public String getOption_1_cnt() {
        return option_1_cnt;
    }

    public void setOption_1_cnt(String option_1_cnt) {
        this.option_1_cnt = option_1_cnt;
    }

    public String getOption_2_cnt() {
        return option_2_cnt;
    }

    public void setOption_2_cnt(String option_2_cnt) {
        this.option_2_cnt = option_2_cnt;
    }

    public String getOption_3_cnt() {
        return option_3_cnt;
    }

    public void setOption_3_cnt(String option_3_cnt) {
        this.option_3_cnt = option_3_cnt;
    }

    public String getOption_4_cnt() {
        return option_4_cnt;
    }

    public void setOption_4_cnt(String option_4_cnt) {
        this.option_4_cnt = option_4_cnt;
    }

    public String getOption_5_cnt() {
        return option_5_cnt;
    }

    public void setOption_5_cnt(String option_5_cnt) {
        this.option_5_cnt = option_5_cnt;
    }

    public String getNo_of_winners() {
        return no_of_winners;
    }

    public void setNo_of_winners(String no_of_winners) {
        this.no_of_winners = no_of_winners;
    }

    public String getNo_of_spot() {
        return no_of_spot;
    }

    public void setNo_of_spot(String no_of_spot) {
        this.no_of_spot = no_of_spot;
    }

    public String getJoin_spot_limit() {
        return join_spot_limit;
    }

    public void setJoin_spot_limit(String join_spot_limit) {
        this.join_spot_limit = join_spot_limit;
    }

    public String getTotal_join_cnt() {
        return total_join_cnt;
    }

    public void setTotal_join_cnt(String total_join_cnt) {
        this.total_join_cnt = total_join_cnt;
    }

    public String getCon_type() {
        return con_type;
    }

    public void setCon_type(String con_type) {
        this.con_type = con_type;
    }

    public boolean isNewBadge() {
        return isNewBadge;
    }

    public void setNewBadge(boolean newBadge) {
        isNewBadge = newBadge;
    }

    public String getSource_agency() {
        return source_agency;
    }

    public void setSource_agency(String source_agency) {
        this.source_agency = source_agency;
    }

    public String getFilter_tags_name() {
        return filter_tags_name;
    }

    public void setFilter_tags_name(String filter_tags_name) {
        this.filter_tags_name = filter_tags_name;
    }

    public String getDisplay_tags_name() {
        return display_tags_name;
    }

    public void setDisplay_tags_name(String display_tags_name) {
        this.display_tags_name = display_tags_name;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getCon_join_qty() {
        return con_join_qty;
    }

    public void setCon_join_qty(String con_join_qty) {
        this.con_join_qty = con_join_qty;
    }

    public String getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(String selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public String getMy_total_trades_count() {
        return my_total_trades_count;
    }

    public void setMy_total_trades_count(String my_total_trades_count) {
        this.my_total_trades_count = my_total_trades_count;
    }

    public String getMy_total_confirm_trades() {
        return my_total_confirm_trades;
    }

    public void setMy_total_confirm_trades(String my_total_confirm_trades) {
        this.my_total_confirm_trades = my_total_confirm_trades;
    }

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

    public String getConImage() {
        return conImage;
    }

    public void setConImage(String conImage) {
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

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(String winAmount) {
        this.winAmount = winAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getExitAmount() {
        return exitAmount;
    }

    public void setExitAmount(String exitAmount) {
        this.exitAmount = exitAmount;
    }

    public String getTotalTradesCount() {
        return totalTradesCount;
    }

    public void setTotalTradesCount(String totalTradesCount) {
        this.totalTradesCount = totalTradesCount;
    }

    public String getWinningTree() {
        return winningTree;
    }

    public void setWinningTree(String winningTree) {
        this.winningTree = winningTree;
    }


}
