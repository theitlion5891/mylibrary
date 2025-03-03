package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("clbk_id")
    @Expose
    private String clbkId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("documents_name")
    @Expose
    private String documentsName;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("allow_doc_upload")
    @Expose
    private String allow_doc_upload;

    private boolean isMe=false;

    public String getAllow_doc_upload() {
        return allow_doc_upload;
    }

    public void setAllow_doc_upload(String allow_doc_upload) {
        this.allow_doc_upload = allow_doc_upload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClbkId() {
        return clbkId;
    }

    public void setClbkId(String clbkId) {
        this.clbkId = clbkId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocumentsName() {
        return documentsName;
    }

    public void setDocumentsName(String documentsName) {
        this.documentsName = documentsName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }



    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
