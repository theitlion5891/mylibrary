package com.fantafeat.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.WinningTransferOfferModel;
import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

public class WinningTransferOfferAdapter extends RecyclerView.Adapter<WinningTransferOfferAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WinningTransferOfferModel> list;

    public WinningTransferOfferAdapter(Context context, ArrayList<WinningTransferOfferModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.winning_transfer_offer_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WinningTransferOfferModel model=list.get(position);

        holder.txtAmount.setText("Transfer "+ CustomUtil.displayAmountFloat(context,model.getStartAmt())+" - "+
                CustomUtil.displayAmountFloat(context,model.getEndAmt()) + " and get "+model.getCreditPercentage().replaceAll(".00","")+
                "% Extra deposit cash.");

        if (model.isSelected()){
            holder.layMain.setBackgroundResource(R.drawable.primary_round_border);
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.darkRedTrans));
            holder.txtAmount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.layMain.setBackgroundResource(R.drawable.gray_border_dark_with_radius);
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.txtAmount.setTextColor(context.getResources().getColor(R.color.gray_444444));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtAmount;
        private CardView cardMain;
        private LinearLayout layMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            cardMain=itemView.findViewById(R.id.cardMain);
            layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
