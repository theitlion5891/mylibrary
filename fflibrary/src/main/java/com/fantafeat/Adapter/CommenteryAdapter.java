package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommenteryAdapter extends RecyclerView.Adapter<CommenteryAdapter.ViewHolder> {


    private Context context;
    private List<ScoreModel.Commentary> list;
    private String tag="";

    public CommenteryAdapter(Context context, List<ScoreModel.Commentary> list, String tag) {
        this.context = context;
        this.list = list;
        this.tag = tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.commentary_header_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ScoreModel.Commentary model=list.get(position);

        holder.txtOverDesc.setText(model.getCommentary());

        if (tag.equalsIgnoreCase("commentary")){
            if (TextUtils.isEmpty(model.getCommentary())){
                holder.txtOverDesc.setVisibility(View.GONE);
            }else {
                holder.txtOverDesc.setVisibility(View.VISIBLE);
            }
        }else {
            holder.txtOverDesc.setVisibility(View.GONE);
        }
        holder.ballScores.clear();
        holder.ballScores.addAll(model.ballScore);

        Collections.reverse(holder.ballScores);

        LinearLayoutManager manager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.recyclerBowl.setLayoutManager(manager);
        CommentryBowlAdapter adapter=new CommentryBowlAdapter(context,holder.ballScores,model.getOvNo());
        holder.recyclerBowl.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtOverDesc;
        private RecyclerView recyclerBowl;
        List<ScoreModel.Commentary.BallScore> ballScores=new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOverDesc=itemView.findViewById(R.id.txtOverDesc);
            recyclerBowl=itemView.findViewById(R.id.recyclerBowl);
        }
    }
}
