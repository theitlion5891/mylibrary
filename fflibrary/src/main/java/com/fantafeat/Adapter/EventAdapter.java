package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<EventModel> list;
    private EventItemClick listener;
    private boolean isLiveEvent;
    private String selectedTag;

    public EventAdapter(Context context, ArrayList<EventModel> list, EventItemClick listener, boolean isLiveEvent, String selectedTag) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.isLiveEvent = isLiveEvent;
        this.selectedTag = selectedTag;
    }

    public void updateTag(String selectedTag,ArrayList<EventModel> list){
        this.isLiveEvent = isLiveEvent;
        this.selectedTag = selectedTag;
        this.list = list;
        notifyDataSetChanged();

    }
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_tradeboard_item,parent,false));
        }else{
            return new ItemTradeHolder(LayoutInflater.from(context).inflate(R.layout.event_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventModel model=list.get(position);

        if (holder.getItemViewType()==0){
            HeaderHolder holder2 = (HeaderHolder) holder;
            if (model.getConfirmTradeList()!=null){
                ConfirmLiveTradeAdapter tradeBoardAdapter = new ConfirmLiveTradeAdapter(context,model.getConfirmTradeList(),model1 -> {
                    listener.onItemClick(model1,"details");
                },isLiveEvent);
                holder2.recyclerTradeBoard.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                holder2.recyclerTradeBoard.setAdapter(tradeBoardAdapter);
            }

        }else{
            ItemTradeHolder holder1 = (ItemTradeHolder) holder;
            String rs=context.getResources().getString(R.string.rs);

            if (isLiveEvent){
                holder1.txtEndDate.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm a"));

                if (selectedTag.equalsIgnoreCase("1")){
                    holder1.txtLblMatched.setText(" Matched");
                    holder1.txtMatched.setText(model.getMy_total_confirm_trades()+" / "+model.getMy_total_trades_count());

                }
                else if (selectedTag.equalsIgnoreCase("3")){
                    holder1.txtLblMatched.setText(" Votes");
                    holder1.txtMatched.setText(model.getMy_total_trades_count());
                }else if (selectedTag.equalsIgnoreCase("2")){
                    holder1.txtLblMatched.setText(" Joined");
                    holder1.txtMatched.setText(model.getTotalTrades());
                }

                holder1.viewSaperator.setVisibility(View.GONE);
                if (TextUtils.isEmpty(model.getConSubDesc())){
                    holder1.layDesc.setVisibility(View.GONE);
                }else {
                    holder1.layDesc.setVisibility(View.VISIBLE);
                    holder1.txtDesc.setText(model.getConSubDesc());
                }
                holder1.txtLblReturn.setText("  Current Returns ");

                holder1.txtLblReturn.setTextColor(context.getResources().getColor(R.color.dark_blue));
                holder1.txtReturn.setTextColor(context.getResources().getColor(R.color.dark_blue));
                holder1.layWinning.setVisibility(View.GONE);

                holder1.txtLblReturn.setVisibility(View.GONE);
                holder1.txtReturn.setVisibility(View.GONE);

            }
            else {
                holder1.txtEndDate.setText(CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm a"));
                holder1.txtMatched.setText(model.getMy_total_trades_count());
                if (selectedTag.equalsIgnoreCase("1")){
                    holder1.txtLblMatched.setText(" Trades");

                }else if (selectedTag.equalsIgnoreCase("3")){
                    holder1.txtLblMatched.setText(" Votes");
                }else if (selectedTag.equalsIgnoreCase("2")){
                    holder1.txtLblMatched.setText(" Joined");
                }
                //holder.txtLblMatched.setText(" Trades");
                holder1.layDesc.setVisibility(View.GONE);
                holder1.viewSaperator.setVisibility(View.VISIBLE);

                if (CustomUtil.convertFloat(model.getWinAmount())>0){
                    holder1.layWinning.setVisibility(View.VISIBLE);
                    holder1.txtWinAmt.setText("Won "+rs+model.getWinAmount() );
                }else {
                    holder1.layWinning.setVisibility(View.GONE);
                }

                holder1.txtLblReturn.setVisibility(View.GONE);
                holder1.txtReturn.setVisibility(View.GONE);
            }

            holder1.txtInvst.setText(rs+ model.getInvestmentAmount() );
            holder1.txtReturn.setText(rs+model.getWinAmount() );

            holder1.txtConTitle.setText(model.getConDesc());

            if (!TextUtils.isEmpty(model.getConImage())){
                CustomUtil.loadImageWithGlide(context,holder1.imgConImage, MyApp.imageBase + MyApp.document,model.getConImage(),R.drawable.event_placeholder);
            }else
                holder1.imgConImage.setImageResource(R.drawable.event_placeholder);

            holder1.layMain.setOnClickListener(view -> {
                listener.onItemClick(model,"details");
            });
        }


    }

    public void updateTag(String selectedTag){
        this.selectedTag=selectedTag;
        notifyDataSetChanged();
    }

    public interface EventItemClick{
        public void onItemClick(EventModel model,String tag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class HeaderHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerTradeBoard;
        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            recyclerTradeBoard = itemView.findViewById(R.id.recyclerTradeBoard);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerTradeBoard);
        }
    }
     class ItemTradeHolder extends RecyclerView.ViewHolder{
        private TextView txtSell,txtInvst,txtReturn,txtEndDate,txtConTitle,txtDesc,txtMatched,txtLblMatched,txtLblReturn,txtWinAmt;
        private ImageView imgConImage;
        private View viewSaperator,view;
        private LinearLayout layDesc,layMain,layWinning;
        public ItemTradeHolder(@NonNull View itemView) {
            super(itemView);
            txtSell=itemView.findViewById(R.id.txtSell);
            txtInvst=itemView.findViewById(R.id.txtInvst);
            txtReturn=itemView.findViewById(R.id.txtReturn);
            txtEndDate=itemView.findViewById(R.id.txtEndDate);
            imgConImage=itemView.findViewById(R.id.imgConImage);
            txtConTitle=itemView.findViewById(R.id.txtConTitle);
            txtDesc=itemView.findViewById(R.id.txtDesc);
            layDesc=itemView.findViewById(R.id.layDesc);
            txtMatched=itemView.findViewById(R.id.txtMatched);
            layMain=itemView.findViewById(R.id.layMain);
            txtLblMatched=itemView.findViewById(R.id.txtLblMatched);
            txtLblReturn=itemView.findViewById(R.id.txtLblReturn);
            viewSaperator=itemView.findViewById(R.id.viewSaperator);
            view=itemView.findViewById(R.id.view);
            layWinning=itemView.findViewById(R.id.layWinning);
            txtWinAmt=itemView.findViewById(R.id.txtWinAmt);
        }
    }

}
