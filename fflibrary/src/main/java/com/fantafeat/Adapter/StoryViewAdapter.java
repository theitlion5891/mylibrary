package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.StatusUserListModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;


public class StoryViewAdapter extends RecyclerView.Adapter<StoryViewAdapter.StoryViewHolder> {

    ArrayList<StatusUserListModel> usernameList;
    Context context;
    private StatusListener listener;
    public StoryViewAdapter(ArrayList<StatusUserListModel> usernameList, Context context, StatusListener listener) {
        this.usernameList = usernameList;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.story_view_item,parent,false);
        return new StoryViewHolder(view);
    }

    public interface StatusListener{
        public void onClick(ArrayList<StatusUserListModel> list,int position);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        //holder.username.setText(usernameList.get(position).getBannerAction());
        StatusUserListModel model=usernameList.get(position);
        CustomUtil.loadImageWithGlide(context,holder.imgStory, MyApp.imageBase+MyApp.user_img, model.getUserImg(),
                R.drawable.placeholder_banner);

        if (model.getReadCount().equalsIgnoreCase(model.getTotalCount())){
            holder.relativeLayout.setBackgroundResource(R.drawable.gray_bg_light_curve);
        }else {
            holder.relativeLayout.setBackgroundResource(R.drawable.status_bg_gradiant);
        }

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(usernameList,holder.getPosition());
              /*  Intent intent = new Intent(view.getContext(), StoryPlayerActivity.class);
                intent.putExtra("statusPosition",position);
                intent.putExtra("statusList",usernameList);
                view.getContext().startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return usernameList.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder
    {
       // TextView username;
        ImageView imgStory;
        FrameLayout frameLayout;
        RelativeLayout relativeLayout;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
           // username = itemView.findViewById(R.id.username);
            frameLayout = itemView.findViewById(R.id.frameLayout);
            imgStory = itemView.findViewById(R.id.imgStory);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

}
