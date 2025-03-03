package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoryDetailsModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("story_document")
    @Expose
    private String storyDocument;
    @SerializedName("story_document_type")
    @Expose
    private String storyDocumentType;
    @SerializedName("story_url")
    @Expose
    private String storyUrl;
    @SerializedName("story_text")
    @Expose
    private String storyText;
    @SerializedName("story_bg_color")
    @Expose
    private String storyBgColor;
    @SerializedName("story_type_of_sports")
    @Expose
    private String storyTypeOfSports;
    @SerializedName("story_action")
    @Expose
    private String storyAction;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("sport_id")
    @Expose
    private String sportId;
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("other_data")
    @Expose
    private String otherData;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("share_with_bb11_family")
    @Expose
    private String shareWithBb11Family;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
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

    private long duration=3000L;


    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoryDocument() {
        return storyDocument;
    }

    public void setStoryDocument(String storyDocument) {
        this.storyDocument = storyDocument;
    }

    public String getStoryDocumentType() {
        return storyDocumentType;
    }

    public void setStoryDocumentType(String storyDocumentType) {
        this.storyDocumentType = storyDocumentType;
    }

    public String getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public String getStoryBgColor() {
        return storyBgColor;
    }

    public void setStoryBgColor(String storyBgColor) {
        this.storyBgColor = storyBgColor;
    }

    public String getStoryTypeOfSports() {
        return storyTypeOfSports;
    }

    public void setStoryTypeOfSports(String storyTypeOfSports) {
        this.storyTypeOfSports = storyTypeOfSports;
    }

    public String getStoryAction() {
        return storyAction;
    }

    public void setStoryAction(String storyAction) {
        this.storyAction = storyAction;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShareWithBb11Family() {
        return shareWithBb11Family;
    }

    public void setShareWithBb11Family(String shareWithBb11Family) {
        this.shareWithBb11Family = shareWithBb11Family;
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
