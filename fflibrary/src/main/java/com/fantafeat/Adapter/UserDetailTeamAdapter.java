package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.UserTeamModal;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.itextpdf.text.pdf.parser.Line;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailTeamAdapter extends RecyclerView.Adapter<UserDetailTeamAdapter.ViewHolder> {

    private Context context;
    private List<UserTeamModal> list;
    private onItemClick onItemClick;
    boolean isInv=false;
    public UserDetailTeamAdapter(Context context, List<UserTeamModal> list, onItemClick onItemClick, boolean isInv) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;
        this.isInv = isInv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_team_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserTeamModal item=list.get(position);

        holder.txtDate.setText(CustomUtil.dateTimeConvert(item.getSafeMatchStartTime()));
        holder.txtPoint.setText(item.getTotalPoint()+"("+item.getTempTeamName()+")");
        holder.txtTitle.setText(item.getMatchTitle());
        holder.txtRank.setText(item.getTotalRank());
        holder.txtTeam1Name.setText(item.getTeam1Sname());
        holder.txtTeam2Name.setText(item.getTeam2Sname());

        CustomUtil.loadImageWithGlide(context,holder.imgTeam1,ApiManager.TEAM_IMG,item.getTeam1Img(),
                R.drawable.ic_team1_placeholder);

        CustomUtil.loadImageWithGlide(context,holder.imgTeam2,ApiManager.TEAM_IMG,item.getTeam2Img(),
                R.drawable.ic_team1_placeholder);

        if (isInv){
            holder.txtLblPoint.setText("Investment");
            holder.txtPoint.setText(item.getTotalPoint());
            holder.layRank.setVisibility(View.GONE);
        }

        /*if ((position%2)==0){
            //even
            holder.layMain.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            //odd
            holder.layMain.setBackgroundColor(context.getResources().getColor(R.color.appBackground));
        }*/

        holder.layMain.setOnClickListener(v->{
            if (MyApp.getClickStatus()){
                onItemClick.onClick(position);
            }
        });

    }

    public interface  onItemClick{
        public void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle,txtPoint,txtDate,txtRank,txtTeam2Name,txtTeam1Name,txtLblPoint;
        CardView layMain;
        LinearLayout layRank;
        CircleImageView imgTeam1,imgTeam2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtTitle.setSelected(true);
            txtPoint=itemView.findViewById(R.id.txtPoint);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtRank=itemView.findViewById(R.id.txtRank);
            txtTeam2Name=itemView.findViewById(R.id.txtTeam2Name);
            txtTeam1Name=itemView.findViewById(R.id.txtTeam1Name);
            imgTeam1=itemView.findViewById(R.id.imgTeam1);
            imgTeam2=itemView.findViewById(R.id.imgTeam2);
            layMain=itemView.findViewById(R.id.layMain);
            txtLblPoint=itemView.findViewById(R.id.txtLblPoint);
            layRank=itemView.findViewById(R.id.layRank);
        }
    }
}
