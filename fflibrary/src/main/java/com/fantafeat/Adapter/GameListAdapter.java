package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.GameMainModal;
import com.fantafeat.Model.Games;
import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder>{

    private Context context;
    private List<Games> list;
    private onGameClick onGameClick;

    public GameListAdapter(Context context, List<Games> list, onGameClick onGameClick) {
        this.context = context;
        this.list = list;
        this.onGameClick = onGameClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.game_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Games game=list.get(position);

        CustomUtil.loadImageWithGlide(context,holder.imgGame, ApiManager.BANNER_IMAGES,
                game.getImgVertical(),R.drawable.big_game_placeholder);

        holder.imgGame.setOnClickListener(v -> {
            onGameClick.onClick(game);
        });
    }

    public interface onGameClick{
        void onClick(Games game);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgGame;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgGame=itemView.findViewById(R.id.imgGame);
        }
    }
}
