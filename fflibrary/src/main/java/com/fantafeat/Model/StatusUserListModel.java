package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StatusUserListModel implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("read_count")
    @Expose
    private String readCount;
    @SerializedName("unread_count")
    @Expose
    private String unreadCount;
    @SerializedName("user_img")
    @Expose
    private String userImg;
    @SerializedName("story")
    @Expose
    private ArrayList<StoryDetailsModel> storyList;

    public ArrayList<StoryDetailsModel> getStoryList() {
        return storyList;
    }

    public void setStoryList(ArrayList<StoryDetailsModel> storyList) {
        this.storyList = storyList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

}
