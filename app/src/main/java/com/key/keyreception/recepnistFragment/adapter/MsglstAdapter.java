package com.key.keyreception.recepnistFragment.adapter;

/**
 * Created by Ravi Birla on 20,December,2018
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.key.keyreception.R;
import com.key.keyreception.activity.ChattingActivity;
import com.key.keyreception.activity.model.Chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ravi Birla on 18,December,2018
 */
public class MsglstAdapter extends RecyclerView.Adapter<MsglstAdapter.MyViewHolder> {

    private Context context;
    private List<Chat> chatList;
    private String sendIntent;

    public MsglstAdapter(Context context, List<Chat> chatList, String sendIntent) {
        this.context = context;
        this.chatList = chatList;
        this.sendIntent = sendIntent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.messagelayout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Chat chat = chatList.get(position);
        String time = "";
        if (chat.getProfilePic() != null && !chat.getProfilePic().isEmpty()) {
            Glide.with(context).load(chat.getProfilePic()).into(holder.chat_img);
        } else {
            Glide.with(context).load(R.drawable.user_upload).into(holder.chat_img);
        }
        holder.chat_tv_name.setText(chat.getName());
        if (chat.getIsImage() == 1) {
            holder.chat_tv_text.setText("Image");
        } else {
            holder.chat_tv_text.setText(chat.getMessage());
        }

        try {
            Date date = new Date((Long) chat.getTimestamp());
            Date cur_date = Calendar.getInstance().getTime();
            printDifference(context, date, cur_date, holder.chat_time_ago);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    private void printDifference(Context mContext, Date startDate, Date endDate, TextView textView) {
        long different;

        //milliseconds
        if (endDate.getTime() > startDate.getTime()) {
            different = endDate.getTime() - startDate.getTime();
        } else {
            different = startDate.getTime() - endDate.getTime();
        }
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthInMilli = weeksInMilli * 30;
        long yearInMilli = monthInMilli * 12;
        long elapsedYears = different / yearInMilli;
        different = different % yearInMilli;
        long elapsedMonths = different / monthInMilli;
        different = different % monthInMilli;
        long elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("hh:mm a");
        String time_str = sd.format(different);
        String time = time_str.replace("am", "AM").replace("pm", "PM");
        if (elapsedYears != 0) {
            if (elapsedYears == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedYears), mContext.getResources().getString(R.string.year_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedYears), mContext.getResources().getString(R.string.years_ago)));
            }
        } else if (elapsedMonths != 0) {
            if (elapsedMonths == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedMonths), mContext.getResources().getString(R.string.month_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedMonths), mContext.getResources().getString(R.string.months_ago)));
            }
        } else if (elapsedWeeks != 0) {
            if (elapsedWeeks == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedWeeks), mContext.getResources().getString(R.string.week_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedWeeks), mContext.getResources().getString(R.string.weeks_ago)));
            }
        } else if (elapsedDays != 0) {
            if (elapsedDays == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedDays), mContext.getResources().getString(R.string.day_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedDays), mContext.getResources().getString(R.string.days_ago)));
            }
        } else if (elapsedHours != 0) {
            if (elapsedHours == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedHours), mContext.getResources().getString(R.string.hour_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedHours), mContext.getResources().getString(R.string.hours_ago)));
            }

        } else if (elapsedMinutes != 0) {
            if (elapsedMinutes == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedMinutes), mContext.getResources().getString(R.string.minute_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedMinutes), mContext.getResources().getString(R.string.minutes_ago)));
            }
        } else if (elapsedSeconds != 0) {
            if (elapsedSeconds == 1) {
                textView.setText(String.format("%s %s", String.valueOf(elapsedSeconds), mContext.getResources().getString(R.string.second_ago)));
            } else {
                textView.setText(String.format("%s %s", String.valueOf(elapsedSeconds), mContext.getResources().getString(R.string.seconds_ago)));
            }

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView chat_tv_name, chat_tv_text, chat_time_ago;
        ImageView chat_img;
        RelativeLayout allrl;
        RatingBar pre_MyRating;


        public MyViewHolder(final View itemView) {
            super(itemView);

            chat_img = itemView.findViewById(R.id.chat_img);
            chat_tv_name = itemView.findViewById(R.id.chat_tv_name);
            chat_tv_text = itemView.findViewById(R.id.chat_tv_text);
            chat_time_ago = itemView.findViewById(R.id.chat_time_ago);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Chat chat = chatList.get(getAdapterPosition());
                    Intent intent = new Intent(context, ChattingActivity.class);
                    intent.putExtra("id", chat.getUid());
                    intent.putExtra("profileImage", chat.getProfilePic());
                    intent.putExtra("fullName", chat.getName());
                    context.startActivity(intent);

                }
            });
        }

    }
}
