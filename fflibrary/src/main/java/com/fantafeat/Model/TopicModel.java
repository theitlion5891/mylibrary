package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopicModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("topic_name")
    @Expose
    private String topicName;
    @SerializedName("topic_order_no")
    @Expose
    private String topicOrderNo;
    @SerializedName("topic_status")
    @Expose
    private String topicStatus;
    @SerializedName("topic_trans_tag_name")
    @Expose
    private String topicTransTagName;
    @SerializedName("cat_name")
    @Expose
    private String cat_name;

    private boolean isSelected=false;

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicOrderNo() {
        return topicOrderNo;
    }

    public void setTopicOrderNo(String topicOrderNo) {
        this.topicOrderNo = topicOrderNo;
    }

    public String getTopicStatus() {
        return topicStatus;
    }

    public void setTopicStatus(String topicStatus) {
        this.topicStatus = topicStatus;
    }

    public String getTopicTransTagName() {
        return topicTransTagName;
    }

    public void setTopicTransTagName(String topicTransTagName) {
        this.topicTransTagName = topicTransTagName;
    }

}
