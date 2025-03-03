package com.fantafeat.Model;

public class EventDetailModel {

    String join_fee,option_value,quantity,tag,isExitedOrBuy,sell_bal,total_win_amount,sell_cnt,sold_cnt,total_cnt,availCnt;
    boolean isExitButton=true;

    public String getAvailCnt() {
        return availCnt;
    }

    public void setAvailCnt(String availCnt) {
        this.availCnt = availCnt;
    }

    public boolean isExitButton() {
        return isExitButton;
    }

    public void setExitButton(boolean exitButton) {
        isExitButton = exitButton;
    }

    public String getSold_cnt() {
        return sold_cnt;
    }

    public void setSold_cnt(String sold_cnt) {
        this.sold_cnt = sold_cnt;
    }

    public String getTotal_cnt() {
        return total_cnt;
    }

    public void setTotal_cnt(String total_cnt) {
        this.total_cnt = total_cnt;
    }

    public String getSell_cnt() {
        return sell_cnt;
    }

    public void setSell_cnt(String sell_cnt) {
        this.sell_cnt = sell_cnt;
    }

    public String getTotal_win_amount() {
        return total_win_amount;
    }

    public void setTotal_win_amount(String total_win_amount) {
        this.total_win_amount = total_win_amount;
    }

    public String getSell_bal() {
        return sell_bal;
    }

    public void setSell_bal(String sell_bal) {
        this.sell_bal = sell_bal;
    }

    public String getIsExitedOrBuy() {
        return isExitedOrBuy;
    }

    public void setIsExitedOrBuy(String isExitedOrBuy) {
        this.isExitedOrBuy = isExitedOrBuy;
    }

    public String getJoin_fee() {
        return join_fee;
    }

    public void setJoin_fee(String join_fee) {
        this.join_fee = join_fee;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
