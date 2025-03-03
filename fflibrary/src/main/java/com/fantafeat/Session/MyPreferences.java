package com.fantafeat.Session;

import android.content.Context;
import android.content.SharedPreferences;


import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.MenuModel;
import com.fantafeat.Model.OfferModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.SportsDetailModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.StateModal;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.util.PrefConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyPreferences {
    private Context mContext;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private static final String IS_NEW_USER = "is_new_user";

    public  MyPreferences(Context mContext){
        this.mContext = mContext;
        mPref = mContext.getSharedPreferences(PrefConstant.FANTAFEAT_GROUP,Context.MODE_PRIVATE);
        editor = mPref.edit();
        gson = new Gson();
    }

    public void setPref(String constant_key, int value){
        editor.putInt(constant_key,value);
        editor.commit();
        editor.apply();
    }

    public void setPref(String constant_key, String value){
        editor.putString(constant_key,value);
        editor.commit();
        editor.apply();
    }

    public void setPref(String constant_key, boolean value){
        editor.putBoolean(constant_key,value);
        editor.commit();
        editor.apply();
    }

    public void setUserModel(UserModel userModel){
        editor.putString(PrefConstant.USER_MODEL,gson.toJson(userModel));
        editor.commit();
        editor.apply();
    }

    public UserModel getUserModel(){
        UserModel userModel =  gson.fromJson(mPref.getString(PrefConstant.USER_MODEL,""),UserModel.class);
        if(userModel != null){
            return  userModel;
        }else{
            return new UserModel();
        }
    }

    public void setUpdateMaster(UpdateModel updateMaster) {
        String json = gson.toJson(updateMaster);
        editor.putString(PrefConstant.UPDATE_MASTER, json);
        editor.commit();
        editor.apply();
    }

    public UpdateModel getUpdateMaster() {
        String json = mPref.getString(PrefConstant.UPDATE_MASTER, "");
        UpdateModel updateModel = gson.fromJson(json, UpdateModel.class);
        return updateModel;
    }

    public void setHomeBanner(List<BannerModel> bannerModels) {
        Type banner = new TypeToken<List<BannerModel>>() {
        }.getType();
        String json = gson.toJson(bannerModels, banner);
        editor.putString(PrefConstant.HOME_BANNER, json);
        editor.commit();
        editor.apply();
    }

    public List<BannerModel> getHomeBanner() {
        String json = mPref.getString(PrefConstant.HOME_BANNER, "");
        Type banner = new TypeToken<List<BannerModel>>() {
        }.getType();
        List<BannerModel> bannerModels = gson.fromJson(json, banner);
        return bannerModels;
    }

    public void setStateList(List<StateModal> stateList) {
        Type state = new TypeToken<List<StateModal>>() {
        }.getType();
        String json = gson.toJson(stateList, state);
        editor.putString(PrefConstant.STATE_LIST, json);
        editor.commit();
        editor.apply();
    }

    public ArrayList<StateModal> getStateList() {
        String json = mPref.getString(PrefConstant.STATE_LIST, "");
        Type state = new TypeToken<List<StateModal>>() {
        }.getType();
        ArrayList<StateModal> stateModalList = gson.fromJson(json, state);
        return stateModalList;
    }

    public void setMatchModel(MatchModel matchModel) {
        editor.putString(PrefConstant.MATCH_MODEL, gson.toJson(matchModel));
        editor.commit();
        editor.apply();
    }

    public MatchModel getMatchModel() {
        MatchModel matchModel = gson.fromJson(mPref.getString(PrefConstant.MATCH_MODEL, ""), MatchModel.class);
        return matchModel;
    }

    public MatchModel getMatchData() {
        String json = mPref.getString(PrefConstant.MATCH_DATA,"");
        MatchModel matchList = gson.fromJson(json, MatchModel.class);
        return matchList;
    }

    public void setMatchData(MatchModel user) {
        String json = gson.toJson(user);
        editor.putString(PrefConstant.MATCH_DATA, json);
        editor.commit();
    }

    public void setPlayerModel(List<PlayerListModel> playerModel) {
        Type player = new TypeToken<List<PlayerListModel>>() {
        }.getType();
        String json = gson.toJson(playerModel, player);
        editor.putString(PrefConstant.PLAYER_MODEL, json);
        editor.commit();
        editor.apply();
    }

    public List<PlayerListModel> getPlayerModel() {
        String json = mPref.getString(PrefConstant.PLAYER_MODEL, "");
        Type player = new TypeToken<List<PlayerListModel>>() {
        }.getType();
        List<PlayerListModel> playerModels = gson.fromJson(json, player);
        return playerModels;
    }

    public void setOfferModel(List<OfferModel> offerModel) {
        Type player = new TypeToken<List<OfferModel>>() {
        }.getType();
        String json = gson.toJson(offerModel, player);
        editor.putString(PrefConstant.OFFER_MODEL, json);
        editor.commit();
        editor.apply();
    }

    public List<OfferModel> getOfferModel() {
        String json = mPref.getString(PrefConstant.OFFER_MODEL, "");
        Type offer = new TypeToken<List<OfferModel>>() {
        }.getType();
        List<OfferModel> offerModel = gson.fromJson(json, offer);
        return offerModel;
    }

    public void setFavList(List<ContestModel.ContestDatum> contestData) {
        Type player = new TypeToken<List<ContestModel.ContestDatum>>() {
        }.getType();
        String json = gson.toJson(contestData, player);
        editor.putString(PrefConstant.FAV_LIST, json);
        editor.commit();
        editor.apply();
    }

    public List<ContestModel.ContestDatum> getFavList() {
        String json = mPref.getString(PrefConstant.FAV_LIST, "");
        Type offer = new TypeToken<List<ContestModel.ContestDatum>>() {
        }.getType();
        return gson.fromJson(json, offer);
    }

    public void setMenu(List<MenuModel> menuModel){
        Type menu = new TypeToken<List<MenuModel>>(){}.getType();
        String json = gson.toJson(menuModel,menu);
        editor.putString(PrefConstant.MENU_MODEL,json);
        editor.commit();
        editor.apply();
    }

    public List<MenuModel> getMenu() {

        String json = mPref.getString(PrefConstant.MENU_MODEL, "");
        Type menu = new TypeToken<List<MenuModel>>() {
        }.getType();
        List<MenuModel> menus = gson.fromJson(json, menu);
        if (menus == null) {
            return new ArrayList<>();
        } else{
            return menus;
        }
    }

    public void setSports(List<SportsModel> sportsModels) {
        Type sport = new TypeToken<List<SportsModel>>() {
        }.getType();
        String json = gson.toJson(sportsModels, sport);
        editor.putString(PrefConstant.SPORTS_LIST, json);
        editor.commit();
        editor.apply();
    }

    public List<SportsModel> getSports() {
        String json = mPref.getString(PrefConstant.SPORTS_LIST, "");
        Type sport = new TypeToken<List<SportsModel>>() {
        }.getType();
        List<SportsModel> sports = gson.fromJson(json, sport);
        return sports;

    }

    public void setSportDetails(List<SportsDetailModel> sportsDetailModels) {
        Type sportDetail = new TypeToken<List<SportsModel>>() {
        }.getType();
        String json = gson.toJson(sportsDetailModels, sportDetail);
        editor.putString(PrefConstant.SPORTS_DETAIL_LIST, json);
        editor.commit();
        editor.apply();
    }

    public List<SportsDetailModel> getSportDetails() {
        String json = mPref.getString(PrefConstant.SPORTS_DETAIL_LIST, "");
        Type sportDetail = new TypeToken<List<SportsDetailModel>>() {
        }.getType();
        List<SportsDetailModel> sportDetails = gson.fromJson(json, sportDetail);
        return sportDetails;

    }

    public int getPrefInt(String key){

        int value = mPref.getInt(key,-1);
        return  value;

    }

    public String getPrefString(String key){

        String value = mPref.getString(key,"");
        return value;

    }

    public boolean getPrefBoolean(String key){

        boolean aBoolean = mPref.getBoolean(key, false);
        return aBoolean;
    }




    public void logout(){
        editor.clear();
        editor.commit();
        editor.apply();
    }

    public void setPlayerList(List<PlayerModel> playerList) {
        Type player = new TypeToken<List<PlayerModel>>() {
        }.getType();
        String json = gson.toJson(playerList, player);
        editor.putString(PrefConstant.PLAYER_MODEL_LIST, json);
        editor.commit();
        editor.apply();
    }

    public List<PlayerModel> getPlayerList() {
        String json = mPref.getString(PrefConstant.PLAYER_MODEL_LIST, "");
        Type player = new TypeToken<List<PlayerModel>>() {
        }.getType();
        List<PlayerModel> playerModels = gson.fromJson(json, player);
        return playerModels;
    }
    public boolean isNewUser() {
        return mPref.getBoolean("isNewUser", false);
    }
    public void setNewUser(boolean isNew) {
        editor.putBoolean(IS_NEW_USER, isNew);
        editor.apply();
    }

}
