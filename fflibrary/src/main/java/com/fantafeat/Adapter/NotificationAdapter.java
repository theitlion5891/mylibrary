package com.fantafeat.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Model.NotificationModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHandler> {
    private Context mContext;
    private static final String TAG = "NotificationAdapter";

    List<NotificationModel> notificationModelArrayList;

    public NotificationAdapter(Context mContext, List<NotificationModel> notificationModelArrayList) {
        this.mContext = mContext;
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @NonNull
    @Override
    public NotificationHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHandler(LayoutInflater.from(mContext).inflate(R.layout.row_notification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHandler holder, int position) {
        NotificationModel List = notificationModelArrayList.get(position);


        if ((position%2)==0){
            //even
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.white_pure));
        }else {
            //odd
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.appBackGround));
        }
       // LogUtil.d("resp",List.getTitle());
        holder.notification_title.setText(List.getTitle());
        holder.notification_desc.setText(List.getMessage());
        SimpleDateFormat inputFormat = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = inputFormat.parse(List.getCreateAt());
            String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , MyApp.getCurrentDate().getTime(), DateUtils.MINUTE_IN_MILLIS);
            holder.notification_time.setText(niceDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(List.getImage().equals("")){
            holder.image_card.setVisibility(View.GONE);
        }else{
            holder.image_card.setVisibility(View.VISIBLE);
            CustomUtil.loadImageWithGlide(mContext,holder.notification_image,ApiManager.NOTIFICATION,List.getImage(),R.drawable.cricketback);
           /* Glide.with(mContext)
                    .load(ApiManager.NOTIFICATION + List.getImage())
                    .placeholder(R.drawable.cricketback)
                    .error(R.drawable.cricketback)
                    .into(holder.notification_image);*/
        }
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class NotificationHandler extends RecyclerView.ViewHolder{

        TextView notification_time,notification_title,notification_desc;
        ImageView notification_image;
        RelativeLayout main;
        CardView image_card;
        public NotificationHandler(@NonNull View itemView) {
            super(itemView);

            notification_title = (TextView) itemView.findViewById(R.id.notification_title);
            notification_time = (TextView) itemView.findViewById(R.id.notification_time);
            notification_desc = (TextView) itemView.findViewById(R.id.notification_desc);
            notification_image = (ImageView) itemView.findViewById(R.id.notification_image);
            main = itemView.findViewById(R.id.main);
            image_card = (CardView) itemView.findViewById(R.id.notification_image_card);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((HomeActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthPixels = displayMetrics.widthPixels;
            float heightPixels = (float) (widthPixels * 0.2);
            notification_image.setMinimumHeight((int) heightPixels);
        }
    }
    public void updateData(List<NotificationModel> notificationModelArrayList) {
        this.notificationModelArrayList = notificationModelArrayList;
        notifyDataSetChanged();
    }
}
