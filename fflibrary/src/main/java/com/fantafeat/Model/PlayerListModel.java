package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlayerListModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    private String cteam_id;
    private String vcteam_id;
    @SerializedName("temp_team_name")
    @Expose
    private String tempTeamName;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("contest_display_type_id")
    @Expose
    private String contestDisplayTypeId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("total_point")
    @Expose
    private String totalPoint;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;

    private String other_text;

    private String other_text2;
    private float total_points = 0;

    public float getTotal_points() {
        return total_points;
    }

    public void setTotal_points(float total_points) {
        this.total_points = total_points;
    }

    private String vc_player_img;
    private String vc_player_sname;
    private String vc_player_name;
    private String vc_player_rank;
    private String vc_player_type;
    private String vc_player_xi;
    private String vc_player_avg_point;

    private String c_player_img;
    private String c_player_sname;
    private String c_player_name;
    private String c_player_rank;
    private String c_player_type;
    private String c_player_xi;
    private String c_player_avg_point;

    private String team1_count = "";
    private String team2_count = "";
    private String team1_sname;
    private String team2_sname;
    private String creditTotal;

    private String bat_count;
    private String wk_count;
    private String bowl_count;
    private String ar_count;
    private String cr_count;



    private String lineup_count;

    private String isSelected;
    private String isJoined;
    private boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private List<PlayerModel> playerDetailList;

    public String getCteam_id() {
        return cteam_id;
    }

    public void setCteam_id(String cteam_id) {
        this.cteam_id = cteam_id;
    }

    public String getVcteam_id() {
        return vcteam_id;
    }

    public void setVcteam_id(String vcteam_id) {
        this.vcteam_id = vcteam_id;
    }

    public String getOther_text() {
        return other_text;
    }

    public void setOther_text(String other_text) {
        this.other_text = other_text;
    }

    public String getOther_text2() {
        return other_text2;
    }

    public void setOther_text2(String other_text2) {
        this.other_text2 = other_text2;
    }

    public String getCTeam_id() {
        return cteam_id;
    }

    public void setCTeam_id(String team_id) {
        this.cteam_id = team_id;
    }
    public String getVCTeam_id() {
        return vcteam_id;
    }

    public void setVCTeam_id(String team_id) {
        this.vcteam_id = team_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempTeamName() {
        return tempTeamName;
    }

    public String getCr_count() {
        return cr_count;
    }

    public void setCr_count(String cr_count) {
        this.cr_count = cr_count;
    }

    public void setTempTeamName(String tempTeamName) {
        this.tempTeamName = tempTeamName;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getContestDisplayTypeId() {
        return contestDisplayTypeId;
    }

    public void setContestDisplayTypeId(String contestDisplayTypeId) {
        this.contestDisplayTypeId = contestDisplayTypeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
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

    public List<PlayerModel> getPlayerDetailList() {
        return playerDetailList;
    }

    public void setPlayerDetailList(List<PlayerModel> playerDetailList) {
        this.playerDetailList = playerDetailList;
    }

    public String getVc_player_img() {
        return vc_player_img;
    }

    public void setVc_player_img(String vc_player_img) {
        this.vc_player_img = vc_player_img;
    }

    public String getVc_player_sname() {
        return vc_player_sname;
    }

    public void setVc_player_sname(String vc_player_sname) {
        this.vc_player_sname = vc_player_sname;
    }

    public String getVc_player_name() {
        return vc_player_name;
    }

    public void setVc_player_name(String vc_player_name) {
        this.vc_player_name = vc_player_name;
    }

    public String getVc_player_rank() {
        return vc_player_rank;
    }

    public void setVc_player_rank(String vc_player_rank) {
        this.vc_player_rank = vc_player_rank;
    }

    public String getVc_player_type() {
        return vc_player_type;
    }

    public void setVc_player_type(String vc_player_type) {
        this.vc_player_type = vc_player_type;
    }

    public String getVc_player_xi() {
        return vc_player_xi;
    }

    public void setVc_player_xi(String vc_player_xi) {
        this.vc_player_xi = vc_player_xi;
    }

    public String getVc_player_avg_point() {
        return vc_player_avg_point;
    }

    public void setVc_player_avg_point(String vc_player_avg_point) {
        this.vc_player_avg_point = vc_player_avg_point;
    }

    public String getC_player_img() {
        return c_player_img;
    }

    public void setC_player_img(String c_player_img) {
        this.c_player_img = c_player_img;
    }

    public String getC_player_sname() {
        return c_player_sname;
    }

    public void setC_player_sname(String c_player_sname) {
        this.c_player_sname = c_player_sname;
    }

    public String getC_player_name() {
        return c_player_name;
    }

    public void setC_player_name(String c_player_name) {
        this.c_player_name = c_player_name;
    }

    public String getC_player_rank() {
        return c_player_rank;
    }

    public void setC_player_rank(String c_player_rank) {
        this.c_player_rank = c_player_rank;
    }

    public String getC_player_type() {
        return c_player_type;
    }

    public void setC_player_type(String c_player_type) {
        this.c_player_type = c_player_type;
    }

    public String getC_player_xi() {
        return c_player_xi;
    }

    public void setC_player_xi(String c_player_xi) {
        this.c_player_xi = c_player_xi;
    }

    public String getC_player_avg_point() {
        return c_player_avg_point;
    }

    public void setC_player_avg_point(String c_player_avg_point) {
        this.c_player_avg_point = c_player_avg_point;
    }

    public String getTeam1_count() {
        return team1_count;
    }

    public void setTeam1_count(String team1_count) {
        this.team1_count = team1_count;
    }

    public String getTeam2_count() {
        return team2_count;
    }

    public void setTeam2_count(String team2_count) {
        this.team2_count = team2_count;
    }

    public String getTeam1_sname() {
        return team1_sname;
    }

    public void setTeam1_sname(String team1_sname) {
        this.team1_sname = team1_sname;
    }

    public String getTeam2_sname() {
        return team2_sname;
    }

    public void setTeam2_sname(String team2_sname) {
        this.team2_sname = team2_sname;
    }

    public String getBat_count() {
        return bat_count;
    }

    public void setBat_count(String bat_count) {
        this.bat_count = bat_count;
    }

    public String getWk_count() {
        return wk_count;
    }

    public void setWk_count(String wk_count) {
        this.wk_count = wk_count;
    }

    public String getBowl_count() {
        return bowl_count;
    }

    public void setBowl_count(String bowl_count) {
        this.bowl_count = bowl_count;
    }

    public String getAr_count() {
        return ar_count;
    }

    public void setAr_count(String ar_count) {
        this.ar_count = ar_count;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(String isJoined) {
        this.isJoined = isJoined;
    }

    public String getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(String creditTotal) {
        this.creditTotal = creditTotal;
    }

    public String getLineup_count() {
        return lineup_count;
    }

    public void setLineup_count(String lineup_count) {
        this.lineup_count = lineup_count;
    }
}