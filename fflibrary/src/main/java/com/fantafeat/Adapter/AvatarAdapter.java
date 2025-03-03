package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.GameListActivity;
import com.fantafeat.Model.AvatarModal;
import com.fantafeat.R;

import java.util.ArrayList;


public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AvatarModal> list;

    public AvatarAdapter(Context context, ArrayList<AvatarModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.avatar_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvatarModal modal=list.get(position);

        holder.imgAvatar.setImageResource(modal.getAvatar());

        if (modal.isCheck()){
            holder.layCheck.setVisibility(View.VISIBLE);
        }else {
            holder.layCheck.setVisibility(View.GONE);
        }

        holder.imgAvatar.setOnClickListener(v->{
            /*if (context != null){
                ((GameListActivity)context).clickEffect();
            }*/
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setCheck(false);
            }
            list.get(position).setCheck(true);

            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layCheck;
        private ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar=itemView.findViewById(R.id.imgAvatar);
            layCheck=itemView.findViewById(R.id.layCheck);
        }
    }
}
