package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GameMainModal {

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("cat_name")
        @Expose
        private String catName;
        @SerializedName("cat_type")
        @Expose
        private String catType;
        @SerializedName("cat_id")
        @Expose
        private String catId;
        @SerializedName("games")
        @Expose
        private List<Game> games = null;

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public String getCatType() {
            return catType;
        }

        public void setCatType(String catType) {
            this.catType = catType;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public List<Game> getGames() {
            return games;
        }

        public void setGames(List<Game> games) {
            this.games = games;
        }

        public class Game implements Serializable{

            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("img")
            @Expose
            private String img;
            @SerializedName("is_maintainance")
            @Expose
            private String isMaintainance;
            @SerializedName("is_update")
            @Expose
            private String isUpdate;
            @SerializedName("update_type")
            @Expose
            private String updateType;
            @SerializedName("game_label")
            @Expose
            private String gameLabel;
            @SerializedName("socket_url")
            @Expose
            private String socketUrl;
            @SerializedName("asset_url")
            @Expose
            private String assetUrl;
            @SerializedName("no_of_player")
            @Expose
            private Integer noOfPlayer;
            @SerializedName("bg_color")
            @Expose
            private String bg_color;

            private boolean isProgress=false;
            private int progress=0;

            public String getBg_color() {
                return bg_color;
            }

            public void setBg_color(String bg_color) {
                this.bg_color = bg_color;
            }

            public int getProgress() {
                return progress;
            }

            public void setProgress(int progress) {
                this.progress = progress;
            }

            public boolean isProgress() {
                return isProgress;
            }

            public void setIsProgress(boolean progress) {
                isProgress = progress;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getIsMaintainance() {
                return isMaintainance;
            }

            public void setIsMaintainance(String isMaintainance) {
                this.isMaintainance = isMaintainance;
            }

            public String getIsUpdate() {
                return isUpdate;
            }

            public void setIsUpdate(String isUpdate) {
                this.isUpdate = isUpdate;
            }

            public String getUpdateType() {
                return updateType;
            }

            public void setUpdateType(String updateType) {
                this.updateType = updateType;
            }

            public String getGameLabel() {
                return gameLabel;
            }

            public void setGameLabel(String gameLabel) {
                this.gameLabel = gameLabel;
            }

            public String getSocketUrl() {
                return socketUrl;
            }

            public void setSocketUrl(String socketUrl) {
                this.socketUrl = socketUrl;
            }

            public String getAssetUrl() {
                return assetUrl;
            }

            public void setAssetUrl(String assetUrl) {
                this.assetUrl = assetUrl;
            }

            public Integer getNoOfPlayer() {
                return noOfPlayer;
            }

            public void setNoOfPlayer(Integer noOfPlayer) {
                this.noOfPlayer = noOfPlayer;
            }

        }

    }

}
