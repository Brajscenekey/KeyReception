package com.key.keyreception.ownerFragment.ownerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.owner.OwnerNotifiDetailActivity;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.recepnist.NotiDetailActivity;
import com.key.keyreception.activity.recepnist.TabActivity;
import com.key.keyreception.recepnistFragment.model.NotifiData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Session session;
    private Context context;
    private ArrayList<NotifiData> notifiList;
    private String id;


    public NotificationAdapter(Context context, ArrayList<NotifiData> notifiList, String id) {
        this.context = context;
        this.notifiList = notifiList;
        this.id = id;

    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notifilayout, parent, false);
        return new NotificationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.MyViewHolder holder, final int position) {

        NotifiData notifiData = notifiList.get(position);
        holder.noti_tv_name.setText(notifiData.getMessage());

        if (notifiData.getMessage().contains(notifiData.getSenderDetail().get(position).getFullName())) {
            int startingPosition = notifiData.getMessage().indexOf(notifiData.getSenderDetail().get(position).getFullName());
            int endingPosition = startingPosition + notifiData.getSenderDetail().get(position).getFullName().length();

            Spannable WordtoSpan = new SpannableString(notifiData.getMessage());
            WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), startingPosition, endingPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            WordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startingPosition, endingPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.noti_tv_name.setText(WordtoSpan);
        } else holder.noti_tv_name.setText(notifiData.getMessage());
        Date cDate = getCurrentDate(notifiData.getCurrent());
        Date eDate = getCurrentDate(notifiData.getCrd());
        printDifference(eDate, cDate, holder.noti_time);
    }

    @Override
    public int getItemCount() {
        return notifiList.size();
    }

    private Date getCurrentDate(String cDate) {
        Log.d("DATE", "C> " + cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        try {
            return sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    private void printDifference(Date startDate, Date endDate, TextView textView) {
        long different = endDate.getTime() - startDate.getTime();
        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays != 0) {
            textView.setText(String.valueOf(elapsedDays) + " " + context.getString(R.string.days_ago));
        } else if (elapsedHours != 0) {
            textView.setText(String.valueOf(elapsedHours) + " " + context.getString(R.string.hours_ago));
        } else if (elapsedMinutes != 0) {
            textView.setText(String.valueOf(elapsedMinutes) + " " + context.getString(R.string.minute_ago));
        } else if (elapsedSeconds != 0) {
            textView.setText(String.valueOf(elapsedSeconds) + " " + context.getString(R.string.second_ago));


        }
    }

    private void alertNFManage() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(context.getString(R.string.please_switch_user_mode_to_do_this));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                if (session.getusertype().equals("owner")) {
                    Intent intent = new Intent(context, OwnerTabActivity.class);
                    intent.putExtra("order", "3");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TabActivity.class);
                    intent.putExtra("order", "3");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView noti_imgLabour;
        TextView noti_tv_name, noti_time;

        public MyViewHolder(final View itemView) {
            super(itemView);
            session = new Session(context);
            noti_tv_name = itemView.findViewById(R.id.noti_tv_name);
            noti_imgLabour = itemView.findViewById(R.id.noti_imgLabour);
            noti_time = itemView.findViewById(R.id.noti_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotifiData notifiData = notifiList.get(getAdapterPosition());
                    if (session.getusertype().equals(notifiData.getForUserType())) {
                        if (id.equals("r1")) {
                            Intent intent = new Intent(context, NotiDetailActivity.class);
                            intent.putExtra("notificationDetail_Jobid", String.valueOf(notifiData.getNotifyId()));
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, OwnerNotifiDetailActivity.class);
                            intent.putExtra("notificationDetail_Jobid", String.valueOf(notifiData.getNotifyId()));
                            context.startActivity(intent);
                        }
                    } else {
                        alertNFManage();
                    }
                }

            });


        }
    }

}

