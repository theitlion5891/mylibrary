package com.fantafeat.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class LudoWaitingModal implements Parcelable {

    String entry_fee,no_player,contest_id,user_id,user_name,user_avatar,user_team_name;

    public LudoWaitingModal() {
    }

    protected LudoWaitingModal(Parcel in) {
        entry_fee = in.readString();
        no_player = in.readString();
        contest_id = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        user_avatar = in.readString();
        user_team_name = in.readString();
    }

    public static final Creator<LudoWaitingModal> CREATOR = new Creator<LudoWaitingModal>() {
        @Override
        public LudoWaitingModal createFromParcel(Parcel in) {
            return new LudoWaitingModal(in);
        }

        @Override
        public LudoWaitingModal[] newArray(int size) {
            return new LudoWaitingModal[size];
        }
    };

    public String getUser_team_name() {
        return user_team_name;
    }

    public void setUser_team_name(String user_team_name) {
        this.user_team_name = user_team_name;
    }

    public String getEntry_fee() {
        return entry_fee;
    }

    public void setEntry_fee(String entry_fee) {
        this.entry_fee = entry_fee;
    }

    public String getNo_player() {
        return no_player;
    }

    public void setNo_player(String no_player) {
        this.no_player = no_player;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(no_player);
        parcel.writeString(contest_id);
        parcel.writeString(entry_fee);
        parcel.writeString(user_id);
        parcel.writeString(user_name);
        parcel.writeString(user_avatar);
        parcel.writeString(user_team_name);
    }
}
