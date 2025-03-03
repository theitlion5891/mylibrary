package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.fantafeat.Model.OpinionFilterTags;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

public class EventFilterAdapter extends RecyclerView.Adapter<EventFilterAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OpinionFilterTags> list;
    private FilterListener listener;

    public EventFilterAdapter(Context context, ArrayList<OpinionFilterTags> list, FilterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.opinio_filter_chip_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpinionFilterTags model=list.get(position);

        if (model.isSelected()){
            //holder.viewSelected.setVisibility(View.VISIBLE);
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.blackPrimary));
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.yellow_dark));
            //holder.txtSubTitle.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            // holder.viewSelected.setVisibility(View.GONE);
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.lighetest_gray));
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.black_pure));
            //holder.txtSubTitle.setTextColor(context.getResources().getColor(R.color.black));
        }

       // holder.txtSubTitle.setText(model.getListOpinion().size()+" Events");
        holder.txtTitle.setText(model.getTag_title()+" ("+model.getListOpinion().size()+")");
       /* if (!TextUtils.isEmpty(model.getTag_img())){
            CustomUtil.loadImageWithGlide(context,holder.imgStart, MyApp.imageBase + MyApp.document,model.getTag_img(),R.drawable.ic_lamp);
        }*/

        if (model.getOther_data()!=null && !TextUtils.isEmpty(model.getOther_data().getMatchStatus()) &&
                model.getOther_data().getMatchStatus().equalsIgnoreCase("Playing")){
            //holder.imgStart.setImageResource(R.drawable.match_live);
            holder.imgStart.setVisibility(View.VISIBLE);
            /*Glide.with(context)
                    .load(R.raw.live_match)
                    .into(holder.imgStart);*/
        }else {
            holder.imgStart.setVisibility(View.GONE);
        }

        holder.cardMain.setOnClickListener(view -> {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelected(false);
            }
            model.setSelected(true);
            listener.onFilterClick(model,position);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface FilterListener{
        public void onFilterClick(OpinionFilterTags model,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgStart;
        private TextView txtTitle/*,txtSubTitle*/;
        private CardView cardMain;
        // private View viewSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //txtSubTitle=itemView.findViewById(R.id.txtSubTitle);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtTitle.setSelected(true);
            //txtSubTitle.setSelected(true);
            imgStart=itemView.findViewById(R.id.imgStart);
            cardMain=itemView.findViewById(R.id.cardMain);
            // viewSelected=itemView.findViewById(R.id.viewSelected);

        }
    }
}
