package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Activity.OpinionDetailsActivity;
import com.fantafeat.Activity.TOPActivity;
import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.WinningTreeSheet;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.ViewHolder>  {

    private ArrayList<EventModel> list;
    private Context context;
    private OpinionItemClick listener;

    public OpinionAdapter(ArrayList<EventModel> list, Context context, OpinionItemClick listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.opinion_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel model=list.get(position);

        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date date1 = input.parse(model.getConStartTime());
            Date date2 = new Date();

            long different = date2.getTime() - date1.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;

            long elapsedHours = different / hoursInMilli;

            if (elapsedHours<=1){
                holder.txtNewBadge.setVisibility(View.VISIBLE);
                model.setNewBadge(true);
            }else {
                holder.txtNewBadge.setVisibility(View.GONE);
                model.setNewBadge(false);
            }
            // obj.printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtConEnd.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));
        holder.txtConTitle.setText(model.getConDesc());
        holder.txtTrades.setText(model.getTotalTrades()+" Joined");

        String rs=context.getResources().getString(R.string.rs);

        holder.txtGiveOpinion.setText("Entry : "+rs+model.getEntryFee());

        int totalSpots = CustomUtil.convertInt(model.getNo_of_spot());
        int jointeam = CustomUtil.convertInt(model.getTotalTrades());

        holder.contest_total_team.setText(model.getNo_of_spot());
        int left=totalSpots - jointeam;
        holder.contest_left_team.setText(left+"");

        int progress = (jointeam * 100) / totalSpots;
        if (progress==0 && jointeam>0)
            progress+=1;

        holder.contest_progress.setProgress(progress);

        int totalWin=Integer.parseInt(model.getNo_of_winners())*100/totalSpots;

        holder.txtWinPer.setText(totalWin+"%");
        holder.txtUpto.setText("Up to "+model.getJoin_spot_limit());

        holder.contest_price_pool.setText(CustomUtil.displayAmountFloat( context, model.getDistributeAmount()));

        try{
            JSONArray winningTree=new JSONArray(model.getWinningTree());
            if (winningTree.length()>0)
                holder.txtFirstPrice.setText(rs+winningTree.optJSONObject(0).optString("amount"));

        }catch (Exception e){
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(model.getConImage())){
            CustomUtil.loadImageWithGlide(context,holder.imgConImage, MyApp.imageBase + MyApp.document,model.getConImage(),R.drawable.event_placeholder);
        }else
            holder.imgConImage.setImageResource(R.drawable.event_placeholder);

        holder.txtFirstPrice.setOnClickListener(view -> {
            WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(context, model.getDistributeAmount(), model.getWinningTree()
                    ,holder.contest_price_pool.getText().toString());
            bottomSheetTeam.show(((TOPActivity) context).getSupportFragmentManager(),
                    BottomSheetTeam.TAG);
        });

        holder.txtGiveOpinion.setOnClickListener(view -> {
            listener.onItemClick(model);
        });

        holder.layMain.setOnClickListener(view -> {
            context.startActivity(new Intent(context, OpinionDetailsActivity.class)
                    .putExtra("eventModel", model)
                    .putExtra("isLiveEvent", true)
            );
        });

    }

    public void updateData(ArrayList<EventModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OpinionItemClick{
        public void onItemClick(EventModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtConEnd,txtConTitle,txtGiveOpinion/*,txtOp1,txtOp2,txtOp3,txtOp4,txtOp5,txtSubmitTime,txtSubmitInteger*/,
                txtTrades,txtNewBadge,txtFirstPrice,contest_left_team,contest_total_team,txtWinPer,txtUpto,contest_price_pool;
        private ImageView imgConImage;
        private ProgressBar contest_progress;
        private LinearLayout layMain;
        //private EditText edtTime,edtInteger;
       // private LinearLayout layOption,layTime,layInteger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtConEnd=itemView.findViewById(R.id.txtConEnd);
            imgConImage=itemView.findViewById(R.id.imgConImage);
            txtConTitle=itemView.findViewById(R.id.txtConTitle);
            txtGiveOpinion=itemView.findViewById(R.id.txtGiveOpinion);
            contest_progress=itemView.findViewById(R.id.contest_progress);
            contest_left_team=itemView.findViewById(R.id.contest_left_team);
            contest_total_team=itemView.findViewById(R.id.contest_total_team);
            txtWinPer=itemView.findViewById(R.id.txtWinPer);
            txtUpto=itemView.findViewById(R.id.txtUpto);
            contest_price_pool=itemView.findViewById(R.id.contest_price_pool);
            layMain=itemView.findViewById(R.id.layMain);
           /* txtOp1=itemView.findViewById(R.id.txtOp1);
            txtOp2=itemView.findViewById(R.id.txtOp2);
            txtOp3=itemView.findViewById(R.id.txtOp3);
            txtOp4=itemView.findViewById(R.id.txtOp4);
            txtOp5=itemView.findViewById(R.id.txtOp5);
            layInteger=itemView.findViewById(R.id.layInteger);
            layOption=itemView.findViewById(R.id.layOption);
            layTime=itemView.findViewById(R.id.layTime);
            edtTime=itemView.findViewById(R.id.edtTime);
            edtInteger=itemView.findViewById(R.id.edtInteger);
            txtSubmitTime=itemView.findViewById(R.id.txtSubmitTime);
            txtSubmitInteger=itemView.findViewById(R.id.txtSubmitInteger);*/
            txtTrades=itemView.findViewById(R.id.txtTrades);
            txtNewBadge=itemView.findViewById(R.id.txtNewBadge);
            txtFirstPrice=itemView.findViewById(R.id.txtFirstPrice);

        }
    }

}
