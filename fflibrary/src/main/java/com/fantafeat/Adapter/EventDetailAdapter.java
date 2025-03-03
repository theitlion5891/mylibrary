package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.EventDetailModel;
import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EventDetailModel> list;
    private EventItemClick listener;
    private boolean isLiveEvent;

    public EventDetailAdapter(Context context, ArrayList<EventDetailModel> list, EventItemClick listener, boolean isLiveEvent) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.isLiveEvent = isLiveEvent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_detail_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventDetailModel model=list.get(position);

        String rs=context.getResources().getString(R.string.rs);

        holder.txtQty.setText(model.getQuantity());
        holder.txtJoinAmt.setText(rs+model.getJoin_fee());
        holder.txtOptionValue.setText(model.getOption_value());

        if (model.getOption_value().equalsIgnoreCase("yes")){
            holder.txtOptionValue.setBackgroundResource(R.drawable.opinio_yes);
        }else {
            holder.txtOptionValue.setBackgroundResource(R.drawable.opinio_no);
        }

        if (model.getIsExitedOrBuy().equalsIgnoreCase("exit")){
            holder.txtExitTag.setVisibility(View.VISIBLE);

            if (model.getTag().equalsIgnoreCase("1")){
                holder.txtLblText.setText("Investment");
                //holder.txtInvst.setText(rs+model.getSell_bal());
                float invst= CustomUtil.convertFloat(model.getJoin_fee())*CustomUtil.convertFloat(model.getQuantity());
                holder.txtInvst.setText(rs+invst+"");

                holder.txtWin.setVisibility(View.GONE);
                holder.txtLblWinText.setVisibility(View.GONE);
            }
            else if (model.getTag().equalsIgnoreCase("2")){
                holder.txtLblText.setText("Investment");
                float invst= CustomUtil.convertFloat(model.getJoin_fee())*CustomUtil.convertFloat(model.getQuantity());
                holder.txtInvst.setText(rs+invst+"");


                if (CustomUtil.convertFloat(model.getTotal_win_amount())>0){
                    holder.txtWin.setVisibility(View.VISIBLE);
                    holder.txtLblWinText.setVisibility(View.VISIBLE);
                    holder.txtWin.setText(rs+model.getTotal_win_amount());
                }else {
                    holder.txtLblWinText.setVisibility(View.GONE);
                    holder.txtWin.setVisibility(View.GONE);
                }

            }else {
                holder.txtWin.setVisibility(View.GONE);
                holder.txtLblWinText.setVisibility(View.GONE);
            }
        }
        else {
            holder.txtExitTag.setVisibility(View.GONE);

            holder.txtWin.setVisibility(View.GONE);
            holder.txtLblWinText.setVisibility(View.GONE);

            holder.txtLblText.setText("Investment");
            float invst= CustomUtil.convertFloat(model.getJoin_fee())* CustomUtil.convertFloat(model.getQuantity());
            holder.txtInvst.setText(rs+invst+"");

        }

        holder.layMain.setAlpha(1f);

        if (model.getTag().equalsIgnoreCase("1")){
            holder.txtSell.setVisibility(View.VISIBLE);
            holder.txtSell.setText("Cancel");
            if (model.getIsExitedOrBuy().equalsIgnoreCase("exit")){
                holder.layAvailExit.setVisibility(View.VISIBLE);
                holder.txtLnlAvail.setText("Exit Price");
                holder.txtAvailExit.setText(rs+model.getSell_bal());
            }else {
                holder.layAvailExit.setVisibility(View.INVISIBLE);
            }
        }
        else if (model.getTag().equalsIgnoreCase("2")){
            holder.txtSell.setVisibility(View.VISIBLE);
            holder.txtSell.setText("Exit");

            /*if (!model.isExitButton()){
                holder.txtSell.setVisibility(View.GONE);
            }*/

            if (model.getIsExitedOrBuy().equalsIgnoreCase("exit")){
                holder.layAvailExit.setVisibility(View.VISIBLE);
                holder.txtLnlAvail.setText("Exit Price");
                holder.txtAvailExit.setText(rs+model.getSell_bal());

                holder.txtSell.setVisibility(View.GONE);

            }else {
                if (isLiveEvent){

                    if (model.getAvailCnt().equalsIgnoreCase("0")){
                        holder.layAvailExit.setVisibility(View.INVISIBLE);
                        holder.txtSell.setVisibility(View.GONE);

                        holder.layMain.setAlpha(0.3f);

                    } else {
                        
                        holder.txtSell.setVisibility(View.VISIBLE);
                        holder.layAvailExit.setVisibility(View.VISIBLE);

                        holder.layAvailExit.setVisibility(View.VISIBLE);
                        holder.txtLnlAvail.setText("Avail to Exit");
                        holder.txtAvailExit.setText(model.getAvailCnt()+"");
                    }
                } else {

                    holder.layAvailExit.setVisibility(View.INVISIBLE);
                    holder.txtSell.setVisibility(View.GONE);

                    if (CustomUtil.convertFloat(model.getTotal_win_amount())>0){
                        holder.txtWin.setVisibility(View.VISIBLE);
                        holder.txtLblWinText.setVisibility(View.VISIBLE);
                        holder.txtWin.setText(rs+model.getTotal_win_amount());
                    }else {
                        holder.txtLblWinText.setVisibility(View.GONE);
                        holder.txtWin.setVisibility(View.GONE);
                    }
                    //holder.txtWin.setText(rs+model.getTotal_win_amount());

                }
            }
        }
        else {
            holder.txtSell.setVisibility(View.GONE);
            holder.layAvailExit.setVisibility(View.INVISIBLE);
        }

        holder.txtSell.setOnClickListener(view -> {
            listener.onItemClick(model);
        });

    }

    public interface EventItemClick{
        public void onItemClick(EventDetailModel model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItem(ArrayList<EventDetailModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtQty,txtJoinAmt,txtOptionValue,txtInvst,txtSell,txtExitTag,txtLblText,txtLblWinText,txtWin,txtAvailExit,txtLnlAvail;
        private LinearLayout layAvailExit,layMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQty=itemView.findViewById(R.id.txtQty);
            txtJoinAmt=itemView.findViewById(R.id.txtJoinAmt);
            txtOptionValue=itemView.findViewById(R.id.txtOptionValue);
            txtInvst=itemView.findViewById(R.id.txtInvst);
            txtSell=itemView.findViewById(R.id.txtSell);
            txtExitTag=itemView.findViewById(R.id.txtExitTag);
            txtLblText=itemView.findViewById(R.id.txtLblText);
            txtLblWinText=itemView.findViewById(R.id.txtLblWinText);
            txtWin=itemView.findViewById(R.id.txtWin);
            layAvailExit=itemView.findViewById(R.id.layAvailExit);
            txtAvailExit=itemView.findViewById(R.id.txtAvailExit);
            txtLnlAvail=itemView.findViewById(R.id.txtLnlAvail);
            layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
