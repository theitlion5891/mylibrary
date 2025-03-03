package com.fantafeat.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LudoContestModal implements Parcelable {
    String title,sub_title,id,entry_fee,no_spot,dis_amt,table_id,bonus,game_time,game_round,game_mode,game_sub_mode;
    String game_play_mode_1;
    String game_play_mode_2;
    String game_play_mode_3;
    String game_play_mode_4;
    String game_play_mode_5;
    String game_con_type_id;
    int type,waitCount;
    ArrayList<LudoWaitingModal> waitingModals=new ArrayList<LudoWaitingModal>();

    public LudoContestModal() {
    }

    public LudoContestModal(String title, String sub_title, String id, String entry_fee, String no_spot, String dis_amt,
                            String bonus, int type, int waitCount, ArrayList<LudoWaitingModal> waitingModals) {
        this.title = title;
        this.sub_title = sub_title;
        this.id = id;
        this.entry_fee = entry_fee;
        this.no_spot = no_spot;
        this.dis_amt = dis_amt;
        this.type = type;
        this.waitCount = waitCount;
        this.waitingModals = waitingModals;
        this.bonus = bonus;
    }

    protected LudoContestModal(Parcel in) {
        title = in.readString();
        sub_title = in.readString();
        id = in.readString();
        entry_fee = in.readString();
        no_spot = in.readString();
        dis_amt = in.readString();
        type = in.readInt();
        waitCount = in.readInt();
        table_id = in.readString();
        bonus = in.readString();
        game_time = in.readString();
        waitingModals = in.createTypedArrayList(LudoWaitingModal.CREATOR);
        game_round = in.readString();
        game_mode = in.readString();
        game_sub_mode = in.readString();
        game_play_mode_1 = in.readString();
        game_play_mode_2 = in.readString();
        game_play_mode_3 = in.readString();
        game_play_mode_4 = in.readString();
        game_play_mode_5 = in.readString();
        game_con_type_id = in.readString();
    }

    public static final Creator<LudoContestModal> CREATOR = new Creator<LudoContestModal>() {
        @Override
        public LudoContestModal createFromParcel(Parcel in) {
            return new LudoContestModal(in);
        }

        @Override
        public LudoContestModal[] newArray(int size) {
            return new LudoContestModal[size];
        }
    };

    public String getGame_round() {
        return game_round;
    }

    public void setGame_round(String game_round) {
        this.game_round = game_round;
    }

    public String getGame_mode() {
        return game_mode;
    }

    public void setGame_mode(String game_mode) {
        this.game_mode = game_mode;
    }

    public String getGame_sub_mode() {
        return game_sub_mode;
    }

    public void setGame_sub_mode(String game_sub_mode) {
        this.game_sub_mode = game_sub_mode;
    }

    public String getGame_play_mode_1() {
        return game_play_mode_1;
    }

    public void setGame_play_mode_1(String game_play_mode_1) {
        this.game_play_mode_1 = game_play_mode_1;
    }

    public String getGame_play_mode_2() {
        return game_play_mode_2;
    }

    public void setGame_play_mode_2(String game_play_mode_2) {
        this.game_play_mode_2 = game_play_mode_2;
    }

    public String getGame_play_mode_3() {
        return game_play_mode_3;
    }

    public void setGame_play_mode_3(String game_play_mode_3) {
        this.game_play_mode_3 = game_play_mode_3;
    }

    public String getGame_play_mode_4() {
        return game_play_mode_4;
    }

    public void setGame_play_mode_4(String game_play_mode_4) {
        this.game_play_mode_4 = game_play_mode_4;
    }

    public String getGame_play_mode_5() {
        return game_play_mode_5;
    }

    public void setGame_play_mode_5(String game_play_mode_5) {
        this.game_play_mode_5 = game_play_mode_5;
    }

    public String getGame_con_type_id() {
        return game_con_type_id;
    }

    public void setGame_con_type_id(String game_con_type_id) {
        this.game_con_type_id = game_con_type_id;
    }

    public String getGame_time() {
        return game_time;
    }

    public void setGame_time(String game_time) {
        this.game_time = game_time;
    }

    public ArrayList<LudoWaitingModal> getWaitingModals() {
        return waitingModals;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public void setWaitingModals(ArrayList<LudoWaitingModal> waitingModals) {
        this.waitingModals = waitingModals;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntry_fee() {
        return entry_fee;
    }

    public void setEntry_fee(String entry_fee) {
        this.entry_fee = entry_fee;
    }

    public String getNo_spot() {
        return no_spot;
    }

    public void setNo_spot(String no_spot) {
        this.no_spot = no_spot;
    }

    public String getDis_amt() {
        return dis_amt;
    }

    public void setDis_amt(String dis_amt) {
        this.dis_amt = dis_amt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(sub_title);
        parcel.writeString(id);
        parcel.writeString(entry_fee);
        parcel.writeString(no_spot);
        parcel.writeString(dis_amt);
        parcel.writeInt(type);
        parcel.writeInt(waitCount);
        parcel.writeString(table_id);
        parcel.writeString(bonus);
        parcel.writeTypedList(waitingModals);
        parcel.writeString(game_round);
        parcel.writeString(game_mode);
        parcel.writeString(game_sub_mode);
        parcel.writeString(game_play_mode_1);
        parcel.writeString(game_play_mode_2);
        parcel.writeString(game_play_mode_3);
        parcel.writeString(game_play_mode_4);
        parcel.writeString(game_play_mode_5);
        parcel.writeString(game_con_type_id);
    }
}
