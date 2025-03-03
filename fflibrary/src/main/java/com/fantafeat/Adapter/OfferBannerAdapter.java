package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.AddDepositActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.WebViewActivity;
import com.fantafeat.Fragment.FullImageFragment;
import com.fantafeat.Fragment.MatchViewModal;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.LogUtil;

import java.util.List;

public class OfferBannerAdapter extends RecyclerView.Adapter<OfferBannerAdapter.OfferBannerHolder> {

    private Context mContext;
    private int lastHolder = -1;
    private List<BannerModel> bannerModelList;
    private MatchViewModal viewModel;
    private MyPreferences preferences;
    private BannerListener listener;

    public OfferBannerAdapter(Context mContext, List<BannerModel> bannerModelList,BannerListener listener) {
        this.mContext = mContext;
        this.bannerModelList = bannerModelList;
        this.listener = listener;
    }

    public OfferBannerAdapter(Context mContext, MatchViewModal viewModel, MyPreferences preferences) {
        this.mContext = mContext;
        this.bannerModelList = preferences.getHomeBanner();
        /*      this.bannerModelList.get(0).setBannerAction("web_view");
        this.bannerModelList.get(0).setBannerWebViewUrl("https://www.fantafeat.com/");*/
        this.viewModel=viewModel;
        this.preferences=preferences;
    }

    @NonNull
    @Override
    public OfferBannerAdapter.OfferBannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfferBannerAdapter.OfferBannerHolder(LayoutInflater.from(mContext).inflate(R.layout.row_home_offer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OfferBannerAdapter.OfferBannerHolder holder, int position) {
        final BannerModel bannerModel = bannerModelList.get(position);

       // LogUtil.e(TAG, "onBindViewHolder: " + gson.toJson(bannerModel));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((HomeActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthPixels = displayMetrics.widthPixels;
        float heightPixels = (float) (widthPixels * 0.2);
        holder.level_image.setMinimumHeight((int) heightPixels);

        CustomUtil.loadImageWithGlide(mContext,holder.level_image,ApiManager.BANNER_IMAGES,bannerModel.getBannerImage(),R.drawable.placeholder_banner);
        LogUtil.e("TAG", "onBindViewHolder  dynamic_key 1: " + ApiManager.BANNER_IMAGES);
       /* Glide.with(mContext)
                .load(ApiManager.BANNER_IMAGES + bannerModel.getBannerImage())
                .placeholder(R.drawable.placeholder_banner)
                .error(R.drawable.placeholder_banner)
                .into(holder.level_image);*/

        holder.level_image.setOnClickListener(view -> {
            if (bannerModel.getBannerAction()!=null){
                listener.onClick(bannerModel);
            }
            // bannerModel.setBannerAction("Contest");
            // LogUtil.e("banAct",bannerModel.getBannerAction()+"   "+viewModel.getMatchModelList().size());
            /*if (bannerModel.getBannerAction().equalsIgnoreCase("deposit")) {
                Intent intent = new Intent(mContext, AddDepositActivity.class);
                intent.putExtra("isJoin",false);
                intent.putExtra("depositAmt",bannerModel.getBannerMatchId());
                mContext.startActivity(intent);
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("Contest")) {
                for (MatchModel matchModel : CustomUtil.emptyIfNull((viewModel.getMatchModelList()))) {
                    if (bannerModel.getBannerMatchId().equalsIgnoreCase(matchModel.getId())) {
                        preferences.setMatchModel(matchModel);
                        Intent intent = new Intent(mContext, ContestListActivity.class);
                        mContext.startActivity(intent);
                        break;
                    }
                }
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("web_view")) {
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,"")
                        .putExtra(ConstantUtil.WEB_URL,bannerModel.getBannerWebViewUrl()));
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("fullBanner")) {
                new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new FullImageFragment(ApiManager.BANNER_IMAGES + bannerModel.getBannerPopupImage()),
                        ((HomeActivity) mContext).fragmentTag(21),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("whatsapp")){
                if (MyApp.getClickStatus()){
                    String url = ApiManager.whatsapp_url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("twitter")){
                if (MyApp.getClickStatus()){
                    String url = ApiManager.tweet_url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("facebook")){
                if (MyApp.getClickStatus()){
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_id));
                        mContext.startActivity(intent);
                    } catch(Exception e) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_url)));
                    }
                }
            }
            else if (bannerModel.getBannerAction().equalsIgnoreCase("telegram")){
                if (MyApp.getClickStatus()){
                    String url = ApiManager.telegram_id;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            }*/
        });
    }

    @Override
    public int getItemCount() {
        return bannerModelList.size();
    }

    public interface BannerListener{
        public void onClick(BannerModel model);
    }

    public class OfferBannerHolder extends RecyclerView.ViewHolder {
        ImageView level_image;

        public OfferBannerHolder(@NonNull View itemView) {
            super(itemView);
            level_image = itemView.findViewById(R.id.offer_slider_img);
        }
    }
}
