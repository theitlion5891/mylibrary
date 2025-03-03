package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.R;

import java.util.ArrayList;

public class OffefAdapter extends RecyclerView.Adapter<OffefAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NewOfferModal> jsonArray;

    public OffefAdapter(Context context, ArrayList<NewOfferModal> jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.offer_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NewOfferModal object=jsonArray.get(position);

        /*float entry= CustomUtil.convertFloat(object.getEntry_fee());
        float disEntry= CustomUtil.convertFloat(object.getDiscount_entry_fee());
        int bonus= CustomUtil.convertInt(object.getUsed_bonus());*/

        holder.txtTeam.setText("Team "+object.getTeam_no());

        //LogUtil.e("compa",object.getDiscount_entry_fee()+"   "+object.getTeam_no()+"  "+object.getUsed_bonus()+"  "+object.getEntry_fee());

        if (TextUtils.isEmpty(object.getDiscount_entry_fee())){
            holder.txtEntry.setText(context.getResources().getString(R.string.rs)+object.getEntry_fee());
            holder.txtEntry.setTextColor(context.getResources().getColor(R.color.black_pure));//
        }else if (object.getDiscount_entry_fee().equalsIgnoreCase("0")){
            holder.txtEntry.setText("FREE");
            holder.txtEntry.setTextColor(context.getResources().getColor(R.color.green_pure));
        } else {
            holder.txtEntry.setText(context.getResources().getString(R.string.rs)+object.getDiscount_entry_fee());
            holder.txtEntry.setTextColor(context.getResources().getColor(R.color.green_pure));
        }

        if (TextUtils.isEmpty(object.getUsed_bonus())){
            holder.txtBonus.setText("-");
        }else {
            if (!object.getUsed_bonus().equals("0")){
                holder.txtBonus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.txtBonus.setText(object.getUsed_bonus()+"%");
            }else {
                holder.txtBonus.setText("-");
                holder.txtBonus.setTextColor(context.getResources().getColor(R.color.black_pure));
            }


        }


//[{"team_no":"1","used_bonus":"0","discount_entry_fee":""},
// {"team_no":"2","used_bonus":"5","discount_entry_fee":""},
// {"team_no":"3","used_bonus":"","discount_entry_fee":"0"},
// {"team_no":"4","used_bonus":"10","discount_entry_fee":"39"},
// {"team_no":"5","used_bonus":"100","discount_entry_fee":""},
// {"team_no":"15","used_bonus":"","discount_entry_fee":"0"},
// {"team_no":"20","used_bonus":"","discount_entry_fee":"0"}]

       // LogUtil.e("cal",object.getDiscount_entry_fee()+"   "+object.getEntry_fee());

      /*  DecimalFormat decFormat = CustomUtil.getFormater("0.0");
        if (!TextUtils.isEmpty(object.getDiscount_entry_fee())){
            float discounted=(disEntry/100)*bonus;
            holder.txtBonusVal.setText(context.getResources().getString(R.string.rs)+decFormat.format(discounted));
        }else {
            float discounted=(entry/100)*bonus;
            holder.txtBonusVal.setText(context.getResources().getString(R.string.rs)+decFormat.format(discounted));
        }*/




    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTeam,txtBonus,txtEntry;//txtBonusVal,
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTeam=itemView.findViewById(R.id.txtTeam);
            txtBonus=itemView.findViewById(R.id.txtBonus);
           // txtBonusVal=itemView.findViewById(R.id.txtBonusVal);
            txtEntry=itemView.findViewById(R.id.txtEntry);
        }
    }
}
