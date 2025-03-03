package com.fantafeat.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.GameListActivity;
import com.fantafeat.Model.Games;
import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.util.List;

public class GameOfferAdapter extends RecyclerView.Adapter<GameOfferAdapter.OfferBannerHolder> {
    private Context mContext;
    private int lastHolder = -1;
    private List<Games> bannerModelList;
    private onGameClick onGameClick;

    public GameOfferAdapter(Context mContext,List<Games> bannerModelList, onGameClick onGameClick) {
        this.mContext = mContext;
        this.bannerModelList = bannerModelList;
        this.onGameClick = onGameClick;
    }

    @NonNull
    @Override
    public OfferBannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfferBannerHolder(LayoutInflater.from(mContext).inflate(R.layout.game_slider_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OfferBannerHolder holder, int position) {
        final Games bannerModel = bannerModelList.get(position);

        // LogUtil.e(TAG, "onBindViewHolder: " + gson.toJson(bannerModel));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((GameListActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthPixels = displayMetrics.widthPixels;
        float heightPixels = (float) (widthPixels * 0.85);
        holder.level_image.setMinimumHeight((int) heightPixels);

        CustomUtil.loadImageWithGlide(mContext,holder.level_image, ApiManager.BANNER_IMAGES,
                bannerModel.getImgHorizontal(),R.drawable.placeholder_slider_game);

        holder.level_image.setOnClickListener(view -> {
            onGameClick.onClick(bannerModel);
        });
    }

    public interface onGameClick{
        void onClick(Games game);
    }

    @Override
    public int getItemCount() {
        return bannerModelList.size();
    }


    public class OfferBannerHolder extends RecyclerView.ViewHolder {
        ImageView level_image;

        public OfferBannerHolder(@NonNull View itemView) {
            super(itemView);
            level_image = itemView.findViewById(R.id.game_slider_img);
        }
    }

}
