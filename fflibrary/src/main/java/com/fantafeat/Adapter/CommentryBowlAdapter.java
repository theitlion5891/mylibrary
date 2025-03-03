package com.fantafeat.Adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;

import java.util.List;

public class CommentryBowlAdapter extends RecyclerView.Adapter<CommentryBowlAdapter.ViewHolder>  {

    private Context context;
    private List<ScoreModel.Commentary.BallScore> list;
    private int overNo=0;
  //  private ObjectAnimator anim;

    public CommentryBowlAdapter(Context context, List<ScoreModel.Commentary.BallScore> list, int overNo) {
        this.context = context;
        this.list = list;
        this.overNo = overNo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.commentry_bowl_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ScoreModel.Commentary.BallScore model=list.get(position);

        holder.txtOverBowl.setText((overNo-1)+"."+model.getBall());

        holder.txtBowlRun.setText(model.getScore());

        if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white_pure));
        }
        else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.appBackGround));
        }
        /*if (model.getScore().equalsIgnoreCase("4")){
            anim.start();
        }else if (model.getScore().equalsIgnoreCase("6")){
            anim.start();
        } else if (model.getScore().equalsIgnoreCase("w")){
            anim.start();
        } else if (Integer.parseInt(model.getRun())>6){
            anim.start();
        }*/

        holder.layBowlRun.setBackgroundResource(ConstantUtil.getColorCode(model.getScore()));

        holder.txtOverName.setText(model.getCommentary());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtOverBowl,txtBowlRun,txtOverName;
        LinearLayout layBowlRun,linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOverBowl=itemView.findViewById(R.id.txtOverBowl);
            txtBowlRun=itemView.findViewById(R.id.txtBowlRun);
            txtOverName=itemView.findViewById(R.id.txtOverName);
            layBowlRun=itemView.findViewById(R.id.layBowlRun);
            linearLayout=itemView.findViewById(R.id.linearLayout);

          /*  anim = ObjectAnimator.ofPropertyValuesHolder(
                    txtBowlRun,
                    PropertyValuesHolder.ofFloat("scaleX", 1.8f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.8f));
            anim.setDuration(310);

            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.setRepeatMode(ObjectAnimator.REVERSE);*/

        }
    }
}
