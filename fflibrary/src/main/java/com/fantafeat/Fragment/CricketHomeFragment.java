package com.fantafeat.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Adapter.MatchFullListAdapter;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.StatusUserListModel;
import com.fantafeat.Model.UpdateModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class CricketHomeFragment extends BaseFragment {

    private RecyclerView cricket_list;
    // private LinearLayout layNoData;
    // private MatchListAdapter matchListAdapter;
    List<MatchModel> matchModelList;
    private ArrayList<MatchModel> matchModelListSafe;
    private ArrayList<MatchModel> tournamentMatchList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MatchFullListAdapter adapter1;
    private String sport_id = ""/*, linkData = ""*/;

    private ArrayList<EventModel> list;
    private boolean isBanner = false;
    private UpdateModel updateModel;


    public CricketHomeFragment() {
    }

   /* public CricketHomeFragment(Fragment fragment) {
       // this.sport_id = id;
       // this.fragment=fragment;
    }*/

    public static CricketHomeFragment newInstance(String id) {//, Fragment fragment
        CricketHomeFragment myFragment = new CricketHomeFragment();//fragment
        Bundle args = new Bundle();
        args.putString("id", id);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sport_id = getArguments().getString("id");
            //linkData = getArguments().getString("linkData");
        }

        //viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(MatchViewModal.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cricket_home, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isBg(false);

        //LogUtil.e("resp","Call");
    }

    @Override
    public void initControl(View view) {



        matchModelList = new ArrayList<>();
        matchModelListSafe = new ArrayList<>();
        tournamentMatchList = new ArrayList<>();

        cricket_list = view.findViewById(R.id.cricket_list);
        //layNoData = view.findViewById(R.id.layNoData);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_cricket);

        updateModel = preferences.getUpdateMaster();
//        scroll = view.findViewById(R.id.scroll);

        // mHomeOffer = view.findViewById(R.id.home_offer);

       /* scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                switch ( event.getAction( ) ) {
                    case MotionEvent.ACTION_SCROLL:
                    case MotionEvent.ACTION_MOVE:
                        Log.e( "SCROLL", "ACTION_SCROLL" );
                       // ((HomeActivityFragment)fragment).enterAnim();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.e( "SCROLL", "ACTION_DOWN" );
                        //((HomeActivityFragment)fragment).enterAnim();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        Log.e( "SCROLL", "SCROLL_STOP" );
                       // ((HomeActivityFragment)fragment).leaveAnim();
                        break;
                }
                return false;
            }
        });*/

        adapter1 = new MatchFullListAdapter(mContext, matchModelList, new MatchFullListAdapter.MatchListener() {
            @Override
            public void onStatusClick(ArrayList<StatusUserListModel> list, int position) {

            }

            @Override
            public void onBannerClick(BannerModel bannerModel) {
                //bannerAction(bannerModel);
            }

            @Override
            public void onMatchClick(MatchModel matchModel) {

            }

            @Override
            public void onTradeClick(EventModel eventModel) {
            }
        });
        cricket_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        cricket_list.setHasFixedSize(true);
        cricket_list.setAdapter(adapter1);
        ViewCompat.setNestedScrollingEnabled(cricket_list, true);

        getData();

    }

    private void getData() {
        // matchModelList.clear();
        matchModelListSafe = new ArrayList<>();
        tournamentMatchList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        try {
            // Log.e(TAG, "onSuccessResult: "+ preferences.getUserModel().getId()+"   "+sport_id);

            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("sports_id", sport_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean show_dialog = true;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            show_dialog = false;
        }
        HttpRestClient.postJSON(mContext, show_dialog, ApiManager.MATCH_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                matchModelList.clear();
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (responseBody.optBoolean("status")) {


                    JSONArray array = responseBody.optJSONArray("data");

                    if (array != null && array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject data = array.getJSONObject(i);
                                //LogUtil.e(TAG, "onSuccessResult: "+ data);
                                MatchModel matchModel = gson.fromJson(data.toString(), MatchModel.class);
                                matchModel.setMatchDisplayType(5);
                                if (!TextUtils.isEmpty(updateModel.getSeries_id_banner()) &&
                                        matchModel.getLeagueId().equalsIgnoreCase(updateModel.getSeries_id_banner())) {
                                    if (matchModel.getDisplayIsSafe().equalsIgnoreCase("Yes")) {
                                        tournamentMatchList.add(matchModel);
                                    }
                                } else {
                                    if (matchModel.getDisplayIsSafe().equalsIgnoreCase("Yes")) {
                                        matchModel.setMatchDisplayType(7);
                                        matchModelListSafe.add(matchModel);
                                        //mainMatchList.add(matchModel);
                                    }
                                }
                                // matchModelList.add(matchModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter1.notifyDataSetChanged();
                    }

                }

                setData();
                //checkData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                setData();
            }

        });
    }

    private void setData() {

        if (tournamentMatchList.size() > 0) {
            MatchModel matchModel = new MatchModel();
            matchModel.setMatchDisplayType(1);
            matchModel.setMatchTitle(updateModel.getSeries_title_banner());
            matchModel.setOtherMatchList(tournamentMatchList);
            matchModelList.add(matchModel);//(pos+1),
        }

        if (matchModelListSafe.size() > 0) {
            MatchModel matchModel1 = new MatchModel();
            matchModel1.setMatchDisplayType(2);
            matchModel1.setMatchTitle("Upcoming Matches");
            matchModelList.add(matchModel1);
            matchModelList.addAll(matchModelListSafe);
        }
        else {
            MatchModel matchModel1 = new MatchModel();
            matchModel1.setMatchDisplayType(3);
            matchModelList.add(matchModel1);
        }
        adapter1.notifyDataSetChanged();

    }

    public void isBg(boolean show) {
       // ((HomeActivityFragment) fragment).isShowBg(show);
    }




    @Override
    public void initClick() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
           /* matchModelList.clear();
            cricket_list.getRecycledViewPool().clear();
            adapter1.notifyDataSetChanged();*/

            /*if (!TextUtils.isEmpty(updateModel.getIs_story_avaialble()) && updateModel.getIs_story_avaialble().equalsIgnoreCase("yes") &&
                    !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
                getStatusUserList(true);
            else if (!TextUtils.isEmpty(updateModel.getIs_trade_available()) && updateModel.getIs_trade_available().equalsIgnoreCase("yes") &&
                    !BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE))
                getTradingData();
            else*/
                getData();
            /*if ((System.currentTimeMillis()-lastUpdateTime)>=ConstantUtil.Refresh_delay) {

            }else {
                swipeRefreshLayout.setRefreshing(false);
            }*/
        });
    }


}