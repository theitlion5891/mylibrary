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
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;

import java.util.ArrayList;
import java.util.List;

public class ScoreCardAdapter extends RecyclerView.Adapter<ScoreCardAdapter.ViewHolder>{

    private Context context;
    private List<ScoreModel> list;
    private boolean isFirstTime=true;
    private MatchModel matchModel;
    private int team1=1;
    private int team2=1;

    public ScoreCardAdapter(Context context, List<ScoreModel> list,MatchModel matchModel) {
        this.context = context;
        this.list = list;
        this.matchModel = matchModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.scorecard_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ScoreModel scoreModel=list.get(position);

        if (position==list.size()-1 && isFirstTime){
            scoreModel.setShow(true);
        }

        if (matchModel.getMatchType().equalsIgnoreCase("test")){
            if (team1==1){
                team1+=1;
                if (!TextUtils.isEmpty(matchModel.getTeam_1_api_id()) &&
                        matchModel.getTeam_1_api_id().equalsIgnoreCase(scoreModel.getBattingTeamId())){
                    holder.txtTeamName.setText(matchModel.getTeam1Sname()+" Inning 1");
                }
            }else if (team2==1){
                team2+=1;
                if (!TextUtils.isEmpty(matchModel.getTeam_2_api_id()) &&
                        matchModel.getTeam_2_api_id().equalsIgnoreCase(scoreModel.getBattingTeamId())){
                    holder.txtTeamName.setText(matchModel.getTeam2Sname()+" Inning 1");
                }
            }else if (team1==2){
                team1+=1;
                if (!TextUtils.isEmpty(matchModel.getTeam_1_api_id()) &&
                        matchModel.getTeam_1_api_id().equalsIgnoreCase(scoreModel.getBattingTeamId())){
                    holder.txtTeamName.setText(matchModel.getTeam1Sname()+" Inning 2");
                }
            }else if (team2==2){
                team2+=1;
                if (!TextUtils.isEmpty(matchModel.getTeam_2_api_id()) &&
                        matchModel.getTeam_2_api_id().equalsIgnoreCase(scoreModel.getBattingTeamId())){
                    holder.txtTeamName.setText(matchModel.getTeam2Sname()+" Inning 2");
                }
            }
        }
        else {
            if (!TextUtils.isEmpty(matchModel.getTeam_1_api_id()) &&
                    matchModel.getTeam_1_api_id().equalsIgnoreCase(scoreModel.getBattingTeamId())){
                holder.txtTeamName.setText(matchModel.getTeam1Sname());
            }

            if (!TextUtils.isEmpty(matchModel.getTeam_2_api_id()) &&
                    matchModel.getTeam_2_api_id().equalsIgnoreCase(scoreModel.getBattingTeamId())){
                holder.txtTeamName.setText(matchModel.getTeam2Sname());
            }
        }

        //holder.txtTeamName.setText(scoreModel.getShortName());
        holder.txtTeamScore.setText(scoreModel.getScoresFull());

       /* int wickets=0,overs=0;
        for (int i = 0; i <scoreModel.getBowlers().size(); i++) {
            ScoreModel.Bowler bowler=scoreModel.getBowlers().get(i);
            wickets += Integer.parseInt(bowler.getWickets());
            overs += Integer.parseInt(bowler.getOvers());
        }
*/

        holder.didnotBatNames="";

        for (int i = 0; i < scoreModel.getDidNotBat().size(); i++) {
            ScoreModel.DidNotBat batsman=scoreModel.getDidNotBat().get(i);
            holder.didnotBatNames = holder.didnotBatNames+batsman.getName()+",";
        }

        String extra="";

        if (scoreModel.getExtraRuns().getPenalty().equals("")) {
            extra="(nb "+scoreModel.getExtraRuns().getNoballs()+", wd "+scoreModel.getExtraRuns().getWides()+", b "+scoreModel.getExtraRuns().getByes()+
                    ", lb "+scoreModel.getExtraRuns().getLegbyes()+", pen 0)";
        }
        else {
            extra="(nb "+scoreModel.getExtraRuns().getNoballs()+", wd "+scoreModel.getExtraRuns().getWides()+", b "+scoreModel.getExtraRuns().getByes()+
                    ", lb "+scoreModel.getExtraRuns().getLegbyes()+", pen "+scoreModel.getExtraRuns().getPenalty()+")";
        }

        String total="( "+scoreModel.getEquations().getWickets()+" Wickets, "+scoreModel.getEquations().getOvers()+" Overs)";

        holder.txtExtra.setText(extra);
        holder.txtTotal.setText(total);
        holder.didnotBatNames = holder.didnotBatNames.replaceAll(",$", "");
        holder.txtYetToBat.setText(holder.didnotBatNames);
        holder.txtTotalRun.setText(scoreModel.getEquations().getRuns()+"");

        if (scoreModel.getShow()){
            holder.layScore.setVisibility(View.VISIBLE);
            holder.imgTeamToggle.setImageResource(R.drawable.up_arrow_show);
            holder.layTeam.setBackgroundColor(context.getResources().getColor(R.color.appBackGround));
        }else {
            holder.layScore.setVisibility(View.GONE);
            holder.imgTeamToggle.setImageResource(R.drawable.down_arrow_hide);
            holder.layTeam.setBackgroundColor(context.getResources().getColor(R.color.selected));
        }

       /* for (int i = 0; i < scoreModel.getBatsmen().size(); i++) {
            ScoreModel.Batsman model=scoreModel.getBatsmen().get(i);
           // if (!model.getHowOut().equalsIgnoreCase("Not out")){
                //batterList.add(model);
            //}
        }*/

        holder.batterList.clear();
        holder.batterList.addAll(scoreModel.getBatsmen());

        LinearLayoutManager manager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.recyclerBatter.setLayoutManager(manager);
        holder.batterAdapter=new BatterAdapter(context,holder.batterList);
        holder.recyclerBatter.setAdapter(holder.batterAdapter);

       holder.bowlerList.clear();
       holder.bowlerList.addAll(scoreModel.getBowlers());

        LinearLayoutManager manager1=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.recyclerBowler.setLayoutManager(manager1);
        holder.bowlerAdapter=new BowlerAdapter(context,holder.bowlerList);
        holder.recyclerBowler.setAdapter(holder.bowlerAdapter);


       holder.fwlist.clear();
       holder.fwlist.addAll(scoreModel.getFows());

        LinearLayoutManager manager2=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.recyclerFW.setLayoutManager(manager2);
        holder.fallAdpter=new FallAdpter(context,holder.fwlist);
        holder.recyclerFW.setAdapter(holder.fallAdpter);


        holder.layTeam.setOnClickListener(view -> {

          /*  for (int i = 0; i < list.size(); i++) {
                list.get(position).setShow(false);
            }*/
            isFirstTime=false;
            scoreModel.setShow(!scoreModel.getShow());

            notifyDataSetChanged();


        });

    }

    public void notifyInnerAdapters(List<ScoreModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTeamName,txtTeamScore,txtExtra,txtTotal,txtYetToBat,txtTotalRun;
        private RecyclerView recyclerBatter,recyclerBowler,recyclerFW;
        private LinearLayout layTeam,layScore;
        private ImageView imgTeamToggle;

        private FallAdpter fallAdpter;
        private BowlerAdapter bowlerAdapter;
        private BatterAdapter batterAdapter;

        List<ScoreModel.Bowler> bowlerList = new ArrayList<>();
        List<ScoreModel.Batsman> batterList = new ArrayList<>();
        List<ScoreModel.Fow> fwlist = new ArrayList<>();
        String didnotBatNames= "";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTeamName=itemView.findViewById(R.id.txtTeamName);
            txtTeamName.setSelected(true);
            txtTeamScore=itemView.findViewById(R.id.txtTeamScore);
            layTeam=itemView.findViewById(R.id.layTeam);
            layScore=itemView.findViewById(R.id.layScore);
            recyclerBatter=itemView.findViewById(R.id.recyclerBatter);

            txtExtra=itemView.findViewById(R.id.txtExtra);
            txtTotal=itemView.findViewById(R.id.txtTotal);
            txtYetToBat=itemView.findViewById(R.id.txtYetToBat);
            txtTotalRun=itemView.findViewById(R.id.txtTotalRun);
            recyclerBowler=itemView.findViewById(R.id.recyclerBowler);
            recyclerFW=itemView.findViewById(R.id.recyclerFW);
            imgTeamToggle=itemView.findViewById(R.id.txtTeamToggle);
        }
    }
}
