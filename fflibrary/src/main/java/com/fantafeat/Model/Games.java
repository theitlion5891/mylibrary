package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Games implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("game_code")
    @Expose
    private String gameCode;
    @SerializedName("game_name")
    @Expose
    private String gameName;
    @SerializedName("game_desc")
    @Expose
    private String gameDesc;
    @SerializedName("game_cat_id")
    @Expose
    private String gameCatId;
    @SerializedName("game_cat_name")
    @Expose
    private String gameCatName;
    @SerializedName("game_label")
    @Expose
    private String gameLabel;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("asset_type")
    @Expose
    private String assetType;
    @SerializedName("socket_url")
    @Expose
    private String socketUrl;
    @SerializedName("img_square")
    @Expose
    private String imgSquare;
    @SerializedName("img_vertical")
    @Expose
    private String imgVertical;
    @SerializedName("img_horizontal")
    @Expose
    private String imgHorizontal;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("android_app_ver")
    @Expose
    private String androidAppVer;
    @SerializedName("android_update_type")
    @Expose
    private String androidUpdateType;
    @SerializedName("android_update_msg")
    @Expose
    private String androidUpdateMsg;
    @SerializedName("android_asset_url")
    @Expose
    private String androidAssetUrl;
    @SerializedName("android_asset_url_test")
    @Expose
    private String androidAssetUrlTest;
    @SerializedName("ios_app_ver")
    @Expose
    private String iosAppVer;
    @SerializedName("ios_update_type")
    @Expose
    private String iosUpdateType;
    @SerializedName("ios_update_msg")
    @Expose
    private String iosUpdateMsg;
    @SerializedName("ios_asset_url")
    @Expose
    private String iosAssetUrl;
    @SerializedName("is_maintanance")
    @Expose
    private String isMaintanance;
    @SerializedName("maintanance_msg")
    @Expose
    private String maintananceMsg;
    @SerializedName("game_status")
    @Expose
    private String gameStatus;
    @SerializedName("no_of_player")
    @Expose
    private String noOfPlayer;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("games_tnc")
    @Expose
    private String gamesTnc;
    @SerializedName("no_token_win")
    @Expose
    private String no_token_win;
    @SerializedName("socket_url_test")
    @Expose
    private String socket_url_test;
    @SerializedName("token_create_url")
    @Expose
    private String token_create_url;
    @SerializedName("token_create_url_test")
    @Expose
    private String token_create_url_test;
    @SerializedName("game_mode")
    @Expose
    private String game_mode;
    @SerializedName("is_auto_game_start")
    @Expose
    private String is_auto_game_start;
    @SerializedName("is_contest_waiting")
    @Expose
    private String is_contest_waiting;
    /*@SerializedName("game_mode")
    @Expose
    private String game_mode;

    public String getGame_mode() {
        return game_mode;
    }

    public void setGame_mode(String game_mode) {
        this.game_mode = game_mode;
    }*/

    public String getIs_contest_waiting() {
        return is_contest_waiting;
    }

    public void setIs_contest_waiting(String is_contest_waiting) {
        this.is_contest_waiting = is_contest_waiting;
    }

    public String getIs_auto_game_start() {
        return is_auto_game_start;
    }

    public void setIs_auto_game_start(String is_auto_game_start) {
        this.is_auto_game_start = is_auto_game_start;
    }

    public String getGame_mode() {
        return game_mode;
    }

    public void setGame_mode(String game_mode) {
        this.game_mode = game_mode;
    }

    public String getToken_create_url_test() {
        return token_create_url_test;
    }

    public void setToken_create_url_test(String token_create_url_test) {
        this.token_create_url_test = token_create_url_test;
    }

    public String getToken_create_url() {
        return token_create_url;
    }

    public void setToken_create_url(String token_create_url) {
        this.token_create_url = token_create_url;
    }

    public String getAndroidAssetUrlTest() {
        return androidAssetUrlTest;
    }

    public void setAndroidAssetUrlTest(String androidAssetUrlTest) {
        this.androidAssetUrlTest = androidAssetUrlTest;
    }

    public String getSocket_url_test() {
        return socket_url_test;
    }

    public void setSocket_url_test(String socket_url_test) {
        this.socket_url_test = socket_url_test;
    }

    public String getNo_token_win() {
        return no_token_win;
    }

    public void setNo_token_win(String no_token_win) {
        this.no_token_win = no_token_win;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDesc() {
        return gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public String getGameCatId() {
        return gameCatId;
    }

    public void setGameCatId(String gameCatId) {
        this.gameCatId = gameCatId;
    }

    public String getGameCatName() {
        return gameCatName;
    }

    public void setGameCatName(String gameCatName) {
        this.gameCatName = gameCatName;
    }

    public String getGameLabel() {
        return gameLabel;
    }

    public void setGameLabel(String gameLabel) {
        this.gameLabel = gameLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public String getImgSquare() {
        return imgSquare;
    }

    public void setImgSquare(String imgSquare) {
        this.imgSquare = imgSquare;
    }

    public String getImgVertical() {
        return imgVertical;
    }

    public void setImgVertical(String imgVertical) {
        this.imgVertical = imgVertical;
    }

    public String getImgHorizontal() {
        return imgHorizontal;
    }

    public void setImgHorizontal(String imgHorizontal) {
        this.imgHorizontal = imgHorizontal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAndroidAppVer() {
        return androidAppVer;
    }

    public void setAndroidAppVer(String androidAppVer) {
        this.androidAppVer = androidAppVer;
    }

    public String getAndroidUpdateType() {
        return androidUpdateType;
    }

    public void setAndroidUpdateType(String androidUpdateType) {
        this.androidUpdateType = androidUpdateType;
    }

    public String getAndroidUpdateMsg() {
        return androidUpdateMsg;
    }

    public void setAndroidUpdateMsg(String androidUpdateMsg) {
        this.androidUpdateMsg = androidUpdateMsg;
    }

    public String getAndroidAssetUrl() {
        return androidAssetUrl;
    }

    public void setAndroidAssetUrl(String androidAssetUrl) {
        this.androidAssetUrl = androidAssetUrl;
    }

    public String getIosAppVer() {
        return iosAppVer;
    }

    public void setIosAppVer(String iosAppVer) {
        this.iosAppVer = iosAppVer;
    }

    public String getIosUpdateType() {
        return iosUpdateType;
    }

    public void setIosUpdateType(String iosUpdateType) {
        this.iosUpdateType = iosUpdateType;
    }

    public String getIosUpdateMsg() {
        return iosUpdateMsg;
    }

    public void setIosUpdateMsg(String iosUpdateMsg) {
        this.iosUpdateMsg = iosUpdateMsg;
    }

    public String getIosAssetUrl() {
        return iosAssetUrl;
    }

    public void setIosAssetUrl(String iosAssetUrl) {
        this.iosAssetUrl = iosAssetUrl;
    }

    public String getIsMaintanance() {
        return isMaintanance;
    }

    public void setIsMaintanance(String isMaintanance) {
        this.isMaintanance = isMaintanance;
    }

    public String getMaintananceMsg() {
        return maintananceMsg;
    }

    public void setMaintananceMsg(String maintananceMsg) {
        this.maintananceMsg = maintananceMsg;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getNoOfPlayer() {
        return noOfPlayer;
    }

    public void setNoOfPlayer(String noOfPlayer) {
        this.noOfPlayer = noOfPlayer;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getGamesTnc() {
        return gamesTnc;
    }

    public void setGamesTnc(String gamesTnc) {
        this.gamesTnc = gamesTnc;
    }

}
