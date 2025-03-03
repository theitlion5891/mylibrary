package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class PollDetailAdapter extends RecyclerView.Adapter<PollDetailAdapter.ViewHolder> {

    private Context context;
    private JSONArray list;

    public PollDetailAdapter(Context context, JSONArray list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.poll_entry_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       try{
           JSONObject object=list.optJSONObject(position);
           String rs=context.getResources().getString(R.string.rs);
           holder.txtInvst.setText(rs+object.optString("investment_amount"));
           if (CustomUtil.convertFloat(object.optString("win_amount"))>0){
               holder.txtWinAmt.setText("Winning "+rs+object.optString("win_amount"));
               holder.layWinning.setVisibility(View.VISIBLE);
           }else {
               holder.layWinning.setVisibility(View.GONE);
           }
           holder.txtTrades.setText(object.optString("my_total_trades_count")+" Votes");
           holder.txtWinOption.setText(object.optString("option_value"));

       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @Override
    public int getItemCount() {
        return list.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTrades,txtWinOption,txtInvst,txtWinAmt;
        private LinearLayout layWinning;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTrades=itemView.findViewById(R.id.txtTrades);
            txtWinOption=itemView.findViewById(R.id.txtWinOption);
            txtWinOption.setSelected(true);
            txtInvst=itemView.findViewById(R.id.txtInvst);
            txtWinAmt=itemView.findViewById(R.id.txtWinAmt);
            txtWinAmt.setSelected(true);
            layWinning=itemView.findViewById(R.id.layWinning);
        }
    }

}
