package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.UserDetailTeamAdapter;
import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.Model.UserTeamModal;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InvesterDetailsActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    private ImageView toolbar_back;
    private CircleImageView imgUser;
    private RecyclerView recyclerMatch;
    private LinearLayout layNoData;
    private TextView toolbar_title,txtUserName,txtUserTeamName,txtTotalMatch,txtPoint,txtRankCnt;
    private UserLeaderboardList.Datum item;
    private String selectedSeriesTitle="",selectedSeriesId="";
    private List<UserTeamModal> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invester_details);
        item=(UserLeaderboardList.Datum)getIntent().getSerializableExtra("userModal") ;
        selectedSeriesId=getIntent().getStringExtra("selectedSeriesId") ;

        toolbar_back=findViewById(R.id.toolbar_back);
        toolbar_title=findViewById(R.id.toolbar_title);
        txtUserName=findViewById(R.id.txtUserName);
        txtUserTeamName=findViewById(R.id.txtUserTeamName);
        txtTotalMatch=findViewById(R.id.txtTotalMatch);
        txtRankCnt=findViewById(R.id.txtRankCnt);
        recyclerMatch=findViewById(R.id.recyclerMatch);
        layNoData=findViewById(R.id.layNoData);
        txtPoint=findViewById(R.id.txtPoint);
        imgUser=findViewById(R.id.imgUser);
        toolbar_title.setSelected(true);

        if (item!=null){
            txtUserName.setText(item.getDisplayName());
            txtUserTeamName.setText(item.getUserTeamName());
            if (TextUtils.isEmpty(item.getTotalRank())){
                txtRankCnt.setText("-");
            }else {
                txtRankCnt.setText("#"+item.getTotalRank());
            }
            if (TextUtils.isEmpty(item.getTotalPoint())){
                txtPoint.setText("-");
            }else {
                txtPoint.setText(item.getTotalPoint());
            }

            CustomUtil.loadImageWithGlide(mContext,imgUser, MyApp.imageBase + MyApp.user_img,item.getUserImg(),
                    R.drawable.ic_team1_placeholder);
        }

        toolbar_title.setText("Details");

        toolbar_back.setOnClickListener(v->{
            finish();
        });
        getData();
    }

    private void getData() {
        list=new ArrayList<>();
        try {
            JSONObject param=new JSONObject();
            param.put("lb_master_type_id",selectedSeriesId);
            param.put("user_id",item.getUserId());

            HttpRestClient.postJSON(mContext, true, ApiManager.investmentLeaderBoardDetailList, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("TAG", "onSuccessResult Detail: " + responseBody.toString() );
                    if (responseBody.optBoolean("status")){
                        JSONArray data=responseBody.optJSONArray("data");
                        if (data!=null && data.length()>0){
                            recyclerMatch.setVisibility(View.VISIBLE);
                            layNoData.setVisibility(View.GONE);
                            txtTotalMatch.setText(""+data.length());
                            for (int i=0;i<data.length();i++){
                                JSONObject object=data.optJSONObject(i);
                                UserTeamModal teamModal=gson.fromJson(object.toString(), UserTeamModal.class);
                                list.add(teamModal);
                            }
                            recyclerMatch.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
                            UserDetailTeamAdapter adapter=new UserDetailTeamAdapter(mContext,list, position -> {

                            },true);
                            recyclerMatch.setAdapter(adapter);
                        }else {
                            recyclerMatch.setVisibility(View.GONE);
                            layNoData.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recyclerMatch.setVisibility(View.GONE);
                        layNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}