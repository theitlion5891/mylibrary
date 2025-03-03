package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameContestModel implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    int type=0;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("game_mode")
    @Expose
    private String gameMode;
    @SerializedName("game_sub_mode")
    @Expose
    private String gameSubMode;
    @SerializedName("entry_fee")
    @Expose
    private String entryFee;
    @SerializedName("game_use_coin")
    @Expose
    private String game_use_coin;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("dis_amt")
    @Expose
    private String disAmt;
    @SerializedName("game_play_mode_1")
    @Expose
    private String gamePlayMode1;
    @SerializedName("game_play_mode_2")
    @Expose
    private String gamePlayMode2;
    @SerializedName("game_play_mode_3")
    @Expose
    private String gamePlayMode3;
    @SerializedName("game_play_mode_4")
    @Expose
    private String gamePlayMode4;
    @SerializedName("game_play_mode_5")
    @Expose
    private String gamePlayMode5;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("game_con_type_id")
    @Expose
    private String gameConTypeId;
    @SerializedName("bonus_text")
    @Expose
    private String bonusText;
    @SerializedName("bonus")
    @Expose
    private String bonus;
    @SerializedName("no_spot")
    @Expose
    private String no_spot;
    @SerializedName("game_round")
    @Expose
    private String game_round;
    @SerializedName("game_time")
    @Expose
    private String game_time;

    @SerializedName("is_pass")
    @Expose
    private String is_pass;

    @SerializedName("my_pass_data")
    @Expose
    private List<PassDatam> my_pass_data;

    private String pokerTableId;
    private String pokerUserId;
    private String pokerStackAmt;
    private String pokerMinBuyIn;
    private String pokerNoOfPlaying;
    @SerializedName("game_profit_amt")
    @Expose
    private String game_profit_amt;

    @SerializedName("user_token_position")
    @Expose
    private String user_token_position;
    boolean isPassApplicable=false;
    String asset_type;

    int waitCount;


    ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();

    @SerializedName("lb_id")
    @Expose
    private String lb_id;

    @SerializedName("shield")
    @Expose
    private String shield;
    @SerializedName("bomb")
    @Expose
    private String bomb;
    @SerializedName("rocket")
    @Expose
    private String rocket;
    @SerializedName("extra_turn")
    @Expose
    private String extra_turn;
    @SerializedName("even_number")
    @Expose
    private String even_number;
    @SerializedName("odd_number")
    @Expose
    private String odd_number;
    @SerializedName("high_number")
    @Expose
    private String high_number;
    @SerializedName("low_number")
    @Expose
    private String low_number;

    public String getShield() {
        return shield;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public String getBomb() {
        return bomb;
    }

    public void setBomb(String bomb) {
        this.bomb = bomb;
    }

    public String getRocket() {
        return rocket;
    }

    public void setRocket(String rocket) {
        this.rocket = rocket;
    }

    public String getExtra_turn() {
        return extra_turn;
    }

    public void setExtra_turn(String extra_turn) {
        this.extra_turn = extra_turn;
    }

    public String getEven_number() {
        return even_number;
    }

    public void setEven_number(String even_number) {
        this.even_number = even_number;
    }

    public String getOdd_number() {
        return odd_number;
    }

    public void setOdd_number(String odd_number) {
        this.odd_number = odd_number;
    }

    public String getHigh_number() {
        return high_number;
    }

    public void setHigh_number(String high_number) {
        this.high_number = high_number;
    }

    public String getLow_number() {
        return low_number;
    }

    public void setLow_number(String low_number) {
        this.low_number = low_number;
    }

    public String getLb_id() {
        return lb_id;
    }

    public void setLb_id(String lb_id) {
        this.lb_id = lb_id;
    }

    public String getUser_token_position() {
        return user_token_position;
    }

    public void setUser_token_position(String user_token_position) {
        this.user_token_position = user_token_position;
    }

    public String getGame_profit_amt() {
        return game_profit_amt;
    }

    public void setGame_profit_amt(String game_profit_amt) {
        this.game_profit_amt = game_profit_amt;
    }

    public String getPokerTableId() {
        return pokerTableId;
    }

    public void setPokerTableId(String pokerTableId) {
        this.pokerTableId = pokerTableId;
    }

    public String getPokerUserId() {
        return pokerUserId;
    }

    public void setPokerUserId(String pokerUserId) {
        this.pokerUserId = pokerUserId;
    }

    public String getPokerStackAmt() {
        return pokerStackAmt;
    }

    public void setPokerStackAmt(String pokerStackAmt) {
        this.pokerStackAmt = pokerStackAmt;
    }

    public String getPokerMinBuyIn() {
        return pokerMinBuyIn;
    }

    public void setPokerMinBuyIn(String pokerMinBuyIn) {
        this.pokerMinBuyIn = pokerMinBuyIn;
    }

    public String getPokerNoOfPlaying() {
        return pokerNoOfPlaying;
    }

    public void setPokerNoOfPlaying(String pokerNoOfPlaying) {
        this.pokerNoOfPlaying = pokerNoOfPlaying;
    }

    public String getGame_use_coin() {
        return game_use_coin;
    }

    public void setGame_use_coin(String game_use_coin) {
        this.game_use_coin = game_use_coin;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getGame_time() {
        return game_time;
    }

    public void setGame_time(String game_time) {
        this.game_time = game_time;
    }

    public String getGame_round() {
        return game_round;
    }

    public void setGame_round(String game_round) {
        this.game_round = game_round;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public ArrayList<LudoWaitingModal> getWaitingModals() {
        return waitingModals;
    }

    public void setWaitingModals(ArrayList<LudoWaitingModal> waitingModals) {
        this.waitingModals = waitingModals;
    }

    public String getNo_spot() {
        return no_spot;
    }

    public void setNo_spot(String no_spot) {
        this.no_spot = no_spot;
    }

    public boolean isPassApplicable() {
        return isPassApplicable;
    }

    public void setPassApplicable(boolean passApplicable) {
        isPassApplicable = passApplicable;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
    }

    public List<PassDatam> getMy_pass_data() {
        return my_pass_data;
    }

    public void setMy_pass_data(List<PassDatam> my_pass_data) {
        this.my_pass_data = my_pass_data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameSubMode() {
        return gameSubMode;
    }

    public void setGameSubMode(String gameSubMode) {
        this.gameSubMode = gameSubMode;
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

    public String getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(String disAmt) {
        this.disAmt = disAmt;
    }

    public String getGamePlayMode1() {
        return gamePlayMode1;
    }

    public void setGamePlayMode1(String gamePlayMode1) {
        this.gamePlayMode1 = gamePlayMode1;
    }

    public String getGamePlayMode2() {
        return gamePlayMode2;
    }

    public void setGamePlayMode2(String gamePlayMode2) {
        this.gamePlayMode2 = gamePlayMode2;
    }

    public String getGamePlayMode3() {
        return gamePlayMode3;
    }

    public void setGamePlayMode3(String gamePlayMode3) {
        this.gamePlayMode3 = gamePlayMode3;
    }

    public String getGamePlayMode4() {
        return gamePlayMode4;
    }

    public void setGamePlayMode4(String gamePlayMode4) {
        this.gamePlayMode4 = gamePlayMode4;
    }

    public String getGamePlayMode5() {
        return gamePlayMode5;
    }

    public void setGamePlayMode5(String gamePlayMode5) {
        this.gamePlayMode5 = gamePlayMode5;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getGameConTypeId() {
        return gameConTypeId;
    }

    public void setGameConTypeId(String gameConTypeId) {
        this.gameConTypeId = gameConTypeId;
    }

    public String getBonusText() {
        return bonusText;
    }

    public void setBonusText(String bonusText) {
        this.bonusText = bonusText;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public static class PassDatam implements Serializable{
        @SerializedName("create_at")
        @Expose
        String create_at;
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("no_of_entry")
        @Expose
        String no_of_entry;
        @SerializedName("pass_id")
        @Expose
        String pass_id;
        @SerializedName("total_join_spot")
        @Expose
        String total_join_spot;



        public String getCreate_at() {
            return create_at;
        }

        public void setCreate_at(String create_at) {
            this.create_at = create_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNo_of_entry() {
            return no_of_entry;
        }

        public void setNo_of_entry(String no_of_entry) {
            this.no_of_entry = no_of_entry;
        }

        public String getPass_id() {
            return pass_id;
        }

        public void setPass_id(String pass_id) {
            this.pass_id = pass_id;
        }

        public String getTotal_join_spot() {
            return total_join_spot;
        }

        public void setTotal_join_spot(String total_join_spot) {
            this.total_join_spot = total_join_spot;
        }

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


}
