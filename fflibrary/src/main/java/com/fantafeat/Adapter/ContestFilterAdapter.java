package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Fragment.ContestListInnerFragment;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.R;

import java.util.List;

public class ContestFilterAdapter extends RecyclerView.Adapter<ContestFilterAdapter.ContestFilterHolder> {

    private Context mContext;
    private Fragment fragment;
    private List<ContestModel> contestModelList;
    private static final String TAG = "ContestFilterAdapter";
    private int selected;

    public ContestFilterAdapter(Context mContext, List<ContestModel> contestModelList, Fragment fragment) {
        this.mContext = mContext;
        this.contestModelList = contestModelList;
        this.fragment = fragment;
        selected = 0;
    }

    @NonNull
    @Override
    public ContestFilterAdapter.ContestFilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContestFilterHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contest_header_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ContestFilterAdapter.ContestFilterHolder holder, int position) {
        holder.title.setText(contestModelList.get(holder.getAdapterPosition()).getTitle());

        if (holder.getAdapterPosition() == selected) {
            holder.contest_filter_layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.pureWhite));
        } else {
            holder.contest_filter_layout.setBackground(mContext.getResources().getDrawable(R.drawable.btn_contest_filter));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.font_color));
        }

        holder.contest_filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selected);
                selected = holder.getAdapterPosition();
                notifyItemChanged(holder.getAdapterPosition());
                ((ContestListInnerFragment) fragment).scrollToContest(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return contestModelList.size();
    }

    protected class ContestFilterHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout contest_filter_layout;

        public ContestFilterHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.contest_filter_title);
            contest_filter_layout = itemView.findViewById(R.id.contest_filter_layout);
        }
    }
}
