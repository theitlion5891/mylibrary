package com.fantafeat.Model;

public class PokerTableModel {

    private String avgStack;
    private String stakes_amount;
    private String min_buy_in;
    private String total_player;
    private String waiting_player;
    private String table_id;
    private String user_id;

    public String getStakes_amount() {
        return stakes_amount;
    }

    public void setStakes_amount(String stakes_amount) {
        this.stakes_amount = stakes_amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvgStack() {
        return avgStack;
    }

    public void setAvgStack(String avgStack) {
        this.avgStack = avgStack;
    }

    public String getMin_buy_in() {
        return min_buy_in;
    }

    public void setMin_buy_in(String min_buy_in) {
        this.min_buy_in = min_buy_in;
    }

    public String getTotal_player() {
        return total_player;
    }

    public void setTotal_player(String total_player) {
        this.total_player = total_player;
    }

    public String getWaiting_player() {
        return waiting_player;
    }

    public void setWaiting_player(String waiting_player) {
        this.waiting_player = waiting_player;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }
}
